package com.yyn.model;

import java.util.List;
import java.util.Map;

/**
 * Created by koala on 2017/5/12.
 */
public class Owldata {

    private Map<String,String> prefixs;
    private List<String> concepts;
    private Map<String,List<String>> map;
    private Map<String,String> nameUriMap;

    public Owldata() {
    }

    public Map<String, String> getPrefixs() {
        return prefixs;
    }

    public void setPrefixs(Map<String, String> prefixs) {
        this.prefixs = prefixs;
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

    public Map<String, String> getNameUriMap() {
        return nameUriMap;
    }

    public void setNameUriMap(Map<String, String> nameUriMap) {
        this.nameUriMap = nameUriMap;
    }
}
