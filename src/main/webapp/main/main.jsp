<%@page pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <%--移动设备优先--%>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>应学后台管理系统</title>
    <link rel="icon" href="${path}/bootstrap/img/arrow-up.png" type="image/x-icon">
    <link rel="stylesheet" href="${path}/bootstrap/css/bootstrap.css">

    <%--引入jqgrid中主题css--%>
    <link rel="stylesheet" href="${path}/bootstrap/jqgrid/css/css/hot-sneaks/jquery-ui-1.8.16.custom.css">
    <link rel="stylesheet" href="${path}/bootstrap/jqgrid/boot/css/trirand/ui.jqgrid-bootstrap.css">
    <%--引入js文件--%>
    <script src="${path}/bootstrap/js/jquery.min.js"></script>
    <script src="${path}/bootstrap/js/bootstrap.js"></script>
    <script src="${path}/bootstrap/jqgrid/js/i18n/grid.locale-cn.js"></script>
    <script src="${path}/bootstrap/jqgrid/boot/js/trirand/jquery.jqGrid.min.js"></script>
    <script src="${path}/bootstrap/js/ajaxfileupload.js"></script>
    <style>
        .navbar {
            min-height: 25px;
        }
    </style>

</head>
<body>
<!--顶部导航-->
<nav class="navbar navbar-inverse">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                    data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand">应学视频App后台管理系统</a>
        </div>
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav navbar-right">
                <li><p class="navbar-text">欢迎：<span class="text-danger">${admin.name}</span></p></li>
                <li><a href="${path}/admin/loginOut">退出&nbsp<span class="glyphicon glyphicon-log-in"></span></a></li>
            </ul>
        </div>
    </div>
</nav>
<!--栅格系统-->
<div class="container-fluid">
    <div class="col-sm-2 text-center">
        <!--左边手风琴部分-->
        <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
            <div class="panel panel-danger">
                <div class="panel-heading" role="tab" id="headingOne">
                    <h4 class="panel-title">
                        <a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseOne"
                           aria-expanded="true" aria-controls="collapseOne" class="text-center">
                            用户管理
                        </a>
                    </h4>
                </div>
                <div id="collapseOne" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingOne">
                    <div class="panel-body">
                        <div class="col-sm-10 col-sm-offset-1">
                            <a class="btn btn-danger center-block " role="button"
                               href="javascript:$('#MainId').load('${path}/user/user.jsp')">用户展示</a>
                            <br>
                            <a class="btn btn-danger center-block " role="button">用户统计</a>
                            <br>
                            <a class="btn btn-danger center-block " role="button">用户分布</a>
                        </div>
                    </div>
                </div>
            </div>
            <hr>
            <div class="panel panel-success">
                <div class="panel-heading" role="tab" id="headingTwo">
                    <h4 class="panel-title">
                        <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion"
                           href="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
                            分类管理
                        </a>
                    </h4>
                </div>
                <div id="collapseTwo" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingTwo">
                    <div class="panel-body">
                        <div class="col-sm-10 col-sm-offset-1">
                            <a class="btn btn-success center-block " role="button"
                               href="javascript:$('#MainId').load('${path}/category/category.jsp')">分类展示</a>
                        </div>
                    </div>
                </div>
            </div>
            <hr>
            <div class="panel panel-warning">
                <div class="panel-heading" role="tab" id="headingThree">
                    <h4 class="panel-title">
                        <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion"
                           href="#collapseThree" aria-expanded="false" aria-controls="collapseThree">
                            视频管理
                        </a>
                    </h4>
                </div>
                <div id="collapseThree" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingThree">
                    <div class="panel-body">
                        <div class="col-sm-10 col-sm-offset-1">
                            <a class="btn btn-warning center-block " role="button"
                               href="javascript:$('#MainId').load('${path}/video/video.jsp')">视频展示</a>
                            <br>
                            <a class="btn btn-warning center-block " role="button"
                               href="javascript:$('#MainId').load('${path}/video/videoES.jsp')">视频搜索</a>
                        </div>
                    </div>
                </div>
            </div>
            <hr>
            <div class="panel panel-info">
                <div class="panel-heading" role="tab" id="headingFour">
                    <h4 class="panel-title">
                        <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion"
                           href="#collapseFour" aria-expanded="false" aria-controls="collapseFour">
                            日志管理
                        </a>
                    </h4>
                </div>
                <div id="collapseFour" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingFour">
                    <div class="panel-body">
                        <a class="btn btn-warning center-block " role="button"
                           href="javascript:$('#MainId').load('${path}/log/log.jsp')">日志展示</a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!--右边部分-->
    <div class="col-md-10" id="MainId">
        <!--巨幕开始-->
        <div class="jumbotron">
            <h1>欢迎来到应学App后台管理系统</h1>
        </div>

        <!--右边轮播图部分-->
        <div id="carousel-example-generic" class="carousel slide" data-ride="carousel" align="center">
            <!-- 中间按钮 -->
            <ol class="carousel-indicators">
                <li data-target="#carousel-example-generic" data-slide-to="0" class="active"></li>
                <li data-target="#carousel-example-generic" data-slide-to="1"></li>
                <li data-target="#carousel-example-generic" data-slide-to="2"></li>
                <li data-target="#carousel-example-generic" data-slide-to="3"></li>
            </ol>

            <!-- 图片 -->
            <div class="carousel-inner" role="listbox">
                <div class="item active">
                    <img src="${path}/bootstrap/img/pic1.jpg" alt="">
                    <div class="carousel-caption"></div>
                </div>
                <div class="item">
                    <img src="${path}/bootstrap/img/pic2.jpg" alt="">
                    <div class="carousel-caption"></div>
                </div>
                <div class="item">
                    <img src="${path}/bootstrap/img/pic3.jpg" alt="">
                    <div class="carousel-caption"></div>
                </div>
                <div class="item">
                    <img src="${path}/bootstrap/img/pic4.jpg" alt="">
                    <div class="carousel-caption"></div>
                </div>
            </div>

            <!-- 前一页后一页按钮 -->
            <a class="left carousel-control" href="#carousel-example-generic" role="button" data-slide="prev">
                <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
                <span class="sr-only">Previous</span>
            </a>
            <a class="right carousel-control" href="#carousel-example-generic" role="button" data-slide="next">
                <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
                <span class="sr-only">Next</span>
            </a>
        </div>


        <!--页脚-->
        <nav class="navbar navbar-default navbar-fixed-bottom">
            <div class="container text-center">
                @百知教育 liqiong@qq.com
            </div>
        </nav>
        <!--栅格系统-->

    </div>
</body>
</html>
