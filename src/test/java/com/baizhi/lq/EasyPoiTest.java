package com.baizhi.lq;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baizhi.lq.entity.Photo;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author 李瓊
 * @Date 2020/4/8 21:16
 */

public class EasyPoiTest {
    @Test
    public void testExportPhoto() {

        //1.数据查询   List<Object>
        ArrayList<Photo> photos = new ArrayList<>();
        photos.add(new Photo("1", "C:\\Users\\12522\\Pictures\\Camera Roll\\2.jpg", 18, new Date()));
        photos.add(new Photo("2", "C:\\Users\\12522\\Pictures\\Camera Roll\\3.jpg", 56, new Date()));
        photos.add(new Photo("3", "C:\\Users\\12522\\Pictures\\Camera Roll\\4.jpg", 54, new Date()));
        photos.add(new Photo("4", "C:\\Users\\12522\\Pictures\\Camera Roll\\5.jpg", 21, new Date()));
        photos.add(new Photo("5", "C:\\Users\\12522\\Pictures\\Camera Roll\\6.jpg", 18, new Date()));

        //导出的参数   参数：标题,工作表名
        ExportParams exportParams = new ExportParams("应学App用户数据", "数据1");
        //配置工作表参数   参数：导出参数对象,要导出的对象,导出的数据集合
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, Photo.class, photos);

        try {
            //导出
            workbook.write(new FileOutputStream(new File("D://186-user.xls")));

            workbook.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testImportUsers() {
        try {
            ImportParams params = new ImportParams();
            params.setTitleRows(1);  //表格标题行数,默认0
            params.setHeadRows(1);  //表头所占行  表头行数,默认1

            FileInputStream fileInputStream = new FileInputStream(new File("D://186-user.xls"));
            List<Photo> result = ExcelImportUtil.importExcel(fileInputStream, Photo.class, params);

            for (int i = 0; i < result.size(); i++) {
                System.out.println(ReflectionToStringBuilder.toString(result.get(i)));
            }

            //Assert.assertTrue(result.size() == 4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
