package com.example.study.es.client;

public class EsClientBulider {

    private EsClientBulider(){

    }

    /**
     * 构建es 折叠查询客户端
     * @param collapseFieldType 折叠字段的类型
     * @param twoCollapseFieldType 第二次折叠字段的类型
     * @param sourceClass
     * @return
     */
    public static <T, R, E>CollapseEsClient<T, R, E> collapse(Class<T> collapseFieldType,
                                                              Class<R> twoCollapseFieldType,
                                                              Class<E> sourceClass){
        return new CollapseEsClient<>();
    }
}
