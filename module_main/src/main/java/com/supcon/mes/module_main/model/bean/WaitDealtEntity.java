package com.supcon.mes.module_main.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.Staff;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/7/24
 * ------------- Description -------------
 */
public class WaitDealtEntity extends BaseEntity {

    public Long dataid;     //dataid

    public String eamcode;     //设备编码

    public String eamname;   //设备名

    public Long excutetime;   //下次执行时间

    public String soucretype;  //来源

    public Long nextduration;  //下次执行时长

    public String overdateflag;//是否超期 1 超期  0 正常

    public String state;//状态  派工  执行  验收

    public String processkey;//工作流编码

    public SystemCodeEntity peroidtype;//时间类型

    public String istemp;//是否临时巡检

    public Long pendingid;//待办

    public Staff staffid;

    public String tableno;

    public String workTableno; // 注：统一使用

    public String openurl;

    public String entrflag; // 委托标志

    public String content;//内容

    public boolean isCheck;

    public Long endtime;
    public Long endtimeactual;
    public Long tableid;

    public Staff getStaffid() {
        if (staffid == null) {
            staffid = new Staff();
        }
        return staffid;
    }
}
