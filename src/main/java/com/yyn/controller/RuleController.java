package com.yyn.controller;

import com.yyn.dao.OwlDao;
import com.yyn.model.MyOwl;
import com.yyn.service.RuleService;
import com.yyn.util.ResolveRule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by koala on 2017/5/23.
 */
@Controller
public class RuleController {

    @Autowired
    RuleService mService;

    @Autowired
    OwlDao mOwlDao;

    @RequestMapping("goWriteRule.do")
    public String goWriteRule(HttpServletRequest request){
        String id = request.getParameter("id");
        MyOwl myOwl = mOwlDao.searchDataById(id);
        String file = myOwl.getRoot()+myOwl.getFile();
        String prefix = ResolveRule.getSprqlPre(file);
        String rulePre = ResolveRule.getRulePre(file);
        String rule = myOwl.getRule();
        List<String> ruleList = new ArrayList<>();
        if (rule != null){

            String[] rules = rule.split("000000");
            for (String str:rules){
                ruleList.add(str);
            }
        }

        System.out.println(rulePre);

        request.getServletContext().setAttribute("currentId",id);
        request.getServletContext().setAttribute("prefix",prefix);
        request.getServletContext().setAttribute("ruleList",ruleList);
        return "expand/writerule.jsp";
    }


    @RequestMapping("saveRule.do")
    public String saveRule(HttpServletRequest request){
        String id = request.getServletContext().getAttribute("currentId").toString();
        String count = request.getParameter("count").toString();

        StringBuilder sb = new StringBuilder("");
        System.out.println(count);
        for (int i=0;i<=Integer.valueOf(count);i++){
            sb.append(request.getParameter("rule"+i)+"000000");
        }

        mService.saveRule(id,sb.toString());
        System.out.println(sb.toString());
        return "redirect:/listowl.do";
    }
}
