package com.supcon.mes.module_hs_tsd.controller;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.view.CustomListWidget;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_hs_tsd.R;
import com.supcon.mes.module_hs_tsd.constant.OperateItemOffEnum;
import com.supcon.mes.module_hs_tsd.model.api.OperateItemAPI;
import com.supcon.mes.module_hs_tsd.model.bean.OperateItemEntity;
import com.supcon.mes.module_hs_tsd.model.bean.OperateItemListEntity;
import com.supcon.mes.module_hs_tsd.model.contract.OperateItemContract;
import com.supcon.mes.module_hs_tsd.presenter.OperateItemPresenter;
import com.supcon.mes.module_hs_tsd.ui.adapter.OperateItemAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 停送电pt: 注:目前非根据设备动态加载模板
 */
@Presenter(value = {OperateItemPresenter.class})
public class OperateItemOffController extends BaseViewController implements OperateItemContract.View {

    @BindByTag("operateItemWidget")
    CustomListWidget<OperateItemEntity> operateItemWidget;

    private Long tableId; // 单据ID

    public List<OperateItemEntity> getOperateItemEntityList() {
        return operateItemEntityList;
    }

    private List<OperateItemEntity> operateItemEntityList = new ArrayList<>();

    public OperateItemOffController(View rootView) {
        super(rootView);
    }

    @Override
    public void onInit() {
        super.onInit();
        tableId = getIntent().getLongExtra(Constant.IntentKey.TABLE_ID,-1);
    }

    @Override
    public void initView() {
        super.initView();
        operateItemWidget.setTitleBgColor(Color.parseColor("#F9F9F9"));
        operateItemWidget.setShowText("");
        RecyclerView contentView = operateItemWidget.findViewById(R.id.contentView);
        contentView.setBackgroundColor(context.getResources().getColor(R.color.line_gray));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(3, context)));

        operateItemWidget.setAdapter(new OperateItemAdapter(context));

    }
    @Override
    public void initData() {
        super.initData();
        if (tableId == -1){
            initOperateItemEntityList();
        }
    }

    public void listOperateItems(){
//        Glide.with(context).asGif().load(R.drawable.preloader_1).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).into(customListWidgetEdit);
        String url = "/BEAMEle/onOrOff/onoroff/data-dg1545361488690.action?datagridCode=BEAMEle_1.0.0_onOrOff_eleOffEditdg1545361488690&rt=json";
        presenterRouter.create(OperateItemAPI.class).listOperateItems(url,tableId);
    }

    private void initOperateItemEntityList() {
        OperateItemListEntity entity = new OperateItemListEntity();
        List<OperateItemEntity> operateItemEntityList = new ArrayList<>();
        OperateItemEntity operateItemEntity;
        for (OperateItemOffEnum detail : OperateItemOffEnum.values()){
            operateItemEntity = new OperateItemEntity();
            operateItemEntity.setCaution(detail.getValue());
            operateItemEntityList.add(operateItemEntity);
        }
        entity.result = operateItemEntityList;
        listOperateItemsSuccess(entity);
    }

    @Override
    public void listOperateItemsSuccess(OperateItemListEntity entity) {
        operateItemEntityList = entity.result;
        operateItemWidget.setData(operateItemEntityList);
    }

    @Override
    public void listOperateItemsFailed(String errorMsg) {
        LogUtil.w(errorMsg);
    }
}
