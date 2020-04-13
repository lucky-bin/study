package com.example.study.es.service;

import com.example.study.es.entity.Employee;
import com.example.study.es.repository.EmployeeRepository;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.*;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.*;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.NumericMetricsAggregation;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.collapse.CollapseBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.term.TermSuggestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

/**
 *  spring es无法使用 search after
 *  如果需要使用search after 请使用 RestHighLevelClient自行构建 request进行解析respone
 *  spring 只解析了 _source 中的内容  如果解析的内容不在_source中，请使用原生客户端
 *  hightlight 和 Aggregations 例外，但是需要自己解析
 */
@Service
public class EsService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ElasticsearchRestTemplate template;
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * spring 封装的一些基于id的增删改查
     */
    public void base(){
        Employee employee = new Employee()
                .setDescribe("ElasticSearch是一个基于Lucene的搜索服务器。" +
                        "它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口。 " +
                        "Elasticsearch是用Java语言开发的，并作为Apache许可条款下的开放源码发布，" +
                        "是一种流行的企业级搜索引擎。")
                .setGender("男")
                .setHiredate(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli())
                .setName("张三")
                .setSalary(BigDecimal.valueOf(Math.floor(new Random().nextDouble() * 1000000.0) / 100));

        // 插入
        employeeRepository.save(employee);
        System.out.println(employee.getId());
        // 根据id查询
        System.out.println(employeeRepository.findById(employee.getId()).get());

        // 更新  有id且es中有数据 则为更新
        employee.setGender("女");
        employeeRepository.save(employee);
        // 根据id查询
        System.out.println(employeeRepository.findById(employee.getId()).get());

        employeeRepository.deleteById(employee.getId());
        if (employeeRepository.findById(employee.getId()).isEmpty()){
            System.out.println(employee.getId() + "被删除");
        }
    }

    /**
     * 等于查询
     */
    public void termLevelQuery(){
        TermQueryBuilder termQueryBuilder = QueryBuilders
                .termQuery("name.keyword", "张三1");
        Query termQuery = new NativeSearchQueryBuilder().withQuery(termQueryBuilder).build();
        SearchHits<Employee> termHits = template.search(termQuery, Employee.class);
        for (SearchHit<Employee> hit : termHits.getSearchHits()) {
            Employee employee = hit.getContent();
            System.out.println(employee);
        }

        TermsQueryBuilder termsQueryBuilder = QueryBuilders
                .termsQuery("name.keyword", "张三1", "李四1");
        Query termsquery = new NativeSearchQueryBuilder().withQuery(termsQueryBuilder).build();
        SearchHits<Employee> termsHits = template.search(termsquery, Employee.class);
        for (SearchHit<Employee> hit : termsHits.getSearchHits()) {
            Employee employee = hit.getContent();
            System.out.println(employee);
        }

        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("salary")
                .gt(BigDecimal.ZERO)
                .lte(BigDecimal.TEN);
        Query rangeQuery = new NativeSearchQueryBuilder().withQuery(rangeQueryBuilder).build();
        SearchHits<Employee> rangeHits = template.search(rangeQuery, Employee.class);
        for (SearchHit<Employee> hit : rangeHits.getSearchHits()) {
            Employee employee = hit.getContent();
            System.out.println(employee);
        }
    }

    /**
     * 全文检索
     */
    public void fullTextQuery(){
        MatchQueryBuilder matchQueryBuilder = QueryBuilders
                .matchQuery("name", "张三");
        SortBuilder sortBuilder = new FieldSortBuilder("hiredate").order(SortOrder.DESC);
        Pageable pageable = PageRequest.of(0, 10);
        Query matchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQueryBuilder)
                .withPageable(pageable)
                .withSort(sortBuilder)
                .build();
        SearchHits<Employee> matchHits = template.search(matchQuery, Employee.class);
        for (SearchHit<Employee> hit : matchHits.getSearchHits()) {
            Employee employee = hit.getContent();
            System.out.println(employee);
        }
    }

    /**
     * 布尔查询
     */
    public void boolQuery(){
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("name", "张三i"))
                .must(QueryBuilders.rangeQuery("salary").lt(BigDecimal.TEN));
        SortBuilder sortBuilder = new FieldSortBuilder("hiredate").order(SortOrder.DESC);
        Pageable pageable = PageRequest.of(0, 10);
        Query boolQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withPageable(pageable)
                .withSort(sortBuilder)
                .build();
        SearchHits<Employee> boolHits = template.search(boolQuery, Employee.class);
        for (SearchHit<Employee> hit : boolHits.getSearchHits()) {
            Employee employee = hit.getContent();
            System.out.println(employee);
        }
    }

    /**
     * 折叠查询
     */
    public void collapse(){
        CollapseBuilder collapseBuilder = new CollapseBuilder("salary")
                .setInnerHits(
                    new InnerHitBuilder("testName").setInnerCollapse(
                            new CollapseBuilder("salary")
                    ).setSize(10)
                );
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery())
                .collapse(collapseBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            return;
        }
        // 遍历结果集
        org.elasticsearch.search.SearchHits hits = response.getHits();
        for (org.elasticsearch.search.SearchHit hit : hits.getHits()) {
            // 获取第一级折叠结果   key = fieldname
            Map<String, DocumentField> fields = hit.getFields();
            for (String field : fields.keySet()) {
                DocumentField documentField = fields.get(field);
                if (documentField.getValue() instanceof Number) {
                    BigDecimal value = new BigDecimal(documentField.getValue().toString());
                    System.out.println("一级折叠： key ： " + field + ", value: " + value);
                }
            }
            // 获取第二级的折叠结果   key = 第二级折叠的名字  value 折叠结果集
            Map<String, org.elasticsearch.search.SearchHits> innerHits = hit.getInnerHits();
            for (String innerHitKey : innerHits.keySet()) {
                System.out.println("第二级折叠: key: " + innerHitKey);
                org.elasticsearch.search.SearchHits searchHits = innerHits.get(innerHitKey);
                Iterator<org.elasticsearch.search.SearchHit> iterator = searchHits.iterator();
                while (iterator.hasNext()){
                    org.elasticsearch.search.SearchHit searchHit = iterator.next();
                    Map<String, DocumentField> searchHitFields = searchHit.getFields();
                    for (String key : searchHitFields.keySet()) {
                        DocumentField documentField = searchHitFields.get(key);
                        if (documentField.getValue() instanceof Number) {
                            BigDecimal value = new BigDecimal(documentField.getValue().toString());
                            System.out.println("第二级折叠： key ： " + key + ", value: " + value);
                        }
                    }
                }
            }
        }
    }

    /**
     * 查询建议
     */
    public void suggest(){
        // 纠错
        SuggestBuilder suggestBuilder = new SuggestBuilder();
        String suggestName = "describe_suggest";
        suggestBuilder.addSuggestion(suggestName,
                SuggestBuilders.termSuggestion("describe").text("ElasticSearh"));
        SearchResponse searchResponse = template.suggest(suggestBuilder, IndexCoordinates.of("employee"));
        Suggest.Suggestion<TermSuggestion.Entry> suggestion = searchResponse.getSuggest().getSuggestion(suggestName);
        List<TermSuggestion.Entry> entries = suggestion.getEntries();
        for (TermSuggestion.Entry entry : entries) {
            List<TermSuggestion.Entry.Option> options = entry.getOptions();
            for (TermSuggestion.Entry.Option option : options) {
                // 获取纠错结果
                System.out.println(option.getText());
            }
        }

        // TODO 短语纠错
        /*SuggestBuilder suggestBuilder = new SuggestBuilder();
        Map<String, Object> param = new HashMap();
        param.put("field_name", "describe");
        suggestBuilder.addSuggestion("describe_suggest",
                SuggestBuilders.phraseSuggestion("describe")
                        //错误的词会被高亮
                        .highlight("<storng>", "</storng>")
                        .text("ElasticSearc Lucene")
                        // 可以传递空
                        .collateQuery("""
                                {
                                    "match" : {
                                        "{{field_name}}" : "{{suggest}}"
                                    }
                                }
                                """)
                        .collateParams(param)
                        // 使用此参数必须要使用collateQuery方法
                        // true 如果不是短语，会分成多个词进行纠错，然后进行组合成短语返回
                        // false 如果短语在字段中没有找到 不会返回任何信息  默认false
                        .collatePrune(true)
        );*/


        // TODO 自动补全  由于 completionSuggest 完全基于内存，很昂贵，要求不是很苛刻可以考虑  match_prefix_phrase
        // TODO 注意 只能对字段类型是 completion 的字段使用 completionSuggest
         /*suggestBuilder.addSuggestion(suggestName,
                        SuggestBuilders.completionSuggestion("describe").prefix("ElasticSearh"));*/


    }

    /**
     * 高亮
     */
    public void highlight(){
        // es client
        SearchRequest searchRequest = new SearchRequest();
        HighlightBuilder highlightBuilder = new HighlightBuilder()
                .field("name")
                .preTags("<storng>")
                .postTags("</storng>");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(QueryBuilders.matchQuery("name", "张三"))
                .highlighter(highlightBuilder)
                .size(10);
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            return;
        }
        org.elasticsearch.search.SearchHits hits = response.getHits();
        for (org.elasticsearch.search.SearchHit hit : hits) {
            Map<String, HighlightField> highlightMap = hit.getHighlightFields();
            Text[] hightLightBodys = highlightMap.get("name").fragments();
            for (Text hightLightBody : hightLightBodys) {
                System.out.println(hightLightBody.string());
            }
        }

        // Spring
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("name", "张三"))
                .withPageable(PageRequest.of(0, 1))
                .withHighlightBuilder(new HighlightBuilder().field("name").preTags("<p>") .postTags("</p>"))
                .build();
        SearchHits<Employee> search = template.search(query, Employee.class);
        List<SearchHit<Employee>> searchHits = search.getSearchHits();
        for (SearchHit<Employee> searchHit : searchHits) {
            List<String> highlightField = searchHit.getHighlightField("name");
            System.out.println(highlightField);
        }
    }

    /**
     * 指标聚合
     */
    public void metricsAggregations(){
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .addAggregation(AggregationBuilders.avg("avg_salary").field("salary"))
                .withQuery(QueryBuilders.matchQuery("name", "张三"))
                .withPageable(PageRequest.of(0, 1))
                .build();
        SearchHits<Employee> search = template.search(query, Employee.class);
        Aggregations aggregations = search.getAggregations();
        Map<String, Aggregation> aggregationsMap = aggregations.getAsMap();

        if (aggregationsMap.get("avg_salary") instanceof NumericMetricsAggregation.SingleValue metricsAggregation){
            System.out.println(metricsAggregation.value());
        }
    }

    /**
     * 桶聚合
     */
    public void bulkAggregations(){

        AggregationBuilder salaryCount = AggregationBuilders.count("salary_count").field("salary");
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("salaryCount", "salary_count");
        Script script = new Script("params.salaryCount > 2");
        PipelineAggregationBuilder having = PipelineAggregatorBuilders
                .bucketSelector("having", paramMap, script);
        TermsAggregationBuilder builder = AggregationBuilders.terms("by_salary")
                .field("salary")
                .subAggregation(salaryCount)
                .subAggregation(having);
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.rangeQuery("salary").gte(BigDecimal.ZERO).lte(100.00))
                .addAggregation(builder)
                .withFilter(QueryBuilders.termQuery("salary", 5835.03))
                .withPageable(PageRequest.of(0, 1))
                .build();
        SearchHits<Employee> search = template.search(query, Employee.class);
        Aggregations aggregations = search.getAggregations();
        List<Aggregation> aggregationList = aggregations.asList();
        for (Aggregation aggregation : aggregationList) {
            if(aggregation instanceof MultiBucketsAggregation multiBucketsAggregation) {
                List<? extends MultiBucketsAggregation.Bucket> buckets = multiBucketsAggregation.getBuckets();
                for (MultiBucketsAggregation.Bucket bucket : buckets) {
                    System.out.println(bucket.getKeyAsString());
                    Aggregations aggregations1 = bucket.getAggregations();
                    Iterator<Aggregation> iterator = aggregations1.iterator();
                    while (iterator.hasNext()){
                        if (iterator.next() instanceof NumericMetricsAggregation.SingleValue number) {
                            System.out.println(number.getName() + ":" + number.value());
                        }
                    }
                }
            }
        }
    }
}
