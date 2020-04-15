package com.example.study.es.client;

import com.example.study.es.domain.Result;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.data.elasticsearch.core.document.DocumentAdapters;
import org.springframework.data.elasticsearch.core.document.SearchDocument;
import org.springframework.data.elasticsearch.core.document.SearchDocumentResponse;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class AbstractEsClient<E, QB extends AbstractEsClient> {

    // 请求体
    private SearchRequest searchRequest;
    protected SearchSourceBuilder searchSourceBuilder;

    // es高级客户端   用于发起请求
    private @Nullable RestHighLevelClient restHighLevelClient;
    // spring 容器中的 ElasticsearchConverter  用于转换es结果
    protected @Nullable ElasticsearchConverter elasticsearchConverter;
    protected SearchResponse searchResponse = null;

    protected AbstractEsClient() {
        this.searchRequest = new SearchRequest();
        this.searchSourceBuilder = new SearchSourceBuilder();
    }

    public QB client(RestHighLevelClient restHighLevelClient){
        this.restHighLevelClient = restHighLevelClient;
        return (QB)this;
    }

    public QB elasticsearchConverter(ElasticsearchConverter elasticsearchConverter){
        this.elasticsearchConverter = elasticsearchConverter;
        return (QB)this;
    }

    public QB indices(String ... indices){
        this.searchRequest.indices(indices);
        return (QB)this;
    }

    public QB indexBoost(String index, Number indexBoost){
        searchSourceBuilder.indexBoost(index, indexBoost.floatValue());
        return (QB)this;
    }

    public QB query(QueryBuilder queryBuilder){
        searchSourceBuilder.query(queryBuilder);
        return (QB)this;
    }

    public QB sort(String name, SortOrder sortOrder){
        searchSourceBuilder.sort(name);
        return (QB)this;
    }

    public QB size(int size){
        searchSourceBuilder.size(size);
        return (QB)this;
    }

    public QB searchAfter(Object[] values){
        searchSourceBuilder.searchAfter(values);
        return (QB)this;
    }

    /**
     * 执行查询并进行解析
     * @param clazz
     * @return
     */
    public List<Result<E>> execute(Class<E> clazz){
        Objects.requireNonNull(restHighLevelClient, "restHighLevelClient must not null");
        Assert.notNull(elasticsearchConverter, "elasticsearchConverter must not be null.");
        search();
        return convert(clazz);
    }

    /**
     * 获取串行结果
     */
    private void search(){
        searchRequest.source(searchSourceBuilder);
        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException("执行失败");
        }
    }

    /**
     * 对结果集进行解析
     * @param clazz
     * @return
     */
    protected abstract List<Result<E>> convert(Class<E> clazz);

    /**
     * 获取spring的  SearchDocumentResponse，便于直接转换成相应的实体类
     * @param searchHits
     * @return
     */
    protected SearchDocumentResponse from(SearchHits searchHits){
        Assert.notNull(searchHits, "searchHits must not be null");

        TotalHits responseTotalHits = searchHits.getTotalHits();
        long totalHits = responseTotalHits.value;
        String totalHitsRelation = responseTotalHits.relation.name();

        float maxScore = searchHits.getMaxScore();
        String scrollId = null;

        List<SearchDocument> searchDocuments = StreamSupport.stream(searchHits.spliterator(), false)
                .filter(Objects::nonNull)
                .map(DocumentAdapters::from)
                .collect(Collectors.toList());

        Aggregations aggregations = null;

        Object[] params = new Object[]{totalHits, totalHitsRelation, maxScore, scrollId, searchDocuments, aggregations};

        try {
            Class<SearchDocumentResponse> responseClass = SearchDocumentResponse.class;
            Constructor<SearchDocumentResponse> constructor = (Constructor<SearchDocumentResponse>) responseClass.getDeclaredConstructors()[0];
            constructor.setAccessible(true);
            return constructor.newInstance(params);
        }catch (Exception e) {
            return null;
        }
    }
}
