package com.junyouava.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 权证业务编号信息
 */
public class EWTBizNoInfo {
    @JsonProperty("ewt_biz_no")
    private String ewtBizNo;

    public EWTBizNoInfo() {
    }

    public EWTBizNoInfo(String ewtBizNo) {
        this.ewtBizNo = ewtBizNo;
    }

    public String getEwtBizNo() {
        return ewtBizNo;
    }

    public void setEwtBizNo(String ewtBizNo) {
        this.ewtBizNo = ewtBizNo;
    }
}

