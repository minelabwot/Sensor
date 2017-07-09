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

    <h3>传感器Sensor</h3>
    <table class="table table-bordered">

        <tr>
            <td>设备名称</td>
            <td>删除设备</td>
            <td>设备详情</td>
        </tr>
        <c:forEach var="device" items="${SensorMapList}" varStatus="status">
            <tr>
                <td><c:out value="${device.name}" /></td>
                <td><a href="<%=basePath%>dynamic/deleteDetail.do?id=${device.id}">删除</a></td>
                <td><a href="<%=basePath%>dynamic/goSensorDetail.do?id=${device.id}">查看详情</a></td>
            </tr>
        </c:forEach>
    </table>

    <h3>执行器Actuator</h3>

    <table class="table table-bordered">


        <tr>
            <td>设备名称</td>
            <td>删除设备</td>
            <%--<td>设备描述</td>--%>
            <td>设备详情</td>
        </tr>
        <c:forEach var="device" items="${ActuatorMapList}" varStatus="status">
            <tr>
                <td><c:out value="${device.name}" /></td>
                <td><a href="<%=basePath%>dynamic/deleteDetail.do?id=${device.id}">删除</a></td>
                <td><a href="<%=basePath%>dynamic/goActuatorDetail.do?id=${device.id}">查看详情</a></td>
            </tr>
        </c:forEach>
    </table>
</div>


</body>
</html>