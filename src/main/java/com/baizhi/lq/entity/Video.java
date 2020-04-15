package com.baizhi.lq.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Document(indexName = "yingxue", type = "video")
@Table(name = "video")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Video implements Serializable {
    @Id
    private String id;
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String title;
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String brief;
    @Field(type = FieldType.Keyword)
    private String path;
    @Field(type = FieldType.Keyword)
    private String cover;

    @Column(name = "publish_date")
    @Field(type = FieldType.Date)
    private Date publishDate;

    @Column(name = "category_id")
    @Field(type = FieldType.Keyword)
    private String categoryId;

    @Column(name = "group_id")
    @Field(type = FieldType.Keyword)
    private String groupId;

    @Column(name = "user_id")
    @Field(type = FieldType.Keyword)
    private String userId;

    @Column(name = "category_name")
    @Field(type = FieldType.Keyword)
    private String categoryName;


}