package com.baizhi.lq.service;

import com.baizhi.lq.entity.Category;

import java.util.HashMap;

/**
 * @Author 李瓊
 * @Date 2020/3/30 19:27
 */
public interface CategoryService {
    /**
     * =
     * 分页展示一级类别
     *
     * @param page
     * @param rows
     * @return
     */
    HashMap<String, Object> queryByOneCategory(Integer page, Integer rows);

    /**
     * 分页展示二级类别
     *
     * @param page
     * @param rows
     * @param parentId
     * @return
     */
    HashMap<String, Object> queryByTwoCategory(Integer page, Integer rows, String parentId);

    /**
     * 添加一级类别
     *
     * @param category
     */
    void addOneCategory(Category category);

    /**
     * 修改一级类别
     *
     * @param category
     */
    void updateOneCategory(Category category);

    /**
     * 添加二级类别
     *
     * @param category
     */
    void addTwoCategory(Category category);

    /**
     * 修改二级类别
     *
     * @param category
     */
    void updateTwoCategory(Category category);

    /**
     * 删除类别
     *
     * @param category
     */
    HashMap<String, Object> deleteCategory(Category category);
}
