<%@page pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<script>
    $(function () {
        //初始化一个表格
        $("#myTable").jqGrid({
            url: "${path}/log/queryAllLog",
            datatype: "json",              //返回      page:当前页   rows：数据(List）  total：总页数   records:总条数
            rowNum: 5,
            rowList: [5, 10, 15, 20],
            pager: '#page',  //工具栏
            viewrecords: true,   //是否显示总条数
            styleUI: "Bootstrap",
            height: "auto",
            autowidth: true,
            multiselect: true,
            colNames: ['ID', '管理员名称', '日期', '操作', '状态'],
            colModel: [
                {name: 'id'},
                {name: 'adminName'},
                {name: 'date'},
                {name: 'operation'},
                {name: 'status'},
            ]
        });


        //表格工具栏
        $("#myTable").jqGrid('navGrid', '#page',
            {edit: false, add: false, del: false},
            {},  //添加
            {}    //删除
        );
    });


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
    </div>
    <!-- Table -->
    <table id="myTable"></table>
    <div id="page"></div>
</div>