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
<%--   <input type="hidden" name="deviceType" value="${deviceType}"> --%>
  <%--<div class="form-group">--%>
    <%--<label class="col-sm-4 col-xs-12 control-label">设备名称(Name)</label>--%>
    <%--<div class="col-sm-8 col-xs-12"><input class="form-control" id="device_name" name="device[name]" type="text" /></div>--%>
  <%--</div>--%>

	<%--<div class="form-group">--%>
		<%--<label class="col-sm-4 col-xs-12 control-label">设备描述(Description)</label>--%>
		<%--<div class="col-sm-8 col-xs-12"><textarea class="form-control" id="device_description" name="device[description]">--%>
		<%--</textarea></div>--%>
	<%--</div>--%>

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
    

  <%--<div class="form-group">--%>
		    <%--<label class="col-sm-4 col-xs-12 control-label">观测属性(Property)</label>--%>
		    <%--<div class="col-sm-8 col-xs-12"><select class="form-control" id="device_property" name="device[property]">--%>
		    <%--<option></option>--%>
		    <%--<option value="Temperature">Temperature</option>--%>
		    <%--<option value="Cooling">Cooling</option>--%>
		    <%--<option value="Occupation">Occupation</option>--%>
		    <%--<option value="Humidity">Humidity</option>--%>
		    <%--<option value="Pressure">Pressure</option>--%>
		    <%--<option value="Wind_direction">Wind_direction</option>--%>
		    <%--<option value="Wind_speed">Wind_speed</option>--%>
		    <%--<option value="Carbon_Monoxide">Carbon Monoxide</option>--%>
		    <%--<option value="Nitrogen_Dioxide">Nitrogen_dioxide</option>--%>
		    <%--<option value="Sulfur_Dioxide">Sulfur_dioxide</option>--%>
		    <%--<option value="Oxygen">Oxygen</option>--%>
		    <%--<option value="Carbon_Dioxide">Carbon Dioxide</option>--%>
		    <%--<option value="Congestion">Congestion</option>--%>
		    <%--<option value="AirCleaner">AirCleaner</option>--%>
		    <%--<option value="PM">PM</option>--%>
		    <%--</select></div>--%>
	<%--</div>--%>
  <%--<div class="form-group">--%>
	    <%--<label class="col-sm-4 col-xs-12 control-label">测量单位(Unit)</label>--%>
	    <%--<div class="col-sm-8 col-xs-12"><input class="form-control" id="device_unit" name="device[unit]" type="text" /></div>--%>
	  <%--</div>--%>
	 <%--<div class="form-group">--%>
	    <%--<label class="col-sm-4 col-xs-12 control-label">所属机构(Owner)</label>--%>
	    <%--<div class="col-sm-8 col-xs-12"><input class="form-control" id="device_company" name="device[company]" type="text" /></div>--%>
    <%--</div>--%>
  <%--<div class="form-group">--%>
    <%--<label class="col-sm-4 col-xs-12 control-label">地区(Region)</label>--%>
    <%--<div class="col-sm-8 col-xs-12">--%>
      <%--<input class="form-control" id="device_region" name="device[region]" type="text" />--%>
    <%--</div>--%>
  <%--</div>--%>
  <%--<div class="form-group">--%>
    <%--<label class="col-sm-4 col-xs-12 control-label">场所(Spot)</label>--%>
    <%--<div class="col-sm-8 col-xs-12">--%>
      <%--<input class="form-control" id="device_spot" name="device[spot]" type="text" />--%>
    <%--</div>--%>
  <%--</div>--%>

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