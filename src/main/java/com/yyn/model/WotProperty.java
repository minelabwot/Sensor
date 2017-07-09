package com.yyn.model;

import java.util.List;
import java.util.Map;

/**
 * Created by koala on 2017/5/17.
 */
public class WotProperty {
    private String tableName;
    private List<String> concepts;
    private Map<String,List<String>> map;
    private Map<String,String> conceptUriMap;
    private Map<String,Boolean> isselect;
    private String fileName;
    private String uri;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Map<String, String> getConceptUriMap() {
        return conceptUriMap;
    }

    public void setConceptUriMap(Map<String, String> conceptUriMap) {
        this.conceptUriMap = conceptUriMap;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getConcepts() {
        return concepts;
    }

    public void setConcepts(List<String> concepts) {
        this.concepts = concepts;
    }

    public Map<String, List<String>> getMap() {
        return map;
    }

    public void setMap(Map<String, List<String>> map) {
        this.map = map;
    }

    public Map<String, Boolean> getIsselect() {
        return isselect;
    }

    public void setIsselect(Map<String, Boolean> isselect) {
        this.isselect = isselect;
    }
}
