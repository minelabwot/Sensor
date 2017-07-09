package com.yyn.controller;

import com.yyn.service.EditBaseOwlService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by koala on 2017/6/19.
 */

@Controller
public class ConfigController {

    @Autowired
    EditBaseOwlService mEditBaseOwlService;

    @RequestMapping("saveConfig.do")
    public String saveConfig(HttpServletRequest request){
        String currentFile = request.getServletContext().getAttribute("currentFile").toString();
        List<String> typeList = mEditBaseOwlService.getSensorType(currentFile);
        File root = new File(currentFile).getParentFile();
        File configFile = new File(root,"config.properties");
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(configFile));
            for (String name:typeList){
                System.out.println(name);
                String high = request.getParameter(name+"_high").toString();
                String low = request.getParameter(name+"_low").toString();
                properties.put(name+"_high",high);
                properties.put(name+"_low",low);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        OutputStreamWriter outputStream = null;
        try {
            outputStream = new OutputStreamWriter(new FileOutputStream(configFile));
            properties.store(outputStream, null);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "redirect:/goSensorConfig.do?owlfile="+currentFile;
    }
}
