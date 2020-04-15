package com.baizhi.lq.controller;

import com.baizhi.lq.entity.Video;
import com.baizhi.lq.service.VideoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 李瓊
 * @Date 2020/3/31 22:19
 */
@Controller
@RequestMapping("video")
public class VideoController {
    @Resource
    VideoService videoService;

    @ResponseBody
    @RequestMapping("videoPage")
    public Map<String, Object> videoPage(Integer page, Integer rows) {
        HashMap<String, Object> map = videoService.queryByPageVideo(page, rows);
        return map;
    }

    @ResponseBody
    @RequestMapping("insert")
    public Object edit(Video video, String oper, String[] id) {
        if (oper.equals("add")) {
            String uid = videoService.insertVideo(video);
            return uid;
        }

        if (oper.equals("edit")) {
            String uid = videoService.updateVideo(video);
            return uid;
        }

        if (oper.equals("del")) {
            //HashMap<String, Object> map = videoService.deleteVideo(video);
            HashMap<String, Object> map = videoService.deleteVideos(id);
            return map;
        }

        return "";
    }

    @ResponseBody
    @RequestMapping("uploadVdieo")
    public void uploadVdieo(MultipartFile path, String id, HttpServletRequest request) {
        videoService.uploadVdieo(path, id, request);
    }

    @ResponseBody
    @RequestMapping("querySearch")
    public List<Video> querySearch(String content, int rows, int page) {
        List<Video> videos = videoService.querySearch(content, rows, page);
        return videos;
    }
}
