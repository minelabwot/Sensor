package com.yyn.controller;

import com.yyn.dao.BaseDeviceDao;
import com.yyn.util.NameSpaceConstants;
import com.yyn.util.RDFReasoning;
import com.yyn.util.ResolveRule;

import org.apache.jena.atlas.lib.StrUtils;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by koala on 2017/6/12.
 */

@Controller
@RequestMapping("/dynamic/")
public class DynamicDeviceController {

    @Autowired
    BaseDeviceDao mBaseDeviceDao;
    @RequestMapping("goSensorDetail.do")
    public String goSensorDetail(HttpServletRequest request){

        String id = request.getParameter("id");
        String talbeName = request.getServletContext().getAttribute("tableName").toString();
        List<Map<String,String>> data = mBaseDeviceDao.getDeviceByTableId(talbeName,Integer.valueOf(id));
        System.out.println(data);
        request.getServletContext().setAttribute("deviceData",data);
        request.getServletContext().setAttribute("deviceId",id);
        for (Map map:data){
            map.remove("id");
        }
        System.out.println(data);
        return "expand/sensorDetail.jsp";
    }

    @RequestMapping("goActuatorDetail.do")
    public String goActuatorDetail(HttpServletRequest request){
        String id = request.getParameter("id");
        String talbeName = request.getServletContext().getAttribute("tableName").toString();
        List<Map<String,String>> data = mBaseDeviceDao.getDeviceByTableId(talbeName,Integer.valueOf(id));
        request.getServletContext().setAttribute("deviceData",data);
        return "expand/actuatorDetail.jsp";
    }

    @RequestMapping("deleteDetail.do")
    public String deleteDetail(HttpServletRequest request){
        String id = request.getParameter("id");
        String talbeName = request.getServletContext().getAttribute("tableName").toString();
        Map<String,String> map = mBaseDeviceDao.getDeviceByTableId(talbeName,Integer.valueOf(id)).get(0);
        mBaseDeviceDao.delete(id,talbeName);

        String file = request.getServletContext().getAttribute("currentFile").toString();


        String prefix = ResolveRule.getSprqlPre(file);
        String type = map.get("type");
        if ("Sensor".equals(type)){
            type = "ssn:"+type;
        }else {
            type = "san:"+type;
        }

        String sparql = StrUtils.strjoinNL(
                NameSpaceConstants.PREFIX,
                "DELETE { GRAPH wot:sensor_annotation { ?device rdf:type "+type+" } }",
                "USING wot:sensor_annotation ",
                "WHERE{ ?device wot:deviceID '"+id+"'^^xsd:string }"
        );
        System.out.println(sparql);
        Dataset dataset = (Dataset) request.getServletContext().getAttribute("dataset");
        dataset.begin(ReadWrite.WRITE);
        RDFReasoning.updateQuery(sparql,dataset);
        dataset.commit();
        dataset.end();
        RDFReasoning.output(dataset);
        return "redirect:/myDevice.do?owlfile="+file;
    }
}
