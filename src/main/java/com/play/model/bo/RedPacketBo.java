package com.play.model.bo;

import java.io.Serializable;

public class RedPacketBo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 红包ID
     */
    private Long packetId;

    public Long getPacketId() {
        return packetId;
    }

    public void setPacketId(Long packetId) {
        this.packetId = packetId;
    }
}
