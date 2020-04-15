<%@page pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<script>
    $(function () {
        //初始化一个表格
        $("#myTable").jqGrid({
            url: "${path}/user/queryAllUser",
            editurl: "${path}/user/insert",
            datatype: "json",              //返回      page:当前页   rows：数据(List）  total：总页数   records:总条数
            rowNum: 3,
            rowList: [3, 5, 10, 15, 20],
            pager: '#page',  //工具栏
            viewrecords: true,   //是否显示总条数
            styleUI: "Bootstrap",
            height: "auto",
            autowidth: true,
            multiselect: true,
            colNames: ['ID', '用户名', '手机号', '头像', '签名', '微信', '状态', "注册时间"],
            colModel: [
                {name: 'id', align: "center", width: 30},
                {name: 'username', editable: true, width: 80, align: "center"},
                {name: 'phone', editable: true, width: 80, align: "center"},
                {
                    name: 'headImg', editable: true, width: 100, align: "center", edittype: "file",
                    //参数;列的值,操作,行对象
                    formatter: function (cellvalue, options, rowObject) {
                        return "<img src='" + rowObject.headImg + "' width='100px' height='100px' />";
                    }
                },
                {name: 'sign', editable: true, width: 80, align: "right", align: "center"},
                {name: 'wechat', editable: true, width: 80, align: "center"},
                {
                    name: 'status', width: 80, align: "center",
                    //参数;列的值,操作,行对象
                    formatter: function (cellvalue, options, rowObject) {
                        if (cellvalue == 0) {  //正常  冻结  onclick=\"btn(\'" + rowObject.id + "\')\"

                            return "<button class='btn btn-success' onclick=\"btn(\'" + rowObject.id + "\')\">正常</button>";
                        } else {
                            return "<button class='btn btn-danger' onclick=\"btn(\'" + rowObject.id + "\')\">冻结</button>";
                        }

                    }
                },
                {name: 'createDate', width: 80, align: "center"}
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
                        url: "${path}/user/upload",
                        type: "post",
                        dataType: "JSON",
                        fileElementId: "headImg",    //上传的文件域的ID
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
                        url: "${path}/user/upload",
                        type: "post",
                        dataType: "JSON",
                        fileElementId: "headImg",    //上传的文件域的ID
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
            {}    //删除
        );
    });

    function btn(id) {
        $.ajax({
            url: "${path}/user/updateStatus",
            type: "POST",
            data: {id: id},
            dataType: "text",
            success: function (data) {
                //刷新表单
                $("#myTable").trigger('reloadGrid');
            }
        });
    }


    $("#code").click(function () {
        $.ajax({
            url: "${path}/user/phone",
            type: "post",
            data: $("#phone").serialize(),
            dataType: "json"
        })
    })
</script>
<div class="panel panel-danger">
    <!-- Default panel contents -->
    <div class="panel-heading">
        <h2>用户信息</h2>
    </div>
    <div class="panel-body">
        <div class="nav nav-tabs">
            <li class="active">
                <a href="#">用户管理</a>
            </li>
        </div>
        <div class="tab-content">
            <div role="tabpanel" class="tab-pane active" id="home"><br>
                <div class="form-group col-sm-4">
                    <a class="btn btn-success" role="button">导出用户信息</a>
                    <a class="btn btn-info" role="button">导入用户</a>
                    <a class="btn btn-warning" role="button">测试按钮</a>
                </div>
                <div class="col-sm-offset-7">
                    <div class="input-group">
                        <input id="phone" name="phone" type="text" class="form-control" placeholder="请输入手机号"
                               aria-describedby="basic-addon2">
                        <span class="input-group-addon" id="basic-addon2"><a id="code" role="button">发送验证码</a></span>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- Table -->
    <table id="myTable"></table>
    <div id="page"></div>
</div>