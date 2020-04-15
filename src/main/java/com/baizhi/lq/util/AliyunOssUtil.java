package com.baizhi.lq.util;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.DeleteObjectsResult;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.PutObjectRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AliyunOssUtil {

    private static String endpoint = "http://oss-cn-beijing.aliyuncs.com";
    // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
    private static String accessKeyId = "LTAI4FwciT45mP9c2VQYLUVi";
    private static String accessKeySecret = "04VTKVZI1ncVBLJEXY7PuFDGZS3Jic";

    private static String bucket = "yingxue";

    /**
     * 上传本地文件
     * 参数：
     * bucket:  存储空间名
     * fileName:  指定上传文件名  可以指定上传目录：  目录名/文件名
     * localFilePath: 指定本地文件路径
     */
    public static void uploadFile(String bucket, String fileName, String localFilePath) {

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 创建PutObjectRequest对象。
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, new File(localFilePath));

        // 上传文件。
        ossClient.putObject(putObjectRequest);

        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /*
     *上传字节数组
     * 参数：
     *   bucket:存储空间名
     *   headImg: 指定MultipartFile类型的文件
     *   fileName:  指定上传文件名  可以指定上传目录：  目录名/文件名
     * */
    public static void uploadFileBytes(String bucket, MultipartFile headImg, String fileName) {

        //转为字节数组
        byte[] bytes = new byte[0];
        try {
            bytes = headImg.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传Byte数组。
        ossClient.putObject(bucket, fileName, new ByteArrayInputStream(bytes));

        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /**
     * 删除阿里云文件
     * 参数：
     * bucker: 存储空间名
     * fileName:文件名    目录名/文件名
     */
    public static void delete(String bucket, String fileNme) {

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
        ossClient.deleteObject(bucket, fileNme);

        // 关闭OSSClient。
        ossClient.shutdown();
    }

    public static void deletes(String bucket, List<String> keys) {

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 删除文件。key等同于ObjectName，表示删除OSS文件时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
       /* List<String> keys = new ArrayList<String>();
        keys.add("key0");
        keys.add("key1");
        keys.add("key2");*/

        DeleteObjectsResult deleteObjectsResult = ossClient.deleteObjects(new DeleteObjectsRequest(bucket).withKeys(keys));
        List<String> deletedObjects = deleteObjectsResult.getDeletedObjects();
        for (String deletedObject : deletedObjects) {
            System.out.println("deletedObject = " + deletedObject);
        }
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /**
     * 视频截取  并上传至阿里云
     * 参数：
     * bucker: 存储空间名
     * fileName:远程文件文件名    目录名/文件名
     * coverName：截取的封面名
     */
    public static void videoCoverIntercept(String bucket, String fileNme, String coverName) {

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 设置视频截帧操作。
        String style = "video/snapshot,t_1000,f_jpg,w_0,h_0";
        // 指定过期时间为10分钟。
        Date expiration = new Date(System.currentTimeMillis() + 1000 * 60 * 10);
        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucket, fileNme, HttpMethod.GET);
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
            ossClient.putObject(bucket, coverName, inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 关闭OSSClient。
        ossClient.shutdown();
        //sClient.shutdown();
    }

    public static void main(String[] args) {
        ArrayList<String> Keys = new ArrayList<>();
        Keys.add("video/1585744579242-小蚂蚁.mp4");
        Keys.add("coverPhotos/1585744579242-小蚂蚁.jpg");
        deletes("yingxue", Keys);
    }
}
