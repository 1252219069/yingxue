package com.baizhi.lq;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.PutObjectRequest;
import com.baizhi.lq.dao.AdminMapper;
import com.baizhi.lq.dao.UserMapper;
import com.baizhi.lq.dao.VideoMapper;
import com.baizhi.lq.entity.Admin;
import com.baizhi.lq.entity.AdminExample;
import com.baizhi.lq.entity.User;
import com.baizhi.lq.vo.VideoVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class YingxueApplicationTests {
    @Resource
    AdminMapper adminMapper;
    @Autowired
    UserMapper userMapper;
    @Resource
    VideoMapper videoMapper;

    @Test
    public void queryAll() {
        AdminExample example = new AdminExample();
        example.createCriteria().andUsernameEqualTo("admin");
        List<Admin> admins = adminMapper.selectByExample(example);

        admins.forEach(admin -> System.out.println(admin));
    }

    @Test
    public void queryAllUser() {
        List<User> users = userMapper.selectAll();
        users.forEach(user -> System.out.println(user));

    }


    @Test
    public void queryBucket() {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = "LTAI4FwciT45mP9c2VQYLUVi";
        String accessKeySecret = "04VTKVZI1ncVBLJEXY7PuFDGZS3Jic";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 列举存储空间。
        List<Bucket> buckets = ossClient.listBuckets();
        for (Bucket bucket : buckets) {
            System.out.println(" - " + bucket.getName());
        }

        // 关闭OSSClient。
        ossClient.shutdown();
    }

    @Test
    public void uploadAliyun() {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = "LTAI4FwciT45mP9c2VQYLUVi";
        String accessKeySecret = "04VTKVZI1ncVBLJEXY7PuFDGZS3Jic";
        String bucketName = "yingxue";  //存储空间名
        String objectName = "photos/美女.jpg";  //文件名  可以指定文件目录
        String localFile = "C:\\Users\\12522\\Pictures\\Camera Roll\\2.jpg";  //本地视频路径
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 创建PutObjectRequest对象。 参数：Bucket名字，指定文件名，文件本地路径
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, new File(localFile));
        // 上传文件。
        ossClient.putObject(putObjectRequest);
        // 关闭OSSClient。
        ossClient.shutdown();
    }


    @Test
    public void testVideoIntercept() {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = "LTAI4FwciT45mP9c2VQYLUVi";
        String accessKeySecret = "04VTKVZI1ncVBLJEXY7PuFDGZS3Jic";
        String bucketName = "yingxue";
        String objectName = "video/1585731754715刀山火海.mp4";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 设置视频截帧操作。
        String style = "video/snapshot,t_1000,f_jpg,w_0,h_0";
        // 指定过期时间为10分钟。
        Date expiration = new Date(new Date().getTime() + 1000 * 60 * 10);
        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, objectName, HttpMethod.GET);
        req.setExpiration(expiration);
        req.setProcess(style);
        URL signedUrl = ossClient.generatePresignedUrl(req);

        //输出图片网络路径
        System.out.println(signedUrl);

        //文件上传 阿里云

        // 上传网络流。
        InputStream inputStream = null;
        try {
            inputStream = new URL(signedUrl.toString()).openStream();
            ossClient.putObject(bucketName, "aaa.jpg", inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }


        // 关闭OSSClient。
        ossClient.shutdown();
        //sClient.shutdown();
    }

    @Test
    public void ceshi() {
        List<VideoVo> videoVos = videoMapper.queryByReleaseTime();
        videoVos.forEach(videoVo -> System.out.println(videoVo));
    }
}
