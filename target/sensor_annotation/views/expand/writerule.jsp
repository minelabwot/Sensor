<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
    <title>规则编辑界面</title>

    <form class="form-horizontal" action="<%=basePath%>saveRule.do">
        <textarea  name="rule" rows="20" cols="150">${prefix}</textarea>
        <br />
        <input class="btn btn-primary" value="保存" type="submit"></input>
    </form>


</head>
<body>

</body>
</html>
