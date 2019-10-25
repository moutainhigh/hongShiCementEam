package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * 备件领用申请明细
 */
public class SparePartReceiveEntity extends BaseEntity {

    public Long id;
    public Float origDemandQuity;//申请数量
    public Float currDemandQuity;//领用量
    public String remark;//备注
    public Good sparePartId;//备件编码
    public Double price; // 单价
    public Double total; // 总价


    public Good getSparePartId() {
        if (sparePartId == null) {
            sparePartId = new Good();
        }
        return sparePartId;
    }
}
