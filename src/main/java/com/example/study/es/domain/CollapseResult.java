package com.example.study.es.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class CollapseResult<T, R,  E> extends Result<E> {

    private String field;
    private List<T> value;
    private Map<String, List<TwoCollApseResult<R, E>>> collapse;
}
