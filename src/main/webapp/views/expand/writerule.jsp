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
</head>
<body>
<script>
    var index = 0;
    function addRules() {
        index++;
        var div = document.getElementById("rules");
        var text = document.createElement("textarea");
        text.name = "rule"+index;
        text.rows = 10;
        text.cols = 150;
        text.style.marginTop=30;
        div.appendChild(text);
    }
</script>
<form class="form-horizontal" action="<%=basePath%>saveRule.do" method="post">
    <div id="rules">
        <textarea  name="rule" rows="10" cols="150"></textarea>
    </div>
    <br />
    <input class="btn btn-primary" value="保存" type="submit"></input>
    <%--<input onclick="addRules()" class="btn btn-primary" value="添加规则" type="button"/>--%>
</form>

</body>
</html>
