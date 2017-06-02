package com.yyn.controller;

import com.yyn.service.RuleService;
import com.yyn.util.ResolveRule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by koala on 2017/5/23.
 */
@Controller
public class RuleController {

    @Autowired
    RuleService mService;

    @RequestMapping("goWriteRule.do")
    public String goWriteRule(HttpServletRequest request){
        String file = request.getParameter("owlfile");
        String prefix = ResolveRule.getSprqlPre(file);
        String rulePre = ResolveRule.getRulePre(file);
        System.out.println(rulePre);

        request.getServletContext().setAttribute("currentFile",file);
        request.getServletContext().setAttribute("prefix",prefix);
        return "expand/writerule.jsp";
    }


    @RequestMapping("saveRule.do")
    public String saveRule(HttpServletRequest request){
        String file = request.getServletContext().getAttribute("currentFile").toString();
        String rule = request.getParameter("rule").toString();
        System.out.println(rule);
        mService.saveRule(file,rule);
        return "redirect:/listowl.do";
    }
}
