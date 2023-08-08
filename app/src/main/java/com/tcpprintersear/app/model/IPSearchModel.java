package com.tcpprintersear.app.model;

public class IPSearchModel {
    public String getStrIp() {
        return strIp;
    }

    public void setStrIp(String strIp) {
        this.strIp = strIp;
    }

    public String getStrMac() {
        return strMac;
    }

    public void setStrMac(String strMac) {
        this.strMac = strMac;
    }

    String strIp,strMac;
    public IPSearchModel(String strIp , String strMac ){
        this.strMac=strMac;
        this.strIp=strIp;
    }
}

