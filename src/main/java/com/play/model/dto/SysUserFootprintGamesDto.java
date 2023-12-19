package com.play.model.dto;

import com.play.model.SysUserFootprintGames;

public class SysUserFootprintGamesDto extends SysUserFootprintGames {
    private static final long serialVersionUID = 1L;

    /**
     * 游戏名称
     */
    private String gameName;

    /**
     * 第三方游戏链接
     */
    private String thirdGameUrl;

    /**
     * 游戏图片链接
     */
    private String imageUrl;

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getThirdGameUrl() {
        return thirdGameUrl;
    }

    public void setThirdGameUrl(String thirdGameUrl) {
        this.thirdGameUrl = thirdGameUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
