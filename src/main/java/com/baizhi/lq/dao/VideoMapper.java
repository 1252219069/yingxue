package com.baizhi.lq.dao;

import com.baizhi.lq.entity.Video;
import com.baizhi.lq.vo.VideoVo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface VideoMapper extends Mapper<Video> {
    List<VideoVo> queryByReleaseTime();
}