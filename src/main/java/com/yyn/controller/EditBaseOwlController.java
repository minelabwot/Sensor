package com.yyn.controller;

import com.yyn.dao.OwlDao;
import com.yyn.model.MyOwl;
import com.yyn.model.SensorConfig;
import com.yyn.model.WotProperty;
import com.yyn.service.EditBaseOwlService;
import com.yyn.util.RDFReasoning;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by koala on 2017/5/16.
 */

@Controller
public class EditBaseOwlController {

    @Autowired
    EditBaseOwlService mService;

    @Autowired
    OwlDao mOwlDao;

    @RequestMapping("showBaseOwl.do")
    public String showBaseOwl(){
//        String baseOwl
        return "";
    }

    @RequestMapping("goDeviceAddPage.do")
    public String goPage(HttpServletRequest request){
        String id = request.getParameter("id");
        MyOwl myOwl = mOwlDao.searchDataById(id);
        System.out.println(myOwl.toString());
        String currentFile = myOwl.getRoot()+myOwl.getFile();
        String table = new File(currentFile).getName().replace(".","");
        request.getServletContext().setAttribute("currentFile",currentFile);
        WotProperty property = mService.getWotProperty(currentFile);


        property.setUri(myOwl.getUri());
        if (property == null){
            return "redirect:/listowl.do?noProperty=true";
        }
        property.setTableName(table);
        System.out.println(property.getMap().toString());
        request.getServletContext().setAttribute("wotProperty",property);
        return "expand/adddevice.jsp";
    }

    @RequestMapping("addCommonDevice.do")
    public String addDevice(HttpServletRequest request){
        String deviceType = request.getParameter("deviceType");
        WotProperty property = (WotProperty) request.getServletContext().getAttribute("wotProperty");
        List<String> propertyNames = property.getConcepts();
        String file = request.getServletContext().getAttribute("currentFile").toString();
        String sql = "insert into "+property.getTableName()+"(";
        sql = sql + "type, ";
        for (String str:propertyNames){
            sql = sql +str.toLowerCase()+", ";
        }
        sql = sql.substring(0,sql.length()-2)+") values (";
        sql = sql + "'" + deviceType + "', ";
        for (String str:propertyNames){
            sql = sql +"'"+request.getParameter(str)+"', ";
        }
        sql = sql.substring(0,sql.length()-2)+")";
        System.out.println(sql);
        mService.insertDevice(sql);
        int id = mService.getLastId(property.getTableName());
        String update = mService.pareSparql(file,property,request,id,property.getUri(),deviceType);
        System.out.println(update);
        String fileName = property.getFileName();
        String root = new File(fileName).getParent();
        System.out.println(root);
        Dataset ds = (Dataset) request.getServletContext().getAttribute("dataset");
        ds.begin(ReadWrite.WRITE);
        RDFReasoning.updateQuery(update,ds);
        ds.commit();
        ds.end();
        RDFReasoning.output(ds,new File(root,"output.owl").getAbsolutePath());
        return "expand/adddevice.jsp";
    }

    @RequestMapping("myDevice.do")
    public String goMyDevice(HttpServletRequest request){
        String currentFile = request.getParameter("owlfile");
        String tableName = new File(currentFile).getName().replace(".","");
        request.getServletContext().setAttribute("tableName",tableName);
        request.getServletContext().setAttribute("currentFile",currentFile);
        WotProperty property = mService.getWotProperty(currentFile);
        if (property == null){
            return "redirect:/listowl.do?noProperty=true";
        }
        List<Map<String,String>> sensorList = mService.selectAllDevice(tableName,"Sensor");
        System.out.println(sensorList);
        request.getServletContext().setAttribute("SensorMapList",sensorList);

        List<Map<String,String>> actuatorList = mService.selectAllDevice(tableName,"Actuator");
        request.getServletContext().setAttribute("ActuatorMapList",actuatorList);
        request.getServletContext().setAttribute("deviceNameList",property.getConcepts());
        return "expand/mydevicelist.jsp";
    }

    @RequestMapping("goSensorConfig.do")
    public String goSensorConfig(HttpServletRequest request){
        String currentFile = request.getParameter("owlfile");
        request.getServletContext().setAttribute("currentFile",currentFile);
        File root = new File(currentFile).getParentFile();
        List<String> sensorType = mService.getSensorType(currentFile);
        List<SensorConfig> configList = mService.getSensorConfig(sensorType,root);
        request.getServletContext().setAttribute("configList",configList);
        return "expand/sensorConfig.jsp?root="+currentFile;
    }
}
