package com.baizhi.lq.controller;

import com.baizhi.lq.entity.Category;
import com.baizhi.lq.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @Author 李瓊
 * @Date 2020/3/30 20:05
 */
@Controller
@RequestMapping("category")
public class CategoryController {
    @Resource
    CategoryService categoryService;

    /**
     * 查询一级类别
     *
     * @param page
     * @param rows
     * @return
     */
    @ResponseBody
    @RequestMapping("queryByOneCategory")
    public HashMap<String, Object> queryByOneCategory(Integer page, Integer rows) {
        HashMap<String, Object> map = categoryService.queryByOneCategory(page, rows);
        return map;
    }

    /**
     * 查询二级类别
     *
     * @param page
     * @param rows
     * @param parentId
     * @return
     */
    @ResponseBody
    @RequestMapping("queryByTwoCategory")
    public HashMap<String, Object> queryByTwoCategory(Integer page, Integer rows, String parentId) {
        HashMap<String, Object> map = categoryService.queryByTwoCategory(page, rows, parentId);
        return map;
    }

    /**
     * 用户添加
     *
     * @param category
     * @param oper
     * @param id
     */
    @ResponseBody
    @RequestMapping("insert")
    public Object insert(Category category, String oper, String[] id) {
        if (oper.equals("add")) {
            //判断添加的是否是一级类别
            if ("1".equals(category.getLevels())) {
                categoryService.addOneCategory(category);
            } else {
                categoryService.addTwoCategory(category);
            }

        }
        if (oper.equals("edit")) {
            //判断修改的是否是一级类别
            if ("1".equals(category.getLevels())) {
                categoryService.updateOneCategory(category);
            } else {
                categoryService.updateTwoCategory(category);
            }
        }

        if (oper.equals("del")) {
            //判断删除的是否是一级类别
            if ("1".equals(category.getLevels())) {
                HashMap<String, Object> map = categoryService.deleteCategory(category);
                return map;
            } else {
                HashMap<String, Object> map = categoryService.deleteCategory(category);
                return map;
            }
        }
        return null;
    }
}
