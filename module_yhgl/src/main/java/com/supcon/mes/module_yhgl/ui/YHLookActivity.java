package com.supcon.mes.module_yhgl.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshActivity;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.ptr.PtrFrameLayout;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.beans.WorkFlowEntity;
import com.supcon.mes.mbap.beans.WorkFlowVar;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomDateView;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomGalleryView;
import com.supcon.mes.mbap.view.CustomSpinner;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomVerticalEditText;
import com.supcon.mes.mbap.view.CustomVerticalSpinner;
import com.supcon.mes.mbap.view.CustomVerticalTextView;
import com.supcon.mes.mbap.view.CustomWorkFlowView;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.constant.Module;
import com.supcon.mes.middleware.controller.AttachmentController;
import com.supcon.mes.middleware.controller.DealInfoController;
import com.supcon.mes.middleware.controller.LinkController;
import com.supcon.mes.middleware.controller.OnlineCameraController;
import com.supcon.mes.middleware.controller.PcController;
import com.supcon.mes.middleware.controller.WorkFlowKeyController;
import com.supcon.mes.middleware.model.bean.AttachmentListEntity;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonDeviceEntity;
import com.supcon.mes.middleware.model.bean.CommonSearchStaff;
import com.supcon.mes.middleware.model.bean.EamEntity;
import com.supcon.mes.middleware.model.bean.Staff;
import com.supcon.mes.middleware.model.bean.WXGDEam;
import com.supcon.mes.middleware.model.bean.YHEntity;
import com.supcon.mes.middleware.model.event.CommonSearchEvent;
import com.supcon.mes.middleware.model.event.DeviceAddEvent;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.listener.OnAPIResultListener;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_wxgd.constant.WXGDConstant;
import com.supcon.mes.module_yhgl.IntentRouter;
import com.supcon.mes.module_yhgl.R;
import com.supcon.mes.module_yhgl.constant.YhConstant;
import com.supcon.mes.module_yhgl.controller.LubricateOilsController;
import com.supcon.mes.module_yhgl.controller.MaintenanceController;
import com.supcon.mes.module_yhgl.controller.RepairStaffController;
import com.supcon.mes.module_yhgl.controller.SparePartController;
import com.supcon.mes.module_yhgl.model.api.YHListAPI;
import com.supcon.mes.module_yhgl.model.api.YHSubmitAPI;
import com.supcon.mes.module_yhgl.model.bean.YHListEntity;
import com.supcon.mes.module_yhgl.model.contract.YHListContract;
import com.supcon.mes.module_yhgl.model.contract.YHSubmitContract;
import com.supcon.mes.module_yhgl.presenter.YHListPresenter;
import com.supcon.mes.module_yhgl.presenter.YHSubmitPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * YHLookActivity 隐患单查看
 * created by zhangwenshuai1 2019/10/9
 */
@Router(Constant.Router.YH_LOOK)
@Presenter({YHSubmitPresenter.class, YHListPresenter.class})
@Controller(value = {SparePartController.class, LubricateOilsController.class, RepairStaffController.class,
        MaintenanceController.class, OnlineCameraController.class, AttachmentController.class, PcController.class,WorkFlowKeyController.class})
public class YHLookActivity extends BaseRefreshActivity implements YHSubmitContract.View, YHListContract.View {

    @BindByTag("leftBtn")
    ImageButton leftBtn;

    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("yhViewFindTime")
    CustomDateView yhViewFindTime;

    @BindByTag("yhViewFindStaff")
    CustomTextView yhViewFindStaff;

    @BindByTag("yhViewArea")
    CustomSpinner yhViewArea;

    @BindByTag("yhViewPriority")
    CustomSpinner yhViewPriority;


    @BindByTag("yhViewEamCode")
    CustomTextView yhViewEamCode;

    @BindByTag("yhViewEamName")
    CustomTextView yhViewEamName;

    @BindByTag("yhViewEamModel")
    CustomTextView yhViewEamModel;

    @BindByTag("yhViewType")
    CustomSpinner yhViewType;

    @BindByTag("yhViewWXType")
    CustomVerticalSpinner yhViewWXType;

    @BindByTag("yhViewWXGroup")
    CustomVerticalSpinner yhViewWXGroup;

