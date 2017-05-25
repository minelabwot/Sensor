<%@ page import="com.yyn.model.Owldata" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>本体展示</title>
</head>
<body>
<c:import url="/views/common/userInfoBar.jsp"></c:import>

<h1>本体前缀</h1>
<table class="table table-bordered">
    <c:forEach var="prefix" items="${owldata.prefixs}">
        <tr>
            <td><c:out value="${prefix.key}" /></td>
            <td><c:out value="${prefix.value}" /></td>
        </tr>
    </c:forEach>
</table>


<h1>本体概念</h1>
<div class="page-body">
    <ol>
        <c:forEach var="concept" items="${owldata.concepts}">
            <li>
                <tr>
                    <td><c:out value="${concept}" /> </td>
                    <%--<td><a href="<%=basePath%>deleteOwl.do?owl=${owldata.nameUriMap[concept]}">删除</a></td>--%>
                    <%--<td><a href="<%=basePath%>editOwl.do?owl=${owldata.nameUriMap[concept]}">编辑</a></td>--%>
                    <%--<td><a href="<%=basePath%>addInstance.do?instance=${owldata.nameUriMap[concept]}">添加实例</a></td>--%>
                </tr>
            </li>
            <ol>
                <c:forEach var="instance" items="${owldata.map[concept]}">
                    <li>
                        <tr>
                            <td><c:out value="${instance}" /></td>
                            <%--<td><a href="<%=basePath%>deleteInstance.do?instance=${owldata.nameUriMap[instance]}&concept=${concept}">删除</a></td>--%>
                        </tr>
                    </li>
                </c:forEach>
            </ol>
        </c:forEach>
    </ol>
</div>

</body>
</html>
