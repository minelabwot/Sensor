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
    <h1 align="center">我的设备</h1>
    <table class="table table-bordered">

        <c:forEach var="device" items="${deviceMapList}" varStatus="status">
            <c:if test="${status.index  == 0}">
                <tr>
                    <c:forEach items="${device}" var="property">
                        <td><c:out value="${property.key}" /></td>
                    </c:forEach>
                </tr>
            </c:if>
            <tr>
                <c:forEach items="${device}" var="property">
                    <td><c:out value="${property.value}" /></td>
                </c:forEach>
            </tr>
        </c:forEach>
    </table>
</div>


</body>
</html>