    @BindByTag("yhViewDescription")
    CustomVerticalTextView yhViewDescription;

    @BindByTag("yhGalleryView")
    CustomGalleryView yhGalleryView;

    @BindByTag("yhViewMemo")
    CustomVerticalEditText yhViewMemo;

    @BindByTag("yhViewCommentInput")
    CustomEditText yhViewCommentInput;

    @BindByTag("yhViewTransition")
    CustomWorkFlowView yhViewTransition;

    @BindByTag("refreshFrameLayout")
    PtrFrameLayout refreshFrameLayout;
    @BindByTag("yhDealBar")
    LinearLayout yhDealBar;

    @BindByTag("eleOffRadioGroup")
    RadioGroup eleOffRadioGroup; // 是否生成停电票
    @BindByTag("eleOff")
    CustomTextView eleOff;
    @BindByTag("recyclerView")
    RecyclerView recyclerView;

    private YHEntity mYHEntity;
    private AttachmentController mAttachmentController;
    private DealInfoController mDealInfoController;
    private String __pc__;

    @Override
    protected int getLayoutID() {
        return R.layout.ac_yh_look;
    }

    @Override
    protected void onInit() {
        super.onInit();
        refreshController.setAutoPullDownRefresh(true);
        refreshController.setPullDownRefreshEnabled(true);
        mYHEntity = (YHEntity) getIntent().getSerializableExtra(Constant.IntentKey.YHGL_ENTITY);

        getController(OnlineCameraController.class).init(Constant.IMAGE_SAVE_YHPATH, Constant.PicType.YH_PIC);
        mAttachmentController = getController(AttachmentController.class);

        mDealInfoController = new DealInfoController(context,recyclerView,null);

    }

    @Override
    protected void onRegisterController() {
        super.onRegisterController();
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(context.getResources().getString(R.string.fault_look));
    }

     /**
      * @method  视图内容填充
      * @description
      * @author: zhangwenshuai
      * @date: 2020/5/30 17:07
      * @param  * @param null
      * @return
      */
    private void setView(){
        yhViewFindStaff.setValue(mYHEntity.findStaffID != null ? mYHEntity.findStaffID.name : "");
        yhViewFindTime.setDate(DateUtil.dateTimeFormat(mYHEntity.findTime));
        yhViewPriority.setSpinner(mYHEntity.priority != null ? mYHEntity.priority.value : "");
        yhViewArea.setSpinner(mYHEntity.areaInstall != null ? mYHEntity.areaInstall.name : "");

        if (mYHEntity.eamID != null) {
            yhViewEamCode.setValue(mYHEntity.eamID.eamAssetCode);
            yhViewEamName.setValue(mYHEntity.eamID.name);
            yhViewEamModel.setValue(mYHEntity.eamID.model);
        }

        yhViewType.setSpinner(mYHEntity.faultInfoType != null ? mYHEntity.faultInfoType.value : "");
        yhViewWXType.setSpinner(mYHEntity.repairType != null ? mYHEntity.repairType.value : "");
        yhViewWXGroup.setSpinner(mYHEntity.repiarGroup != null ? mYHEntity.repiarGroup.name : "");
        if (!TextUtils.isEmpty(mYHEntity.describe)) {
            yhViewDescription.setContent(mYHEntity.describe);
        }

        if (mYHEntity.isPowerCut != null){
            if (mYHEntity.isPowerCut.id.equals(WXGDConstant.EleOff.yes)){
                eleOffRadioGroup.check(R.id.yesRadioButton);
            }else {
                eleOffRadioGroup.check(R.id.noRadioButton);
            }
        }else {
            eleOffRadioGroup.clearCheck();
        }
        for (int i = 0; i < eleOffRadioGroup.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) eleOffRadioGroup.getChildAt(i);
            radioButton.setEnabled(false);
            if (radioButton.isChecked()) {
                radioButton.setButtonDrawable(R.drawable.ic_check_box_true_small_gray);
            }
        }

