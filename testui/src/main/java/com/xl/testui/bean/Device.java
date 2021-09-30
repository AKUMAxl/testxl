package com.xl.testui.bean;


public class Device {


    private String name;
    private String p2pmac;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getP2pmac() {
        return p2pmac;
    }

    public void setP2pmac(String p2pmac) {
        this.p2pmac = p2pmac;
    }

    @Override
    public String toString() {
        return "Device{" +
                "name='" + name + '\'' +
                ", p2pmac='" + p2pmac + '\'' +
                '}';
    }
}
