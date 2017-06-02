package com.yyn.service;

import com.yyn.dao.OwlDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by koala on 2017/5/23.
 */
@Service
public class RuleService {

    @Autowired
    OwlDao mDao;
    public void saveRule(String file,String rule){

        mDao.saveRule(file,rule);
    }

}
