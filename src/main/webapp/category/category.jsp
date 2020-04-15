<%@page pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<script>
    $(function () {
        pageInit();
    });

    function pageInit() {
        $("#myTable").jqGrid(
            {
                url: "${path}/category/queryByOneCategory",
                editurl: "${path}/category/insert?levels=1",
                datatype: "json",
                styleUI: "Bootstrap",
                type: "POST",
                height: "auto",
                autowidth: true,
                rowNum: 3,
                rowList: [3, 5, 10, 15],
                pager: '#page',
                viewrecords: true,
                colNames: ['ID', '名称', '级别'],
                colModel: [
                    {name: 'id', index: 'id', width: 55},
                    {name: 'cateName', editable: true, index: 'invdate', width: 90},
                    {name: 'levels', index: 'name', width: 100},
                ],
                subGrid: true, //是否开启子表格
                //subgridId  点击一行时会在父表格中创建一个div  用来容纳子表格  subgridId就是div  的id
                //rowId  点击行的id
                subGridRowExpanded: function (subgridId, rowId) {
                    addSubGrid(subgridId, rowId);
                },
            });
        $("#myTable").jqGrid('navGrid', '#page', {edit: true, add: true, del: true, search: false},
            {closeAfterEdit: true},
            {closeAfterAdd: true},
            {
                closeAfterdel: true,  //关闭对话框
                //提交之后的操作
                afterSubmit: function (response) {
                    //向警告框中写入内容
                    $("#showMsg").html(response.responseJSON.message);
                    //展示警告框
                    $("#deleteMsg").show();

                    //自动关闭
                    setTimeout(function () {
                        //关闭提示框
                        $("#deleteMsg").hide();
                    }, 3000);

                    return "ok";
                }
            }
        );

        function addSubGrid(subgridId, rowId) {
            var subgridTableId = subgridId + "Table";  //定义子表格 Table的id
            var pagerId = subgridId + "Page";   //定义子表格工具栏id

            //在子表格容器中创建子表格和子表格分页工具栏
            $("#" + subgridId).html(
                "<table id='" + subgridTableId + "'></table>" +
                "<div id='" + pagerId + "'></div>");
            //子表格
            $("#" + subgridTableId).jqGrid(
                {
                    url: "${path}/category/queryByTwoCategory?parentId=" + rowId,
                    editurl: "${path}/category/insert?parentId=" + rowId + "&levels=2",
                    datatype: "json",
                    type: "POST",
                    pager: "#" + pagerId,
                    styleUI: "Bootstrap",
                    height: 'auto',
                    autowidth: true,
                    rowNum: 3,
                    rowList: [3, 5, 10, 15],
                    viewrecords: true,
                    colNames: ['ID', '名称', '类别', '上级类别ID'],
                    colModel: [
                        {name: "id", index: "num", width: 80, key: true},
                        {name: "cateName", editable: true, index: "item", width: 130},
                        {name: "levels", index: "qty", width: 70, align: "right"},
                        {name: "parentId", index: "unit", width: 70, align: "right"},
                    ],
                });
            $("#" + subgridTableId).jqGrid('navGrid', "#" + pagerId, {
                    edit: true, add: true, del: true, search: false
                },
                {closeAfterEdit: true},
                {closeAfterAdd: true},
                {
                    closeAfterdel: true,  //关闭对话框
                    //提交之后的操作
                    afterSubmit: function (response) {
                        //向警告框中写入内容
                        $("#showMsg").html(response.responseJSON.message);
                        //展示警告框
                        $("#deleteMsg").show();

                        //自动关闭
                        setTimeout(function () {
                            //关闭提示框
                            $("#deleteMsg").hide();
                        }, 3000);

                        return "ok";
                    }
                }
            );
        }
    }
</script>
<%--面板头--%>
<div class="panel panel-success">
    <div class="panel panel-heading">
        <h2>类别信息</h2>
    </div>

    <%--标签页--%>
    <div class="panel-body">
        <div class="nav nav-tabs">
            <li class="active">
                <a href="#">用户管理</a>
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