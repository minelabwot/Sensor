package com.yyn.scheduler;

import com.yyn.service.AnomalyService;
import com.yyn.util.RDFReasoning;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by yangyunong on 2017/5/10.
 */
public class AnomalyDetectTask {
    @Autowired
    private AnomalyService as;


    public void runDetect() {
        System.out.println("anomaly diagnose start");
//        as.generateDiagModel(datasetContainer.getDataset());
//        RDFReasoning.output(datasetContainer.getDataset());
        System.out.println("anomaly diagnose finish");
    }
}
