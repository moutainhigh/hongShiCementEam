package com.supcon.mes.module_wxgd.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseRefreshActivity;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.utils.controllers.DatePickController;
import com.supcon.mes.mbap.view.CustomDateView;
import com.supcon.mes.mbap.view.CustomSpinner;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomVerticalDateView;
import com.supcon.mes.mbap.view.CustomVerticalEditText;
import com.supcon.mes.mbap.view.CustomVerticalSpinner;
import com.supcon.mes.mbap.view.CustomVerticalTextView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.EamPicController;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntityDao;
import com.supcon.mes.middleware.model.bean.WXGDEntity;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_wxgd.IntentRouter;
import com.supcon.mes.module_wxgd.R;
import com.supcon.mes.module_wxgd.controller.AcceptanceCheckController;
import com.supcon.mes.module_wxgd.controller.LubricateOilsController;
import com.supcon.mes.module_wxgd.controller.MaintenanceController;
import com.supcon.mes.module_wxgd.controller.RepairStaffController;
import com.supcon.mes.module_wxgd.controller.SparePartController;
import com.supcon.mes.module_wxgd.model.api.WXGDListAPI;
import com.supcon.mes.module_wxgd.model.bean.WXGDListEntity;
import com.supcon.mes.module_wxgd.model.contract.WXGDListContract;
import com.supcon.mes.module_wxgd.presenter.WXGDListPresenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * WXGDCompleteActivity 完成Activity
 * created by zhangwenshuai1 2018/8/15
 * 已完成
 */

@Router(value = Constant.Router.WXGD_COMPLETE)
@Controller(value = {SparePartController.class, RepairStaffController.class, LubricateOilsController.class,
        MaintenanceController.class, AcceptanceCheckController.class})
@Presenter(value = {WXGDListPresenter.class})
public class WXGDCompleteActivity extends BaseRefreshActivity implements WXGDListContract.View {

    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("workSource")
    TextView workSource;

    ImageView eamIc;
    @BindByTag("eamName")
    CustomTextView eamName;
    @BindByTag("eamCode")
    CustomTextView eamCode;
    @BindByTag("eamArea")
    CustomTextView eamArea;
    @BindByTag("discoverer")
    CustomTextView discoverer;

    @BindByTag("faultInfoType")
    CustomTextView faultInfoType;
    @BindByTag("repairType")
    CustomSpinner repairType;
    @BindByTag("priority")
    CustomTextView priority;
    @BindByTag("faultInfoDescribe")
    CustomVerticalTextView faultInfoDescribe;
    @BindByTag("faultInfo")
    LinearLayout faultInfo;
    @BindByTag("repairAdvise")
    CustomVerticalEditText repairAdvise;

    @BindByTag("content")
    CustomTextView content;

    @BindByTag("repairGroup")
    CustomTextView repairGroup;
    @BindByTag("chargeStaff")
    CustomTextView chargeStaff;
    @BindByTag("wosource")
    CustomTextView wosource;
    @BindByTag("planStartTime")
    CustomDateView planStartTime;
    @BindByTag("planEndTime")
    CustomDateView planEndTime;
    @BindByTag("realEndTime")
    CustomVerticalDateView realEndTime;
    @BindByTag("dispatcherStaff")
    CustomTextView dispatcherStaff;

    @BindByTag("workContext")
    CustomVerticalEditText workContext;

    @BindByTag("eleOffChkBox")
    CheckBox eleOffChkBox; // 是否生成停电票
    @BindByTag("eleOff")
    CustomTextView eleOff;

    private WXGDEntity mWXGDEntity;//传入维修工单实体参数

//    private DatePickController mDatePickController;
//    private List<SystemCodeEntity> checkResultList = new ArrayList<>();
//    private List<String> checkResultListStr = new ArrayList<>();
//    private boolean mStatisticSource; // 是否来自报表统计跳转

    @Override
    protected int getLayoutID() {
        return R.layout.ac_wxgd_complee;
    }

    @Override
    protected void onInit() {
        super.onInit();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        refreshController.setPullDownRefreshEnabled(true);
        refreshController.setAutoPullDownRefresh(true);
        mWXGDEntity = (WXGDEntity) getIntent().getSerializableExtra(Constant.IntentKey.WXGD_ENTITY);
//        mStatisticSource = getIntent().getBooleanExtra(Constant.IntentKey.STATISTIC_SORCE,false);

        getController(SparePartController.class).setEditable(false);
        getController(RepairStaffController.class).setEditable(false);
        getController(LubricateOilsController.class).setEditable(false);
        getController(AcceptanceCheckController.class).setEditable(false);
        getController(MaintenanceController.class).setEditable(false);

//        mDatePickController = new DatePickController(this);
//        mDatePickController.setSecondVisible(true);
//        mDatePickController.setDividerVisible(true);
//        mDatePickController.setCanceledOnTouchOutside(true);

//        initCheckResult();
    }

//    private void initCheckResult() {
//        checkResultList = EamApplication.dao().getSystemCodeEntityDao().queryBuilder().where(SystemCodeEntityDao.Properties.EntityCode.eq(Constant.SystemCode.CHECK_RESULT)).list();
//        for (SystemCodeEntity entity : checkResultList) {
//            checkResultListStr.add(entity.value);
//        }
//    }

    @Override
    protected void initView() {
        super.initView();
        eamIc = findViewById(R.id.eamIc);
//        if (mStatisticSource){
            titleText.setText("工单查看");
//        }else {
//            titleText.setText("生效");
//        }
    }


