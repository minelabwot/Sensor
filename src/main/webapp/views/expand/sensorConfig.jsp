<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title></title>
</head>
<body>
<c:import url="/views/common/userInfoBar.jsp"></c:import>
<div class="page-body">
    <div>
        <h3>阈值配置:</h3>
        <form accept-charset="UTF-8" action="<%=basePath %>saveConfig.do" class="form-horizontal" id="addOperation" method="post">
            <table class="table-striped" id="add_table">
                <tr><td>类型</td>
                    <td>高位异常</td>
                    <td>低位异常</td>
                </tr>
                <c:forEach var="config" items="${configList}">
                    <tr>
                        <td><input class="form-control" name="${config.name}" type="text" value="${config.name}"/></td>
                        <td><input class="form-control" name="${config.name}_high" type="text" value="${config.high}"/></td>
                        <td><input class="form-control" name="${config.name}_low" type="text" value="${config.low}"/></td>
                    </tr>
                </c:forEach>
            </table>
            <div class="form-group">
                <label class="col-sm-4 control-label"></label>
                <div class="col-sm-8 col-xs-12"><p class="form-control-static"><input class="btn btn-primary" type="submit" value="提交" /></p></div>
            </div>
        </form>
    </div>
</div>


</body>
</html>
