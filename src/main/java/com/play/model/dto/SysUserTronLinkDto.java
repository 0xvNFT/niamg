package com.play.model.dto;

import com.play.model.SysUserTronLink;

/**
 * @author Leon
 * @create 2023-06-24 16:25
 */
public class SysUserTronLinkDto extends SysUserTronLink {

    /**
     * 信息备注
     */
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