    /**
     * @param
     * @return
     * @description 初始化表头显示
     * @author zhangwenshuai1 2018/8/16
     */
    private void initTableHeadView() {
        if (mWXGDEntity.faultInfo == null) {
            faultInfo.setVisibility(View.GONE);
        } else {
            if (TextUtils.isEmpty(mWXGDEntity.faultInfo.tableNo)) {
                faultInfo.setVisibility(View.GONE);
            } else {
                faultInfo.setVisibility(View.VISIBLE);
            }
        }
        repairGroup.setEditable(false);
        chargeStaff.setEditable(false);
        planStartTime.setEditable(false);
        planEndTime.setEditable(false);
        realEndTime.setEditable(false);
        realEndTime.setNecessary(false);
        repairType.setEditable(false);
        repairAdvise.setEditable(false);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    /**
     * @param
     * @return
     * @description 初始化表头数据
     * @author zhangwenshuai1 2018/8/16
     */
    private void initTableHeadData() {
        workSource.setText(mWXGDEntity.workSource == null ? "" : mWXGDEntity.workSource.value);
        if (mWXGDEntity.eamID != null && mWXGDEntity.eamID.id != null) {
            eamName.setValue(mWXGDEntity.eamID.name);
            eamCode.setValue(mWXGDEntity.eamID.eamAssetCode);
            eamArea.setValue(mWXGDEntity.eamID.installPlace == null ? "" : mWXGDEntity.eamID.installPlace.name);

            new EamPicController().initEamPic(eamIc, mWXGDEntity.eamID.id);
        }
        if (mWXGDEntity.faultInfo != null) {
            discoverer.setValue(mWXGDEntity.faultInfo.findStaffID != null ? mWXGDEntity.faultInfo.findStaffID.name : "");
            faultInfoType.setValue(mWXGDEntity.faultInfo.faultInfoType == null ? "" : mWXGDEntity.faultInfo.faultInfoType.value);
            priority.setValue(mWXGDEntity.faultInfo.priority == null ? "" : mWXGDEntity.faultInfo.priority.value);
            faultInfoDescribe.setValue(mWXGDEntity.faultInfo.describe);
        }
        dispatcherStaff.setContent(!TextUtils.isEmpty(mWXGDEntity.getDispatcher().name) ? mWXGDEntity.getDispatcher().name : EamApplication.getAccountInfo().staffName);
        wosource.setContent(mWXGDEntity.workSource != null ? mWXGDEntity.workSource.value : "");
        repairType.setSpinner(mWXGDEntity.repairType != null ? mWXGDEntity.repairType.value : "");
        repairAdvise.setContent(mWXGDEntity.repairAdvise);
        chargeStaff.setValue(Util.strFormat2(mWXGDEntity.getChargeStaff().name));
        repairGroup.setValue(mWXGDEntity.repairGroup != null ? mWXGDEntity.repairGroup.name : "");
        planStartTime.setDate(mWXGDEntity.planStartDate == null ? "" : DateUtil.dateTimeFormat(mWXGDEntity.planStartDate));
        planEndTime.setDate(mWXGDEntity.planEndDate == null ? "" : DateUtil.dateTimeFormat(mWXGDEntity.planEndDate));
        realEndTime.setDate(mWXGDEntity.realEndDate == null ? "" : DateUtil.dateFormat(mWXGDEntity.realEndDate, "yyyy-MM-dd HH:mm:ss"));

        workContext.setContent(mWXGDEntity.workOrderContext);
        if (mWXGDEntity.offApply != null && mWXGDEntity.offApply.id != null){
            eleOffChkBox.setButtonDrawable(R.drawable.ic_checked);
//            eleOffChkBox.setBackgroundResource(R.drawable.ic_checked);
        }else {
            eleOffChkBox.setButtonDrawable(null);
        }
        eleOffChkBox.setClickable(false);
    }

    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> onBackPressed());
        refreshController.setOnRefreshListener(() -> {
            Map<String,Object> queryParam = new HashMap<>();
            queryParam.put(Constant.BAPQuery.TABLE_NO,mWXGDEntity.tableNo);
            presenterRouter.create(WXGDListAPI.class).listWxgds(1,queryParam,true);
        });
        eamName.getCustomValue().setOnClickListener(v -> goSBDA());
        eamIc.setOnClickListener(v -> goSBDA());
        eamCode.getCustomValue().setOnClickListener(v -> goSBDA());

    }

    private void goSBDA() {
        if (mWXGDEntity.eamID == null || mWXGDEntity.eamID.id == null) {
            ToastUtils.show(context, "无设备详情可查看！");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putLong(Constant.IntentKey.SBDA_ONLINE_EAMID, mWXGDEntity.eamID.id);
        bundle.putString(Constant.IntentKey.SBDA_ONLINE_EAMCODE, mWXGDEntity.eamID.code);
        IntentRouter.go(context, Constant.Router.SBDA_ONLINE_VIEW, bundle);
    }

    @Override
    public void listWxgdsSuccess(WXGDListEntity entity) {
        List<WXGDEntity> wxgdEntityList = entity.result;
        if (wxgdEntityList.size() > 0) {
            mWXGDEntity = wxgdEntityList.get(0);
            initTableHeadView();
            initTableHeadData();
            getController(RepairStaffController.class).setWxgdEntity(mWXGDEntity);
            getController(SparePartController.class).setWxgdEntity(mWXGDEntity);
            getController(LubricateOilsController.class).setWxgdEntity(mWXGDEntity);
            getController(MaintenanceController.class).setWxgdEntity(mWXGDEntity);
            getController(MaintenanceController.class).setWxgdEntity(mWXGDEntity);
        } else {
            ToastUtils.show(this, "未查到当前单据信息");
        }
        refreshController.refreshComplete();
    }

    @Override
    public void listWxgdsFailed(String errorMsg) {
        refreshController.refreshComplete();
        ToastUtils.show(context, errorMsg);
    }
}
