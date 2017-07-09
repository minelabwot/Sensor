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
    <script>
        function addOperation() {
            var table = document.getElementById("add_table");
            var tr = document.createElement("tr");
            table.appendChild(tr);
            var columns = ["action_name[]","action_urlTemplate[]","action_messageContent[]","action_lifecycle[]"];
            for(var col in columns) {
                var td = document.createElement("td");
                tr.appendChild(td);
                var input = document.createElement("input");
                input.name = columns[col];
                input.type = "text";
                input.class = "form-control";
                td.appendChild(input);
            }
            var td = document.createElement("td");
            tr.appendChild(td);
            var select = document.createElement("select");
            select.name = "action_effect[]";
            td.appendChild(select);
            var option1 = document.createElement("option");
            var option2 = document.createElement("option");
            option1.value = "increment";
            option1.innerHTML = "increment";
            option2.value = "decrement";
            option2.innerHTML = "decrement";
            select.appendChild(option1);
            select.appendChild(option2);
        }
    </script>
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
        <h3>设备服务:</h3>
        <form accept-charset="UTF-8" action="<%=basePath %>deviceActionAdd.do" class="form-horizontal" id="addOperation" method="post">
            <table class="table-striped" id="add_table">
                <tr><td>设备Id</td><td>服务名称</td><td>服务地址</td><td>参数(多个参数用,分割)</td><td>工作状态</td><td>调用结果</td></tr>
                <tr>
                    <td><input class="form-control" name="deviceId" type="text" /></td>
                    <td><input class="form-control" name="action_name[]" type="text" /></td>
                    <td><input class="form-control" name="action_urlTemplate[]" type="text" /></td>
                    <td><input class="form-control" name="action_messageContent[]" type="text" /></td>
                    <td><input class="form-control" name="action_lifecycle[]" type="text" /></td>
                    <td><select name="action_effect[]">
                        <option value ="increment">increment</option>
                        <option value ="decrement">decrement</option>
                    </select></td>
                </tr>

            </table>
            <input type="button" value="新建服务" onclick="addOperation()"/>
            <div class="form-group">
                <label class="col-sm-4 control-label"></label>
                <div class="col-sm-8 col-xs-12"><p class="form-control-static"><input class="btn btn-primary" type="submit" value="提交服务" /></p></div>
            </div>
        </form>
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