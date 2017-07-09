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
        body .label {
            font-size: 100%;
        }
        body .btn {
            font-size: 20px;
        }
    </style>
</head>
<body>
<c:import url="/views/common/userInfoBar.jsp"></c:import>


<div class="container">
    <br/><br/><br/><br/>
    <div class="row">
        <div class="col-xs-12 col-sm-6">
            <div class="col-pad">
                <h1>设备详细信息</h1>


                <form accept-charset="UTF-8" class="form-horizontal" method="post">
                    <div class="form-group">
                        <label class="col-sm-4 col-xs-12 control-label">id</label>
                        <div class="col-sm-8 col-xs-12"><label class="control-label"><c:out value="${deviceId}"/></label></div>
                    </div>

                    <c:forEach items="${deviceData[0]}" var="device">
                        <div class="form-group">
                            <label class="col-sm-4 col-xs-12 control-label">${device.key}</label>
                            <div class="col-sm-8 col-xs-12"><label class="control-label"><c:out value="${device.value}"/></label></div>
                        </div>
                    </c:forEach>

                </form>

            </div>

            <br><br><br><br>

        </div>

        <div id="sidebar" class="col-xs-12 col-sm-6">


        </div>

    </div>
    <div>
        <h3>构造传感器数据:</h3>
        <form accept-charset="UTF-8" action="<%=basePath %>sensor_data_update.do" class="form-horizontal" id="device_form" method="post">
            <div class="form-group">
                <label class="col-sm-4 col-xs-12 control-label">设备ID</label>
                <div class="col-sm-8 col-xs-12"> <input class="form-control" name="id" type="text" /></div>
            </div>
            <div class="form-group">
                <label class="col-sm-4 col-xs-12 control-label">当前观测值</label>
                <div class="col-sm-8 col-xs-12"> <input class="form-control" name="value" type="text" /></div>
            </div>
            <div class="form-group">
                <label class="col-sm-4 control-label"></label>
                <div class="col-sm-8 col-xs-12"><p class="form-control-static"><input class="btn btn-primary" type="submit" value="产生数据" /></p></div>
            </div>
        </form>
    </div>
</div>



<c:import url="/views/common/importJs.jsp"/>
</body>
</html>