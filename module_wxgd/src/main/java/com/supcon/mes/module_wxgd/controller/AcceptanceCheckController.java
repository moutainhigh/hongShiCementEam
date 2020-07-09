package com.supcon.mes.module_wxgd.controller;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.view.CustomListWidget;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.AcceptanceCheckEntity;
import com.supcon.mes.middleware.model.bean.WXGDEntity;
import com.supcon.mes.module_wxgd.IntentRouter;
import com.supcon.mes.module_wxgd.model.api.AcceptanceCheckAPI;
import com.supcon.mes.middleware.model.bean.AcceptanceCheckListEntity;
import com.supcon.mes.module_wxgd.model.contract.AcceptanceCheckContract;
import com.supcon.mes.middleware.model.event.ListEvent;
import com.supcon.mes.module_wxgd.presenter.AcceptanceCheckPresenter;
import com.supcon.mes.module_wxgd.ui.adapter.AcceptanceCheckAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangshizhan on 2018/8/28
 * Email:wangshizhan@supcom.com
 */
@Presenter(AcceptanceCheckPresenter.class)
public class AcceptanceCheckController extends BaseViewController implements AcceptanceCheckContract.View {

    @BindByTag("acceptanceCheckListWidget")
    CustomListWidget<AcceptanceCheckEntity> acceptanceCheckListWidget;

    private WXGDEntity mWxgdEntity;
    private List<AcceptanceCheckEntity> mAcceptanceCheckEntities = new ArrayList<>();
    private boolean isEditable;

    public AcceptanceCheckController(View rootView) {
        super(rootView);
    }

    @Override
    public void onInit() {
        super.onInit();
        mWxgdEntity = (WXGDEntity) ((Activity) context).getIntent().getSerializableExtra(Constant.IntentKey.WXGD_ENTITY);
    }

    @Override
    public void initView() {
        super.initView();
        acceptanceCheckListWidget.setAdapter(new AcceptanceCheckAdapter(context, isEditable));
    }

    @Override
    public void initListener() {
        super.initListener();
        acceptanceCheckListWidget.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                Bundle bundle = new Bundle();
                switch (action) {
                    case CustomListWidget.ACTION_VIEW_ALL:
                    case 0:
                        bundle.putBoolean(Constant.IntentKey.IS_EDITABLE, false);
                        bundle.putBoolean(Constant.IntentKey.IS_ADD, false);
                        bundle.putString(Constant.IntentKey.ACCEPTANCE_ENTITIES, mAcceptanceCheckEntities.toString());
                        IntentRouter.go(context, Constant.Router.WXGD_ACCEPTANCE_LIST, bundle);
                        break;
                    default:
                        break;
                }

            }
        });
    }

    @Override
    public void listAcceptanceCheckListSuccess(AcceptanceCheckListEntity entity) {
        for (AcceptanceCheckEntity acceptanceCheckEntity : entity.result) {
            if (acceptanceCheckEntity.remark == null) {
                acceptanceCheckEntity.remark = "";
            }
        }
        mAcceptanceCheckEntities = entity.result;
        if (acceptanceCheckListWidget != null) {
            acceptanceCheckListWidget.setData(entity.result);
            if (isEditable) {
                acceptanceCheckListWidget.setShowText("编辑 (" + mAcceptanceCheckEntities.size() + ")");
            } else {
                acceptanceCheckListWidget.setShowText("查看 (" + mAcceptanceCheckEntities.size() + ")");
            }
        }
        EventBus.getDefault().post(new ListEvent("acceptanceCheckEntity", mAcceptanceCheckEntities));
    }

    @Override
    public void listAcceptanceCheckListFailed(String errorMsg) {
        LogUtil.e("AcceptanceCheckController listAcceptanceCheckListFailed:" + errorMsg);
    }

    public void setCustomListWidget(CustomListWidget<AcceptanceCheckEntity> customListWidget) {
        this.acceptanceCheckListWidget = customListWidget;
    }

    @Override
    public void initData() {
        super.initData();
    }

    public void setWxgdEntity(WXGDEntity mWxgdEntity) {
        this.mWxgdEntity = mWxgdEntity;
        presenterRouter.create(AcceptanceCheckAPI.class).listAcceptanceCheckList(mWxgdEntity.id);
    }

    /**
     * @param
     * @return
     * @description 获取验收列表数据
     * @author zhangwenshuai1 2018/9/10
     */
    public List<AcceptanceCheckEntity> getAcceptanceCheckEntities() {
        if (mAcceptanceCheckEntities == null) {
            return new ArrayList<>();
        }
        return mAcceptanceCheckEntities;
    }

    /**
     * @param
     * @return
     * @description 更新列表数据
     * @author zhangwenshuai1 2018/9/10
     */
    public void updateAcceptanceCheckEntities(List<AcceptanceCheckEntity> list) {
        if (list == null) {
            return;
        }
        mAcceptanceCheckEntities = list;
        if (acceptanceCheckListWidget != null) {
//            mCustomListWidget.setData(mAcceptanceCheckEntities);
            if (isEditable) {
                acceptanceCheckListWidget.setShowText("编辑 (" + list.size() + ")");
            } else {
                acceptanceCheckListWidget.setShowText("查看 (" + list.size() + ")");
            }
        }
    }

    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }

    public void clear() {
        acceptanceCheckListWidget.clear();
    }
}
