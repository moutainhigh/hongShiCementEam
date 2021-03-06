package com.supcon.mes.module_olxj.model.bean;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.AttachmentEntity;
import com.supcon.mes.middleware.model.bean.EamEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.ValueEntity;
import com.supcon.mes.middleware.model.bean.WXGDEam;
import com.supcon.mes.module_olxj.constant.OLXJConstant;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by wangshizhan on 2019/4/1
 * Email:wangshizhan@supcom.com
 */
public class OLXJWorkItemEntity extends BaseEntity implements Comparable<OLXJWorkItemEntity> {

    /**
     * "autoAanalysis": false,
     * "autoGetValue": false,
     * "autoJudge": true,
     * "claim": null,
     * "content": "无漏灰",
     * "control": true,
     * "defaultVal": "正常",
     * "eamID": {
     * "code": "BEAM_TZ_002",
     * "id": 1005,
     * "name": "8#磨1#废石秤"
     * },
     * "id": 36176,
     * "inputStandardID": {
     * "decimalPlace": null,
     * "editTypeMoblie": {
     * "id": "mobileEAM054/02",
     * "value": "单选"
     * },
     * "id": 1011,
     * "name": "是否正常",
     * "standardCode": "BZ006",
     * "unitID": null,
     * "valueName": "正常,不正常",
     * "valueTypeMoblie": {
     * "id": "mobileEAM055/01",
     * "value": "字符"
     * }
     * },
     * "isSeismic": false,
     * "isThermometric": false,
     * "ispass": true,
     * "isphone": false,
     * "limitValue": null,
     * "llimitValue": null,
     * "normalRange": "正常",
     * "part": "斜槽管道",
     * "pstaffid": null,
     * "publicItemID": null,
     * "remark": null,
     * "signWorkID": {
     * "id": 1354,
     * "name":"区域名称"
     * },
     * "sort": 1,
     * "taskID": {
     * "id": 1190
     * },
     * "taskSignID": null,
     * "version": 1,
     * "workID": {
     * "id": 1354,
     * "name":"区域名称"
     * },
     * "workItemID": {
     * "id": 1967,
     * "remark": null
     * }
     */

    public long id;
    public boolean autoAanalysis;
    public boolean autoGetValue;
    public boolean autoJudge;
    public String claim;  //标准
    public String content;//内容
    public boolean control; //是否重录
    public String defaultVal;
    public EamEntity eamID;

    public OLXJInputStandard inputStandardID;

    public boolean isSeismic;
    public boolean isThermometric;
    public boolean ispass;//跳检
    public boolean isphone;//拍照
    public String limitValue;
    public String llimitValue;
    public String normalRange;//正常值

    public String part;//部位
    public Long pstaffid;
    public Object publicItemID;
    public OLXJArea signWorkID;

    public OLXJArea workID;
    public OLXJWorkItem workItemID;
    public OLXJTaskEntity taskID;

    public String remark;

    public String concluse;
    public long concluseTime;
    public String result;   //结果
    public String conclusionID;  //结论ID
    public String conclusionName;  //结论名称
    public String realRemark;  //备注
    public String endTime;  //结束时间
    public String skipReasonID;//跳过原因ID
    public String skipReasonName; //跳过原因名称
//    public String linkState = "wiLinkState/01"; //状态,默认待检
    private SystemCodeEntity linkState; //状态,默认待检
    public boolean isFinished = false;
    public Long staffId;
    public boolean isPhonere;//实际是否拍照
    public boolean realispass;
    public String xjImgUrl; //图片路径，逗号相隔
    public SystemCodeEntity realValue;
    public String itemnumber;//逻辑位号

    public int sort;//排序
    private int prioritySort;

    public ValueEntity priority;//优先级

    public boolean isEffective; // 隐患单是否生效（注：true：结论异常巡检项生成生效隐患单；否则编辑）
    public boolean isPush; // 是否推送群消息

    private TaskSignBean taskSignID; // 签到

    public BigDecimal leakRate1; // 漏检率
    public BigDecimal stopTime; // 停留时间(分钟)
    public int abnormalItem; // 异常项目数
    public int leakCheckItem; // 漏检项目数
    public int totalItem; // 所有项目数
    public int uncheckItem; // 未检项目数
    public String attachmentId; // 附件id
    public List<AttachmentEntity> attachmentEntityList;

    public Long tableInfoId;
    @Expose
    public String title;
    @Expose
    public int viewType = 0;
    @Expose
    public String headerPicPath;

    @Override
    public int compareTo(@NonNull OLXJWorkItemEntity o) {

//        if (this.eamID == null || o.eamID == null) {
//            return 0;
//        }
//
//        if (this.eamID.id.equals(o.eamID.id) && this.part != null) {
//            if (o.part == null) return 1;
//            return this.part.compareTo(o.part);
//        }
//
//        return (int) (this.eamID.id - o.eamID.id);
        if (this.getPrioritySort() == o.getPrioritySort()) {
            return this.sort - o.sort;
        } else {
            return o.getPrioritySort() - this.getPrioritySort();
        }
    }

    public int getPrioritySort() {
        if (priority == null) {
            prioritySort = 0;
        } else {
            if (priority.id.equals("mobileEAM_056/01")) {
                prioritySort = 1;
            } else if (priority.id.equals("mobileEAM_056/02")) {
                prioritySort = 0;
            }
        }
        return prioritySort;
    }

    public SystemCodeEntity getLinkState() {
        if (linkState == null){
            linkState = new SystemCodeEntity();
            linkState.setId(OLXJConstant.MobileWiLinkState.WAIT_STATE);
        }
        return  linkState;
    }

    public void setLinkState(SystemCodeEntity linkState) {
        this.linkState = linkState;
    }

    public TaskSignBean getTaskSignID() {
        if (taskSignID == null) {
            taskSignID = new TaskSignBean();
        }
        return taskSignID;
    }

    public void setTaskSignID(TaskSignBean taskSignID) {
        this.taskSignID = taskSignID;
    }

    public class TaskSignBean extends BaseEntity {

        /**
         * cardTime : null
         * cardType : null
         * cartReason : null
         * id : null
         */

        public Long cardTime; // 刷卡时间
        public SystemCodeEntity cardType; // 刷卡类型
        public SystemCodeEntity cartReason; // 手工签到原因
        public Long id;
    }

}
