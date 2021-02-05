package com.xl.testui;

public class ImageItemBean {

    private String name;
    private String path;

    public ImageItemBean(){

    }

    public ImageItemBean(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "ImageItemBean{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