        if (!TextUtils.isEmpty(mYHEntity.remark)) {
            yhViewMemo.setInput(mYHEntity.remark);
        }
        initPic();
        getSubmitPc(mYHEntity.pending.activityName);
    }

    /**
     * @param
     * @return 获取单据提交pc
     * @description
     * @author user 2019/10/31
     */
    private void getSubmitPc(String operateCode) {
        getController(WorkFlowKeyController.class).queryWorkFlowKeyToPc(operateCode,Constant.EntityCode.FAULT_INFO, null, new OnAPIResultListener<Object>() {
            @Override
            public void onFail(String errorMsg) {
                ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
            }

            @Override
            public void onSuccess(Object result) {
                __pc__ = String.valueOf(result);
            }
        });
    }

    /**
     * 加载隐患图片
     */
    private void initPic() {
        if (mYHEntity != null) {
            mAttachmentController.refreshGalleryView(new OnAPIResultListener<AttachmentListEntity>() {
                @Override
                public void onFail(String errorMsg) {

                }

                @Override
                public void onSuccess(AttachmentListEntity result) {
                    if (result.result.size() > 0) {
                        mYHEntity.attachmentEntities = result.result;
                        getController(OnlineCameraController.class).setPicData(mYHEntity.attachmentEntities,"BEAM2_1.0.0_faultInfo");
                    }
                }
            }, mYHEntity.tableInfoId == null ? -1 : mYHEntity.tableInfoId);
        }
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();

        RxView.clicks(leftBtn)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(o -> onBackPressed());
        refreshController.setOnRefreshListener(() -> {
            Map<String, Object> queryParam = new HashMap<>();
            queryParam.put(Constant.BAPQuery.TABLE_NO,mYHEntity.tableNo);
            presenterRouter.create(YHListAPI.class).queryYHList(1, queryParam,true);
        });

        /*yhViewGalleryView.setOnChildViewClickListener((childView, action, obj) -> {

            int position = (int) obj;

            if (position == -1) {
                return;
            }
            List<GalleryBean> galleryBeans = yhViewGalleryView.getGalleryAdapter().getList();

            if (action == CustomGalleryView.ACTION_VIEW) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", (ArrayList) FaultPicHelper.getImagePathList(galleryBeans));//非必须
                bundle.putInt("position", position);
                int[] location = new int[2];
                childView.getLocationOnScreen(location);
                bundle.putInt("locationX", location[0]);//必须
                bundle.putInt("locationY", location[1]);//必须

                bundle.putInt("width", DisplayUtil.dip2px(100, context));//必须
                bundle.putInt("height", DisplayUtil.dip2px(100, context));//必须
                bundle.putBoolean("isEditable", false);

                getWindow().setWindowAnimations(R.style.fadeStyle);
                IntentRouter.go(context, Constant.Router.IMAGE_VIEW, bundle);
            }

        });*/

        yhViewTransition.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                switch (action) {
                    case 0:
                        doSave();
                        break;
                    case 1:
                    case 2:
                        if (checkBeforeSubmit()){
                            doSubmit((WorkFlowVar)obj);
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        yhViewEamCode.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                Bundle bundle = new Bundle();
                bundle.putString(Constant.IntentKey.MODULE, Module.Fault.name());
                IntentRouter.go(context, Constant.Router.ADD_DEVICE, bundle);
            }
        });

        yhViewEamName.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                Bundle bundle = new Bundle();
                bundle.putString(Constant.IntentKey.MODULE, Module.Fault.name());
                IntentRouter.go(context, Constant.Router.ADD_DEVICE, bundle);
            }
        });

        yhViewFindStaff.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constant.IntentKey.IS_MULTI, false);
                bundle.putBoolean(Constant.IntentKey.IS_SELECT_STAFF, true);
                IntentRouter.go(context, Constant.Router.CONTACT_SELECT, bundle);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addDeviceEvent(DeviceAddEvent deviceAddEvent) {

        String add = deviceAddEvent.getDeviceEntity();
        if (TextUtils.isEmpty(add)) {
            return;
        }
        List<CommonDeviceEntity> deviceEntities = GsonUtil.jsonToList(add, CommonDeviceEntity.class);
        if (deviceEntities != null && deviceEntities.size() != 0) {
            CommonDeviceEntity commonDeviceEntity = deviceEntities.get(0);
            yhViewEamName.setValue(commonDeviceEntity.eamName);
            yhViewEamCode.setValue(commonDeviceEntity.eamCode);
            yhViewEamModel.setValue(commonDeviceEntity.eamModel);
            EamEntity eamEntity = new EamEntity();
            eamEntity.name = commonDeviceEntity.eamName;
            eamEntity.code = commonDeviceEntity.eamCode;
            eamEntity.model = commonDeviceEntity.eamModel;
            eamEntity.id = commonDeviceEntity.eamId;
            mYHEntity.eamID = eamEntity;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMaintenanceStaff(CommonSearchEvent event) {
        if (event.commonSearchEntity instanceof CommonSearchStaff) {
            CommonSearchStaff searchStaff = (CommonSearchStaff) event.commonSearchEntity;
            yhViewFindStaff.setValue(searchStaff.name);
            Staff staff = new Staff();
            staff.id = searchStaff.id;
            staff.code = searchStaff.code;
            staff.name = searchStaff.name;
            mYHEntity.findStaffID = staff;
        }

    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setWindowAnimations(R.style.activityAnimation);
    }

    @Override
    public void onBackPressed() {
        finish();
        //退出前校验表单是否修改过
        //如果修改过, 提示是否保存
//        if (checkIsModified()) {
//            new CustomDialog(context)
//                    .twoButtonAlertDialog("页面已经被修改，是否要保存?")
//                    .bindView(R.id.grayBtn, "保存")
//                    .bindView(R.id.redBtn, "离开")
//                    .bindClickListener(R.id.grayBtn, view ->doSave(), true)
//                    .bindClickListener(R.id.redBtn, v3 -> {
//                        //关闭页面
//                        finish();
//                    }, true)
//                    .show();
//        } else {
//            finish();
//        }

    }

    /**
     * 提交之前校验必填字段
     *
     * @return
     */
    private boolean checkBeforeSubmit() {


//        if (TextUtils.isEmpty(yhViewTransition.currentTransition().outCome)) {
//            SnackbarHelper.showError(rootView, "请选择操作！");
//            return false;
//        }

        return true;
    }

    private void doSave() {
        submit(null);
    }

    private void doSubmit(WorkFlowVar workFlowVar) {
        List<WorkFlowEntity> workFlowEntities = new ArrayList<>();
        WorkFlowEntity workFlowEntity = new WorkFlowEntity();
        workFlowEntity.dec = workFlowVar.dec;
        workFlowEntity.type = workFlowVar.outcomeMapJson.get(0).type;
        workFlowEntity.outcome = workFlowVar.outCome;
        workFlowEntities.add(workFlowEntity);

        submit(workFlowEntities);
    }

    private void submit(List<WorkFlowEntity> workFlowEntities) {
        WorkFlowEntity workFlowEntity = null;
        if (workFlowEntities != null && workFlowEntities.size() != 0) {
            workFlowEntity = workFlowEntities.get(0);
        } else {
            workFlowEntity = new WorkFlowEntity();
        }

        Map<String, Object> map = new HashMap<>();
        map.put("bap_validate_user_id", String.valueOf(EamApplication.getAccountInfo().userId));
        map.put("faultInfo.createStaffId", mYHEntity.findStaffID.id);
        map.put("faultInfo.createTime", DateUtil.dateTimeFormat(mYHEntity.findTime));
        map.put("faultInfo.findStaffID.id", mYHEntity.findStaffID.id);
        map.put("faultInfo.findTime", DateUtil.dateTimeFormat(mYHEntity.findTime));
        map.put("faultInfo.createPositionId", EamApplication.getAccountInfo().positionId);


        if (mYHEntity.id != null) {
            map.put("id", mYHEntity.id);
            map.put("faultInfo.id", mYHEntity.id);
        } else {
            map.put("id", "");
            map.put("faultInfo.id", "");
        }

        if (mYHEntity.pending != null && mYHEntity.pending.id != null) {

            map.put("pendingId", mYHEntity.pending.id);
            map.put("deploymentId", mYHEntity.pending.deploymentId);
        } else {
            map.put("deploymentId", 1040);
            map.put("faultInfo.version", 1);
        }


        if (mYHEntity.eamID != null && mYHEntity.eamID.id != null) {
            map.put("faultInfo.eamID.id", mYHEntity.eamID.id);
        } else {
            map.put("faultInfo.eamID.id", "");
        }


        if (mYHEntity.areaInstall != null && mYHEntity.areaInstall.id != 0) {
            map.put("faultInfo.areaInstall.id", mYHEntity.areaInstall.id);
        } else {
            map.put("faultInfo.areaInstall.id", "");
        }

        if (mYHEntity.repiarGroup != null && mYHEntity.repiarGroup.id != null) {
            map.put("faultInfo.repiarGroup.id", mYHEntity.repiarGroup.id);
        } else {
            map.put("faultInfo.repiarGroup.id", "");
        }

        if (mYHEntity.faultInfoType != null) {
            map.put("faultInfo.faultInfoType.id", mYHEntity.faultInfoType.id);
            map.put("faultInfo.faultInfoType.value", mYHEntity.faultInfoType.value);
        } else {
            map.put("faultInfo.faultInfoType.id", "");
            map.put("faultInfo.faultInfoType.value", "");
        }

        if (mYHEntity.priority != null) {
            map.put("faultInfo.priority.id", mYHEntity.priority.id);
        } else {
            map.put("faultInfo.priority.id", "");
        }

        if (mYHEntity.repairType != null) {
            map.put("faultInfo.repairType.id", mYHEntity.repairType.id);
        } else {
            map.put("faultInfo.repairType.id", "");
        }

        if (mYHEntity.describe != null)
            map.put("faultInfo.describe", mYHEntity.describe);
        if (mYHEntity.remark != null)
            map.put("faultInfo.remark", mYHEntity.remark);

        map.put("linkId", mYHEntity.tableInfoId != null ? mYHEntity.tableInfoId : "");
//        map.put("dlTableInfoId",                mYHEntity.tableInfoId);
        map.put("tableInfoId", mYHEntity.tableInfoId != null ? mYHEntity.tableInfoId : "");
        if (workFlowEntities != null) {//保存为空
            map.put("workFlowVar.outcomeMapJson", workFlowEntities);
            map.put("workFlowVar.dec", workFlowEntity.dec);
            map.put("workFlowVar.outcome", workFlowEntity.outcome);
            map.put("operateType", "submit");
        } else {
//            map.put("workFlowVar.dec", "");
//            map.put("workFlowVar.outcome", "");
            map.put("operateType", "save");
        }
        map.put("taskDescription", "BEAM2_1.0.0.faultInfoFW.task342");
        map.put("workFlowVar.comment", yhViewCommentInput.getInput());

        map.put("viewCode", "BEAM2_1.0.0_faultInfo_faultInfoEdit");

        map.put("modelName", "FaultInfo");
        map.put("datagridKey", "BEAM2_faultInfo_faultInfo_faultInfoEdit_datagrids");
        map.put("__file_upload", true);


        LogUtil.d(GsonUtil.gsonString(map));
        onLoading("正在提交...");
        presenterRouter.create(YHSubmitAPI.class).doSubmit(map, null, __pc__,false);

    }


    @Override
    public void doSubmitSuccess(BapResultEntity entity) {
        LogUtil.d("entity:" + entity);

        RefreshEvent refreshEvent = new RefreshEvent();
        EventBus.getDefault().post(refreshEvent);

        onLoadSuccessAndExit("处理成功！", this::finish);
    }

    @Override
    public void doSubmitFailed(String errorMsg) {
        LogUtil.e("errorMsg:" + errorMsg);
        onLoadFailed(errorMsg);
        SnackbarHelper.showError(rootView, ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void queryYHListSuccess(YHListEntity entity) {
        if (entity.result.size() > 0){
            mYHEntity = entity.result.get(0);
//            mOriginalEntity = GsonUtil.gsonToBean(mYHEntity.toString(), YHEntity.class);
            setView();
            getController(SparePartController.class).refreshData(mYHEntity);
            getController(LubricateOilsController.class).refreshData(mYHEntity);
            getController(RepairStaffController.class).refreshData(mYHEntity);
            getController(MaintenanceController.class).refreshData(mYHEntity);
            // 加载处理意见
            if (mYHEntity.tableInfoId != null){
                mDealInfoController.listTableDealInfo(YhConstant.URL.PRE_URL,mYHEntity.tableInfoId);
            }
        }
        refreshController.refreshComplete();
    }

    @Override
    public void queryYHListFailed(String errorMsg) {
        ToastUtils.show(context,ErrorMsgHelper.msgParse(errorMsg));
    }
}
