package com.yyn.model;

/**
 * Created by koala on 2017/5/15.
 */
public class MyOwl {
    private int id;
    private String name;
    private String description;
    private String file;
//    private String basefile = "H:/sensor/RDF_Database/wot.owl";

    public MyOwl(int id, String name, String description, String file) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.file = file;
    }

    public MyOwl(String name, String description, String file) {
        this.name = name;
        this.description = description;
        this.file = file;
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
}
