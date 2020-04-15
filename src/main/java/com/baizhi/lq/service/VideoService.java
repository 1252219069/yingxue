package com.baizhi.lq.service;

import com.baizhi.lq.entity.Video;
import com.baizhi.lq.vo.VideoVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * @Author 李瓊
 * @Date 2020/3/31 22:09
 */
public interface VideoService {
    /**
     * 分页查询所有视频
     *
     * @param page
     * @param rows
     * @return
     */
    HashMap<String, Object> queryByPageVideo(Integer page, Integer rows);

    /**
     * 添加视频
     *
     * @param video
     */
    String insertVideo(Video video);

    /**
     * 修改视频
     *
     * @param video
     */
    String updateVideo(Video video);

    /**
     * 删除视频
     *
     * @param video
     */
    HashMap<String, Object> deleteVideo(Video video);

    /**
     * 批量视频
     *
     * @param ids
     * @return
     */
    HashMap<String, Object> deleteVideos(String[] ids);

    /**
     * 视频上传
     *
     * @param path
     * @param id
     * @param request
     */
    void uploadVdieo(MultipartFile path, String id, HttpServletRequest request);

    /**
     * 前台视频数据
     *
     * @return
     */
    List<VideoVo> queryByReleaseTime();

    List<Video> querySearch(String content, int rows, int page);

}
