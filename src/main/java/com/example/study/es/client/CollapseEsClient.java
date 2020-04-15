package com.example.study.es.client;

import com.example.study.es.domain.CollapseResult;
import com.example.study.es.domain.Result;
import com.example.study.es.domain.TwoCollApseResult;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.collapse.CollapseBuilder;
import org.springframework.data.elasticsearch.core.document.SearchDocumentResponse;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CollapseEsClient<T, R, E> extends AbstractEsClient<E, CollapseEsClient<T, R, E>> {

    CollapseEsClient(){
        super();
    }

    /**
     * 构建折叠查询
     * @param collapseBuilder 折叠查询
     * @return
     */
    public CollapseEsClient<T, R, E> collapse(CollapseBuilder collapseBuilder){
        searchSourceBuilder.collapse(collapseBuilder);
        return this;
    }

    @Override
    protected List<Result<E>> convert(Class<E> clazz) {
        SearchHits hits = searchResponse.getHits();
        if(Objects.nonNull(hits)){
            return oneLevelConvert(clazz);
        }
        return Collections.emptyList();
    }

    /**
     * 一级折叠查询结果转换
     * @param clazz
     * @return
     */
    private List<Result<E>> oneLevelConvert(Class<E> clazz){
        List<Result<E>> results = Lists.newArrayList();
        SearchDocumentResponse documentResponse = SearchDocumentResponse.from(searchResponse);
        org.springframework.data.elasticsearch.core.SearchHits<E> searchHits =
                elasticsearchConverter.read(clazz, documentResponse);
        SearchHits oneLevelHits = searchResponse.getHits();
        for (int i = 0; i < oneLevelHits.getHits().length; i++) {
            SearchHit oneLevelHit = oneLevelHits.getHits()[i];
            Map<String, DocumentField> oneLevelHitFields = oneLevelHit.getFields();
            org.springframework.data.elasticsearch.core.SearchHit<E> oneLevelSearchHit = searchHits.getSearchHit(i);

            String oneLevelKey = oneLevelHitFields.keySet().iterator().next();
            CollapseResult<T, R, E> result = new CollapseResult();
            result.setField(oneLevelKey)
                    .setValue((List<T>) oneLevelHitFields.get(oneLevelKey).getValues())
                    .setCollapse(twoLevelConvert(clazz, oneLevelHit))
                    .setSource(oneLevelSearchHit.getContent());
            results.add(result);
        }

        return results;
    }

    /**
     * 二级折叠结果转换
     * @param clazz
     * @param oneLevelHit
     * @return
     */
    private Map<String, List<TwoCollApseResult<R, E>>> twoLevelConvert(Class<E> clazz, SearchHit oneLevelHit){
        Map<String, SearchHits> innerHits = oneLevelHit.getInnerHits();
        if(CollectionUtils.isEmpty(innerHits)){
            return Collections.emptyMap();
        }
        Map<String, List<TwoCollApseResult<R, E>>> twoLevelResult = Maps.newHashMap();
        for (Map.Entry<String, SearchHits> twoLevelHitsResult : innerHits.entrySet()) {

            SearchHits twoLevelHitsResults = twoLevelHitsResult.getValue();
            SearchDocumentResponse documentResponse = from(twoLevelHitsResults);
            org.springframework.data.elasticsearch.core.SearchHits<E> searchHits =
                    elasticsearchConverter.read(clazz, documentResponse);
            SearchHit[] twoLevelHits = twoLevelHitsResults.getHits();

            List<TwoCollApseResult<R, E>> twoCollApseResults = Lists.newArrayList();
            for (int i = 0; i < twoLevelHits.length; i++) {
                SearchHit twoLevelHit = twoLevelHits[i];
                org.springframework.data.elasticsearch.core.SearchHit<E> searchHit = searchHits.getSearchHit(i);

                Map<String, DocumentField> twoLevelHitFields = twoLevelHit.getFields();
                String twoLevelFieldValue = twoLevelHitFields.keySet().iterator().next();

                TwoCollApseResult<R, E> twoCollApseResult = new TwoCollApseResult<>();
                twoCollApseResult.setField(twoLevelFieldValue)
                        .setValue((List<R>)twoLevelHitFields.get(twoLevelFieldValue).getValues())
                        .setSource(searchHit.getContent());

                twoCollApseResults.add(twoCollApseResult);
            }
            twoLevelResult.put(twoLevelHitsResult.getKey(), twoCollApseResults);
        }

        return twoLevelResult;
    }
}
