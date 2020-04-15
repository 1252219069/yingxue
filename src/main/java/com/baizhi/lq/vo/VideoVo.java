package com.baizhi.lq.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author 李瓊
 * @Date 2020/4/7 21:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoVo {

    private String id;       //视频id
    private String videoTitle; //视频标题
    private String cover;      //视频封面
    private String path;       //视频路径
    private Date uploadTime;  //视频上传时间
    private String description;   //视频简介
    private Integer likeCount;  //点赞数

    private String cateName;    //视频所属类别

    private String headImg;     //用户头像
}
