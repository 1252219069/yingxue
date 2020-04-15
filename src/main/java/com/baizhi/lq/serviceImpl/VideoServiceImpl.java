package com.baizhi.lq.serviceImpl;

import com.baizhi.lq.annotation.AddCach;
import com.baizhi.lq.annotation.AddLog;
import com.baizhi.lq.annotation.DelCach;
import com.baizhi.lq.dao.VideoMapper;
import com.baizhi.lq.entity.Video;
import com.baizhi.lq.entity.VideoExample;
import com.baizhi.lq.repository.VideoRepository;
import com.baizhi.lq.service.VideoService;
import com.baizhi.lq.util.AliyunOssUtil;
import com.baizhi.lq.vo.VideoVo;
import org.apache.ibatis.session.RowBounds;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Author 李瓊
 * @Date 2020/3/31 22:11
 */
@Service
public class VideoServiceImpl implements VideoService {
    @Resource
    VideoMapper videoMapper;
    @Resource
    ElasticsearchTemplate elasticsearchTemplate;
    @Resource
    VideoRepository videoRepository;

    /**
     * 分页展示所有视频
     *
     * @param page
     * @param rows
     * @return
     */
    @Override
    @AddCach
    public HashMap<String, Object> queryByPageVideo(Integer page, Integer rows) {
        HashMap<String, Object> map = new HashMap<>();
        VideoExample example = new VideoExample();
        //获取总条数
        Integer records = videoMapper.selectCountByExample(example);
        map.put("records", records);
        //获取总页数
        Integer total = records % rows == 0 ? records / rows : records / rows + 1;
        map.put("total", total);
        //当前页
        map.put("page", page);
        //忽略几条  获取几条
        RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
        List<Video> videos = videoMapper.selectByExampleAndRowBounds(example, rowBounds);
        map.put("rows", videos);

        return map;
    }

    /**
     * 添加视频
     *
     * @param video
     * @return
     */
    @DelCach
    @AddLog("添加视频")
    @Override
    public String insertVideo(Video video) {
        String id = UUID.randomUUID().toString().replace("-", "");
        video.setId(id);
        video.setPublishDate(new Date());
        video.setCategoryName("视频");
        video.setCategoryId("2");
        video.setUserId("1");
        video.setGroupId("1");
        videoMapper.insert(video);
        return id;
    }

    /**
     * 删除视频
     *
     * @param video
     * @return
     */
    @DelCach
    @AddLog("删除视频")
    @Override
    public HashMap<String, Object> deleteVideo(Video video) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            //设置条件
            VideoExample example = new VideoExample();
            example.createCriteria().andIdEqualTo(video.getId());
            //根据id查询视频
            Video videos = videoMapper.selectOneByExample(example);

            //删除数据
            videoMapper.deleteByExample(example);
            //字符串拆分
            String[] pathSplit = videos.getPath().split("/");
            String[] coverSplit = videos.getCover().split("/");
            String videoName = pathSplit[3] + "/" + pathSplit[4];
            System.out.println(videoName);
            String coverName = coverSplit[3] + "/" + coverSplit[4];
            System.out.println(coverName);

            //删除视频
            AliyunOssUtil.delete("yingxue", videoName);
            //删除封面
            AliyunOssUtil.delete("yingxue", coverName);

