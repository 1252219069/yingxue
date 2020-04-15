package com.baizhi.lq;

import com.baizhi.lq.dao.VideoMapper;
import com.baizhi.lq.entity.Video;
import com.baizhi.lq.repository.VideoRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Author 李瓊
 * @Date 2020/4/12 16:26
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class VideoES {
    @Resource
    VideoRepository videoRepository;
    @Resource
    VideoMapper videoMapper;

    @Test
    public void querySearchVideo() {
        videoRepository.save(new Video("1", "小石头", "坚硬的小石头", "1.mp4", "1.jpg", new Date(), "1", "1", "1", "视频"));

    }

    @Test
    public void querySearchVideos() {
        List<Video> videos = videoMapper.selectAll();
        for (Video video : videos) {
            videoRepository.save(video);
        }
    }

    @Test
    public void querySearchVideoss() {
        String id = "059e6641494f465d92c547f9fc6b80b7";
        Video video = videoMapper.selectByPrimaryKey(id);
        System.out.println(video);
        //删除索引
        //videoRepository.delete(video);
    }

    @Test
    public void querySearchVideoe() {
        String id = "059e6641494f465d92c547f9fc6b80b7";
        Video video = videoMapper.selectByPrimaryKey(id);
        System.out.println(video);
    }

    @Test
    public void ceshi() {
        System.out.println("测试");
    }


}
