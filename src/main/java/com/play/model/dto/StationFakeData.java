package com.play.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Leon
 * @create 2023-09-27 15:09
 */
public class StationFakeData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 在线人数假数据
     */
    private BigDecimal numberOfPeopleOnline;

    /**
     * 获胜人数假数据
     */
    private BigDecimal numberOfWinners;

    public BigDecimal getNumberOfPeopleOnline() {
        return numberOfPeopleOnline;
    }

    public void setNumberOfPeopleOnline(BigDecimal numberOfPeopleOnline) {
        this.numberOfPeopleOnline = numberOfPeopleOnline;
    }

    public BigDecimal getNumberOfWinners() {
        return numberOfWinners;
    }

    public void setNumberOfWinners(BigDecimal numberOfWinners) {
        this.numberOfWinners = numberOfWinners;
    }
}
