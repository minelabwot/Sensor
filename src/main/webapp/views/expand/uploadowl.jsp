<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css">
.float-left{
float:left;
text-align: center;
}
.bg1 {
	background-color:#efefef;
	padding: 20px;
	margin: 20px;
}
.bg2 {
	background-color:#efeff3;
	padding: 20px;
	margin: 20px;
}
</style>
</head>
<body>
<c:import url="/views/common/userInfoBar.jsp"></c:import>

<div class="page-body">
	<h1>这是添加本体</h1>
	<form action="<%=basePath%>owlupdate.do" method="post"
		enctype="multipart/form-data">
		<table>
			<tr>
				<td><input type="file" name="file" /></td>
			</tr>
			<tr>
				<td>本体名称：<input type="text" name="name" /></td>
			</tr>
			<tr>
				<td>本体描述<input type="text" name="description" /></td>
			</tr>

			<tr>
				<td>本体URI<input type="text" name="uri" /></td>
			</tr>
			<tr>
				<td><input type="submit" value="提交" /></td>
			</tr>
		</table>
	</form>
</div>
<c:import url="/views/common/importJs.jsp"></c:import>
</body>
</html>