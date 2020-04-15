package com.baizhi.lq.repository;

import com.baizhi.lq.entity.Video;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Author 李瓊
 * @Date 2020/4/12 16:17
 */
//泛型   <操作对象类型,序列化主键的类型>
public interface VideoRepository extends ElasticsearchRepository<Video, String> {
}