            map.put("status", "200");
            map.put("message", "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", "400");
            map.put("message", "删除失败");
        }
        return map;
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DelCach
    @AddLog("批量删除视频")
    @Override
    public HashMap<String, Object> deleteVideos(String[] ids) {
        HashMap<String, Object> map = new HashMap<>();
        ArrayList<String> keys = new ArrayList<>();
        try {
            VideoExample example = new VideoExample();
            example.createCriteria().andIdIn(Arrays.asList(ids));
            List<Video> videos = videoMapper.selectByExample(example);
            for (Video video : videos) {
                String[] pathSplit = video.getPath().split("/");
                String[] coverSplit = video.getCover().split("/");

                String videoName = pathSplit[3] + "/" + pathSplit[4];
                String coverName = coverSplit[3] + "/" + coverSplit[4];
                keys.add(videoName);
                keys.add(coverName);

            }

            for (String id : ids) {
                Video video = videoMapper.selectByPrimaryKey(id);
                //删除索引
                videoRepository.delete(video);
            }
            //批量删除数据
            videoMapper.deleteByExample(example);
            //批量删除文件
            AliyunOssUtil.deletes("yingxue", keys);


            map.put("status", "200");
            map.put("message", "删除成功");


        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", "400");
            map.put("message", "删除失败");
        }
        return map;
    }

    /**
     * 文件上传
     *
     * @param path
     * @param id
     * @param request
     */
    @Override
    public void uploadVdieo(MultipartFile path, String id, HttpServletRequest request) {
        if ("".equals(path.getOriginalFilename())) {
            System.out.println("不做修改视频的操作");
        } else {
            Video video1 = videoMapper.selectByPrimaryKey(id);
            if (video1.getCover() != null) {

                //根据id查询视频
                Video videos = videoMapper.selectByPrimaryKey(id);

                //字符串拆分
                String[] pathSplit = videos.getPath().split("/");
                String[] coverSplit = videos.getCover().split("/");

                String videoName = pathSplit[3] + "/" + pathSplit[4];
                String coverName = coverSplit[3] + "/" + coverSplit[4];

                //删除视频
                AliyunOssUtil.delete("yingxue", videoName);
                //删除封面
                AliyunOssUtil.delete("yingxue", coverName);
            }
            //上传至阿里云
            //获取文件名
            String filename = path.getOriginalFilename();
            //给文件名加个时间戳
            String newName = System.currentTimeMillis() + "-" + filename;
            /**1.视频上传至阿里云
             *上传字节数组
             * 参数：
             *   bucket:存储空间名
             *   headImg: 指定MultipartFile类型的文件
             *   fileName:  指定上传文件名  可以指定上传目录：  目录名/文件名
             * */
            AliyunOssUtil.uploadFileBytes("yingxue", path, "video/" + newName);

            //频接视频完整路径
            String netFilePath = "liqiong.vip/video/" + newName;

            //将文件名拆分
            String[] name = newName.split("\\.");
            //获取到文件名
            String interceptName = name[0];
            //获得截取封面文件名
            String coverName = interceptName + ".jpg";

            /**
             * 截取视频第一帧做封面
             * newName:文件名
             * coverName: 封面名
             */
            AliyunOssUtil.videoCoverIntercept("yingxue", "video/" + newName, "coverPhotos/" + coverName);

            VideoExample example = new VideoExample();
            example.createCriteria().andIdEqualTo(id);
            //修改的结果
            Video video = new Video();
            //链接视频完整路径
            video.setPath("https://liqiong.vip/video/" + newName);
            video.setCover("https://liqiong.vip/coverPhotos/" + coverName);
            videoMapper.updateByExampleSelective(video, example);
            //设置id
            video.setId(id);
            Video videos = videoMapper.selectOne(video);
            //像es中构建索引
            videoRepository.save(videos);
        }
    }

    /**
     * 首页展示信息
     */
    @Override
    @AddCach
    public List<VideoVo> queryByReleaseTime() {
        List<VideoVo> videoVos = videoMapper.queryByReleaseTime();
        ArrayList<VideoVo> videoVosList = new ArrayList<VideoVo>();

        for (VideoVo videoVo : videoVos) {
            Integer likeCount = 18;
            VideoVo vo = new VideoVo();
            vo.setId(videoVo.getId());
            vo.setVideoTitle(videoVo.getVideoTitle());
            vo.setCover(videoVo.getCover());
            vo.setPath(videoVo.getPath());
            vo.setUploadTime(videoVo.getUploadTime());
            vo.setDescription(videoVo.getDescription());
            vo.setLikeCount(likeCount);
            vo.setCateName(videoVo.getCateName());
            vo.setHeadImg(videoVo.getHeadImg());
            videoVosList.add(vo);
        }
        return videoVosList;
    }

    /**
     * 修改视频
     *
     * @param video
     * @return
     */
    @DelCach
    @AddLog("修改视频")
    @Override
    public String updateVideo(Video video) {
        VideoExample example = new VideoExample();
        example.createCriteria().andIdEqualTo(video.getId());
        video.setPath(null);
        videoMapper.updateByExampleSelective(video, example);
        return video.getId();
    }

    /**
     * Es视频搜索
     *
     * @param content
     * @return
     */
    @AddCach
    @Override
    public List<Video> querySearch(String content, int rows, int page) {
        //处理高亮字段
        HighlightBuilder.Field field = new HighlightBuilder.Field("*");
        //前缀
        field.preTags("<span style='color:red'>");
        //后缀
        field.postTags("</span>");

        FieldSortBuilder order = SortBuilders.fieldSort("publishDate").order(SortOrder.DESC);

        //查询条件
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                //指定索引名
                .withIndices("yingxue")
                //指定索引类型
                .withTypes("video")
                //搜索的条件
                .withQuery(QueryBuilders.queryStringQuery(content).field("title").field("brief"))
                //处理高亮
                .withHighlightFields(field)
                //指定排序条件
                .withSort(order)
                //指定分页
                //.withPageable(PageRequest.of((page - 1) * rows,rows))
                .build();
        //高亮查询  参数:  查询条件   查询的类  返回结果的mapper
        AggregatedPage<Video> videoList = elasticsearchTemplate.queryForPage(nativeSearchQuery, Video.class, new SearchResultMapper() {

            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                ArrayList<Object> videos = new ArrayList<>();

                //获取查询结果
                SearchHit[] hits = searchResponse.getHits().getHits();
                for (SearchHit hit : hits) {
                    //原始数据
                    Map<String, Object> map = hit.getSourceAsMap();
                    //处理普通数据
                    String id = map.get("id") != null ? map.get("id").toString() : null;
                    String title = map.get("title") != null ? map.get("title").toString() : null;
                    String brief = map.get("brief") != null ? map.get("brief").toString() : null;
                    String path = map.get("path") != null ? map.get("path").toString() : null;
                    String cover = map.get("cover") != null ? map.get("cover").toString() : null;
                    String categoryId = map.get("categoryId") != null ? map.get("categoryId").toString() : null;
                    String groupId = map.get("groupId") != null ? map.get("groupId").toString() : null;
                    String userId = map.get("userId") != null ? map.get("userId").toString() : null;
                    String categoryName = map.get("categoryName") != null ? map.get("categoryName").toString() : null;

                    //处理日期操作
                    Date date = null;
                    if (map.get("publishDate") != null) {
                        String publishDate = map.get("publishDate").toString();
                        Long aLong = Long.valueOf(publishDate);
                        date = new Date(aLong);
                    }
                    //封装video对象
                    Video video = new Video(id, title, brief, path, cover, date, categoryId, groupId, userId, categoryName);
                    //处理高亮数据
                    Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                    if (title != null) {
                        if (highlightFields.get("title") != null) {
                            String titles = highlightFields.get("title").fragments()[0].toString();
                            video.setTitle(titles);
                        }
                    }
                    if (brief != null) {
                        if (highlightFields.get("brief") != null) {
                            String briefs = highlightFields.get("brief").fragments()[0].toString();
                            video.setBrief(briefs);
                        }
                    }

                    //将video对象放入集合
                    videos.add(video);
                }
                return new AggregatedPageImpl<T>((List<T>) videos);
            }
        });
        return videoList.getContent();
    }
}
