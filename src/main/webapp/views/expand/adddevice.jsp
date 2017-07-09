<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=basePath%>assets/css/deviceEdit.css">
</head>
<body>
<c:import url="/views/common/userInfoBar.jsp"></c:import>


<div class="container">
<br/><br/><br/><br/>
<div class="row">
	<div class="col-xs-12 col-sm-6">
		<div class="col-pad">
			<h1>新建设备</h1>

<form accept-charset="UTF-8" action="<%=basePath %>addCommonDevice.do" class="form-horizontal" id="device_form" method="post">
  <h3>选择设备类型</h3>
 	<div class="form-group"><select class="form-control" name="deviceType">
		<option value="Sensor">Sensor</option>
		<option value="Actuator">Actuator</option>
	</select></div>

    <c:set var="map" value="${wotProperty.map}"/>
    <c:forEach var="property" items="${wotProperty.concepts}">
        <div class="form-group">
            <label class="col-sm-4 col-xs-12 control-label">${property}</label>
            <div class="col-sm-8 col-xs-12">

                <c:choose>
                    <c:when test="${wotProperty.isselect[property]}">
                        <select class="form-control" id="device_property" name="${property}">
                            <c:forEach var="option" items="${map[property]}">
                                <option value="${option}">${option}</option>
                            </c:forEach>
                        </select>

                    </c:when>
                    <c:otherwise>
                        <input class="form-control" id="device_type" name="${property}" type="text" />
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </c:forEach>
  <div class="form-group">
    <label class="col-sm-4 control-label"></label>
    <div class="col-sm-8 col-xs-12"><p class="form-control-static"><input class="btn btn-primary" id="device_form_submit" name="commit" type="submit" value="保存设备" /></p></div>
  </div>

</form>

    </div>

    <br><br><br><br>

  </div>
</div>

</div>

<!-- <script> -->
<c:import url="/views/common/importJs.jsp"></c:import>
<!-- </script> -->

</body>
</html>