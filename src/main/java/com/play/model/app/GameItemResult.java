package com.play.model.app;

/**
 * Created by johnson on 2017/11/9.
 */

public class GameItemResult {
    String ButtonImagePath;
    String DisplayName;
    String typeid;
    String LapisId;
    int single;

    //json中的新字段
    String img;
    String name;
    String type;

    String isApp;

    public String getIsApp() {
        return isApp;
    }

    public void setIsApp(String isApp) {
        this.isApp = isApp;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    String finalRelatveUrl;

    public String getFinalRelatveUrl() {
        return finalRelatveUrl;
    }

    public void setFinalRelatveUrl(String finalRelatveUrl) {
        this.finalRelatveUrl = finalRelatveUrl;
    }

    public String getButtonImagePath() {
        return ButtonImagePath;
    }

    public void setButtonImagePath(String buttonImagePath) {
        ButtonImagePath = buttonImagePath;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public String getLapisId() {
        return LapisId;
    }

    public void setLapisId(String lapisId) {
        LapisId = lapisId;
    }

    public int getSingle() {
        return single;
    }

    public void setSingle(int single) {
        this.single = single;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }
}
