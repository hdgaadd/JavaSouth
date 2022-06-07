package com.codeman.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author hdgaadd
 * Created on 2021/12/06/21:26
 */
@Data
@Document(indexName = "esteacher", shards = 1,replicas = 0)
public class EsProduct{
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;
    @Field(type = FieldType.Text)
    private String name;
    @Field(type = FieldType.Keyword)
    private String icon;
}
