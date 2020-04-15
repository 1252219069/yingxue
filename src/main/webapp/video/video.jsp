<%@page pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<script>
    $(function () {
        //初始化一个表格
        $("#myTable").jqGrid({
            url: "${path}/video/videoPage",
            editurl: "${path}/video/insert",
            datatype: "json",              //返回      page:当前页   rows：数据(List）  total：总页数   records:总条数
            rowNum: 3,
            rowList: [3, 5, 10, 15, 20],
            pager: '#page',  //工具栏
            viewrecords: true,   //是否显示总条数
            styleUI: "Bootstrap",
            height: "auto",
            autowidth: true,
            multiselect: true,
            colNames: ['ID', '名称', '视频', '上传时间', '描述', '所属类别', '类别ID', '用户ID', '分组ID'],
            colModel: [
                {name: 'id', align: "center", width: 30},
                {name: 'title', editable: true, width: 60, align: "center"},
                {
                    name: 'path', editable: true, width: 150, align: "center", edittype: "file",
                    //参数;列的值,操作,行对象
                    formatter: function (cellvalue, options, rowObject) {
                        return "<a href='" + cellvalue + "'><video id='media' src='" + cellvalue + "' width='150px' height='100px' controls poster='" + rowObject.cover + "'/></a>";
                    }
                },
                {name: 'publishDate', width: 80, align: "center"},
                {name: 'brief', editable: true, width: 80, align: "right", align: "center"},
                {name: 'categoryName', width: 60, align: "center"},
                {name: 'categoryId', width: 60, align: "center"},
                {name: 'userId', width: 60, align: "center"},
                {name: 'groupId', width: 60, align: "center"},
            ]
        });


        //表格工具栏
        $("#myTable").jqGrid('navGrid', '#page',
            {edit: true, add: true, del: true, addtext: "添加", edittext: "修改", deltext: "删除"},
            {
                closeAfterEdit: true,//关闭对话框
                //文件上传
                afterSubmit: function (response) {
                    //数据的入库
                    //文件没有上传
                    // 图片路径不对
                    //在提交之后做文件上传   本地
                    //fileElementId　　　　　  需要上传的文件域的ID，即<input type="file">的ID。
                    //修改图片路径     id,图片路径
                    //异步文件上传
                    $.ajaxFileUpload({
                        //文件上传
                        url: "${path}/video/uploadVdieo",
                        type: "post",
                        dataType: "JSON",
                        fileElementId: "path",    //上传的文件域的ID
                        data: {id: response.responseText},
                        success: function () {
                            //刷新表单  .jqGrid().trigger("reloadGrid");
                            $("#myTable").trigger('reloadGrid');
                        }
                    });
                    //必须有返回值
                    return "guanbi";
                }
            },   //修改之后的额外操作
            {
                closeAfterAdd: true,  //关闭对话框
                //文件上传
                afterSubmit: function (response) {
                    //数据的入库
                    //文件没有上传
                    // 图片路径不对
                    //在提交之后做文件上传   本地
                    //fileElementId　　　　　  需要上传的文件域的ID，即<input type="file">的ID。
                    //修改图片路径     id,图片路径
                    //异步文件上传
                    $.ajaxFileUpload({
                        //文件上传
                        url: "${path}/video/uploadVdieo",
                        type: "post",
                        dataType: "JSON",
                        fileElementId: "path",    //上传的文件域的ID
                        data: {id: response.responseText},
                        success: function () {
                            //刷新表单  .jqGrid().trigger("reloadGrid");
                            $("#myTable").trigger('reloadGrid');
                        }
                    });
                    //必须有返回值
                    return "guanbi";
                }
            },   //添加
            {
                afterSubmit: function (data) {
                    //向警告框中追加错误信息
                    $("#showMsg").html(data.responseJSON.message);
                    //展示警告框
                    $("#deleteMsg").show();

                    //自动关闭
                    setTimeout(function () {
                        $("#deleteMsg").hide();
                    }, 3000);
                    return "hello";
                }
            }    //删除
        );
    });
</script>
<%--面板头--%>
<div class="panel panel-success">
    <div class="panel panel-heading">
        <h2>视频信息</h2>
    </div>

    <%--标签页--%>
    <div class="panel-body">
        <div class="nav nav-tabs">
            <li class="active">
                <a href="#">视频管理</a>
            </li>
        </div>
    </div>

    <%--警告框--%>
    <div id="deleteMsg" class="alert alert-warning alert-dismissible" role="alert" style="width: 300px;display: none">
        <span id="showMsg"/>
    </div>

    <%--初始化表单--%>
    <table id="myTable"></table>

    <%--分页工具栏--%>
    <div id="page"></div>
</div>