package com.yyn.controller;

import com.yyn.model.WotProperty;
import com.yyn.service.EditBaseOwlService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by koala on 2017/5/16.
 */

@Controller
public class EditBaseOwlController {

    @Autowired
    EditBaseOwlService mService;

    @RequestMapping("showBaseOwl.do")
    public String showBaseOwl(){
//        String baseOwl
        return "";
    }

    @RequestMapping("goDeviceAddPage.do")
    public String goPage(HttpServletRequest request){
        String currentFile = request.getParameter("owlfile");
        String table = new File(currentFile).getName().replace(".","");
        request.getServletContext().setAttribute("currentFile",currentFile);
        WotProperty property = mService.getWotProperty(currentFile);
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
        WotProperty property = (WotProperty) request.getServletContext().getAttribute("wotProperty");
        List<String> propertyNames = property.getConcepts();
        String file = request.getServletContext().getAttribute("currentFile").toString();
        String sql = "insert into "+property.getTableName()+"(";
        for (String str:propertyNames){
            sql = sql +str.toLowerCase()+", ";
        }
        sql = sql.substring(0,sql.length()-2)+") values (";
        for (String str:propertyNames){
            sql = sql +"'"+request.getParameter(str)+"', ";
        }
        sql = sql.substring(0,sql.length()-2)+")";
        System.out.println(sql);
        mService.insertDevice(sql);
        mService.pareSparql(file,property,request);
        return "expand/adddevice.jsp";
    }

    @RequestMapping("myDevice.do")
    public String goMyDevice(HttpServletRequest request){
        String currentFile = request.getParameter("owlfile");
        String tableName = new File(currentFile).getName().replace(".","");
        WotProperty property = mService.getWotProperty(currentFile);
        if (property == null){
            return "redirect:/listowl.do?noProperty=true";
        }
        List<Map<String,String>> list = mService.selectAllDevice(tableName);
        request.getServletContext().setAttribute("deviceMapList",list);
        request.getServletContext().setAttribute("deviceNameList",property.getConcepts());
        System.out.println(property.getConcepts());
        return "expand/mydevicelist.jsp";
    }
}
