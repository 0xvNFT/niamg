package com.play.model.dto;

import com.play.model.StationHomepageGame;

import java.util.Date;

public class StationHomepageGameDto extends StationHomepageGame {

    /**
     * 开始日期
     */
    private Date startDate;

    /**
     * 结束日期
     */
    private Date endDate;

    /**
     * 是否子游戏
     */
    private Integer isSubGame;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getIsSubGame() {
        return isSubGame;
    }

    public void setIsSubGame(Integer isSubGame) {
        this.isSubGame = isSubGame;
    }
}
