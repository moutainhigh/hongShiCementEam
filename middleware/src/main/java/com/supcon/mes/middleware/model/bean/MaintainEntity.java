package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

import java.math.BigDecimal;

public class MaintainEntity extends BaseEntity {

    public Long id;
    public String basicJwx;//是否历史数据
    public JWXItem jwxItemID; //业务规则
    public String remark;//备注
    public BigDecimal lastDuration; // 上次执行时长(H)
    public BigDecimal nextDuration; // 下次执行时长(H)
    public Long lastTime; // 上次执行时间
    public Long nextTime; // 下次执行时间

    public boolean isWarn;

    public JWXItem getJwxItem() {
        if (jwxItemID == null) {
            jwxItemID = new JWXItem();
        }
        return jwxItemID;
    }

}
