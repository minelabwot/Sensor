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
<c:if test="${noProperty}">
	out.println("<script>function showMessage() {alert('没有继承WoTProperty的概念');}</script>");
	out.println("<script>showMessage()</script>");//调用showMessage()方法
</c:if>
<div class="page-body">
	<%--<h1 align="center">基础本体</h1>--%>
	<%--<table class="table table-bordered">--%>
		<%--<tr>--%>
			<%--<td>基础本体</td>--%>
			<%--<td><c:out value="wot.owl" /></td>--%>
			<%--<td><a href="<%=basePath%>showBaseOwl.do?baseOwl=${baseOwl}">编辑</a></td>--%>
		<%--</tr>--%>
	<%--</table>--%>
	<h1 align="center">添加本体</h1>
	<table class="table table-bordered">
		<c:forEach var="owl" items="${owllist}" varStatus="status">
			<tr>
				<td><c:out value="${owl.name}" /></td>
				<td><c:out value="${owl.description}" /></td>
				<td><a href="<%=basePath%>deleteOwl.do?id=${owl.id}">删除</a></td>
				<td><a href="<%=basePath%>editOwl.do?owlfile=${owl.root}${owl.file}">本体展示</a></td>
				<td><a href="<%=basePath%>expandOwl.do?owlfile=${owl.root}${owl.file}">拓展</a></td>
				<td><a href="<%=basePath%>goDeviceAddPage.do?id=${owl.id}">添加设备</a></td>
				<td><a href="<%=basePath%>myDevice.do?owlfile=${owl.root}${owl.file}">我的设备</a></td>
				<td><a href="<%=basePath%>goWriteRule.do?id=${owl.id}">配置规则</a> </td>
				<td><a href="<%=basePath%>goSensorConfig.do?owlfile=${owl.root}${owl.file}">传感器阈值配置</a> </td>
			</tr>
		</c:forEach>
	</table>
</div>

</body>
</html>