package com.example.study.es.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.HighlightField;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@Document(indexName = "employee", createIndex = false)
public class Employee {

    @Id
    private String id;
    @Field(type = FieldType.Text)
    private String name;
    @Field(type = FieldType.Date)
    private Long hiredate;
    @Field(type = FieldType.Double)
    private BigDecimal salary;
    @Field(type = FieldType.Keyword)
    private String gender;
    @Field(type = FieldType.Text, store = true)
    private String describe;
    @HighlightField(name = "name")
    private String hightLightName;
}
