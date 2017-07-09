package com.yyn.model;

import org.apache.jena.query.Dataset;

/**
 * Created by koala on 2017/5/15.
 */
public class MyOwl {
    private int id;
    private String name;
    private String description;
    private String file;
    private String root;
    private String rule;
    private String uri;
    private int user_id;

    public MyOwl(Integer id, String name, String description, String file, String root, String rule) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.file = file;
        this.root = root;
        this.rule = rule;
    }

    public MyOwl(Integer id, String name, String description, String file, String root, String rule, String uri) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.file = file;
        this.root = root;
        this.rule = rule;
        this.uri = uri;
    }

    public MyOwl(String name, String description, String file, String root, String uri,int user_id) {
        this.name = name;
        this.description = description;
        this.file = file;
        this.root = root;
        this.uri = uri;
        this.user_id = user_id;
    }

    public MyOwl(String name, String description, String file, String root) {
        this.name = name;
        this.description = description;
        this.file = file;
        this.root = root;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    private Dataset mDataset;
//    private String basefile = "H:/sensor/RDF_Database/wot.owl";


    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public Dataset getDataset() {
        return mDataset;
    }

    public void setDataset(Dataset dataset) {
        mDataset = dataset;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "MyOwl{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", file='" + file + '\'' +
                ", root='" + root + '\'' +
                ", uri='" + uri + '\'' +
                '}';
    }
}
