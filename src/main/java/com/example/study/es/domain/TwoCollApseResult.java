package com.example.study.es.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class TwoCollApseResult<R, E> extends Result<E>{
    private String field;
    private List<R> value;
}