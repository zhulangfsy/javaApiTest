package com.api.entity;

public class Interfaces {
    private String apiId;
    private String apiName;
    private String type;
    private String url;
    private String dependApiId;
    private String dependField;
    private String beDependField;

    public String getDependApiId() {
        return dependApiId;
    }

    public void setDependApiId(String dependApiId) {
        this.dependApiId = dependApiId;
    }

    public String getDependField() {
        return dependField;
    }

    public void setDependField(String dependField) {
        this.dependField = dependField;
    }

    public String getBeDependField() {
        return beDependField;
    }

    public void setBeDependField(String beDependField) {
        this.beDependField = beDependField;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
