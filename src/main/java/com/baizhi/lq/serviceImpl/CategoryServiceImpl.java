package com.baizhi.lq.serviceImpl;

import com.baizhi.lq.annotation.AddCach;
import com.baizhi.lq.annotation.AddLog;
import com.baizhi.lq.annotation.DelCach;
import com.baizhi.lq.dao.CategoryMapper;
import com.baizhi.lq.entity.Category;
import com.baizhi.lq.entity.CategoryExample;
import com.baizhi.lq.service.CategoryService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @Author 李瓊
 * @Date 2020/3/30 19:58
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Resource
    CategoryMapper categoryMapper;

    /**
     * 分页展示一级类别
     *
     * @param page
     * @param rows
     * @return
     */
    @Override
    @AddCach
    public HashMap<String, Object> queryByOneCategory(Integer page, Integer rows) {
        HashMap<String, Object> map = new HashMap<>();
        //设置条件类别为 1及类别
        CategoryExample example = new CategoryExample();
        example.createCriteria().andLevelsEqualTo("1");
        //获取一级类别总条数
        Integer records = categoryMapper.selectCountByExample(example);
        map.put("records", records);
        //总页数   total   总条数/每页展示条数  是否有余数
        Integer total = records % rows == 0 ? records / rows : records / rows + 1;
        map.put("total", total);
        //当前页
        map.put("page", page);
        //参数  忽略几条  获取几条
        RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
        //分页查询所有一级类别
        List<Category> categories = categoryMapper.selectByExampleAndRowBounds(example, rowBounds);
        map.put("rows", categories);
        return map;
    }

    /**
     * 分页展示二级类别
     *
     * @param page
     * @param rows
     * @param parentId
     * @return
     */
    @Override
    @AddCach
    public HashMap<String, Object> queryByTwoCategory(Integer page, Integer rows, String parentId) {
        HashMap<String, Object> map = new HashMap<>();
        //设置获取一级类别下二级类别条数
        CategoryExample example = new CategoryExample();
        example.createCriteria().andParentIdEqualTo(parentId);
        //获取总条数
        Integer records = categoryMapper.selectCountByExample(example);
        map.put("records", records);
        //获取总页数
        Integer total = records % rows == 0 ? records / rows : records / rows + 1;
        map.put("total", total);
        //当前页
        map.put("page", page);
        //  忽略几条  获取几条
        RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
        List<Category> categories = categoryMapper.selectByExampleAndRowBounds(example, rowBounds);
        map.put("rows", categories);

        return map;
    }

    /**
     * 添加一级类别
     *
     * @param category
     */
    @DelCach
    @AddLog("添加一级类别")
    @Override
    public void addOneCategory(Category category) {
        //获取uuid
        String uuid = UUID.randomUUID().toString();
        String id = uuid.replace("-", "");
        //设置id
        category.setId(id);
        //设置类别
        category.setLevels("1");
        categoryMapper.insert(category);
    }

    /**
     * 修改一级类别
     *
     * @param category
     */
    @DelCach
    @AddLog("修改一级类别")
    @Override
    public void updateOneCategory(Category category) {
        CategoryExample example = new CategoryExample();
        example.createCriteria().andIdEqualTo(category.getId());
        categoryMapper.updateByExampleSelective(category, example);
    }

    /**
     * 添加二级类别
     *
     * @param category
     */
    @DelCach
    @AddLog("添加二级类别")
    @Override
    public void addTwoCategory(Category category) {
        String uuid = UUID.randomUUID().toString();
        String id = uuid.replace("-", "");
        category.setId(id);
        category.setLevels("2");
        category.setParentId(category.getParentId());
        categoryMapper.insert(category);
    }

    /**
     * 修改二级类别
     *
     * @param category
     */
    @DelCach
    @AddLog("修改二级类别")
    @Override
    public void updateTwoCategory(Category category) {
        CategoryExample example = new CategoryExample();
        example.createCriteria().andIdEqualTo(category.getId());
        categoryMapper.updateByExampleSelective(category, example);
    }

    /**
     * 删除类别
     *
     * @param category
     * @return
     */
    @DelCach
    @AddLog("删除类别")
    @Override
    public HashMap<String, Object> deleteCategory(Category category) {
        HashMap<String, Object> map = new HashMap<>();
        //判断删除的是否是一级类别
        if ("1".equals(category.getLevels())) {
            CategoryExample example = new CategoryExample();
            example.createCriteria().andParentIdEqualTo(category.getId());
            Integer count = categoryMapper.selectCountByExample(example);
            if (count == 0) {
                //判断一级类别下是否有二级类别   没有  直接删除
                categoryMapper.delete(category);
                map.put("status", "200");
                map.put("message", "删除成功!");
            } else {
                map.put("status", "400");
                map.put("message", "删除失败,该类别下存在二级类别!");
            }
        } else {
            //判断该类别下是否存在视频

            //有 返回错误信息

            //没有直接删除
            categoryMapper.delete(category);
            map.put("status", "200");
            map.put("message", "删除成功!");
        }

        return map;
    }
}
