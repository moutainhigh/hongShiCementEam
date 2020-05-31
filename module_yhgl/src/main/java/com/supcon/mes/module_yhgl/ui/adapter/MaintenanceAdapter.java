package com.supcon.mes.module_yhgl.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.CustomSwipeLayout;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomVerticalEditText;
import com.supcon.mes.mbap.view.CustomVerticalTextView;
import com.supcon.mes.middleware.model.bean.MaintainEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_yhgl.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class MaintenanceAdapter extends BaseListDataRecyclerViewAdapter<MaintainEntity> {

    public boolean editable;
    private String tableStatus; //单据状态

    public MaintenanceAdapter(Context context, boolean editable) {
        super(context);
        this.editable = editable;
    }

    @Override
    protected BaseRecyclerViewHolder<MaintainEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public void setTableStatus(String tableStatus) {
        this.tableStatus = tableStatus;
    }

    class ViewHolder extends BaseRecyclerViewHolder<MaintainEntity> {

        @BindByTag("itemSwipeLayout")
        CustomSwipeLayout itemSwipeLayout;
        @BindByTag("main")
        LinearLayout main;
        @BindByTag("index")
        TextView index;
        @BindByTag("itemViewDelBtn")
        TextView itemViewDelBtn;

        @BindByTag("sparePartName")
        CustomVerticalTextView sparePartName;
        @BindByTag("attachEam")
        CustomVerticalTextView attachEam;

        @BindByTag("timeLayout")
        LinearLayout timeLayout;
        @BindByTag("durationLayout")
        LinearLayout durationLayout;

        @BindByTag("lastTime")
        CustomVerticalTextView lastTime;
        @BindByTag("nextTime")
        CustomVerticalTextView nextTime;

        @BindByTag("lastDuration")
        CustomVerticalTextView lastDuration;
        @BindByTag("nextDuration")
        CustomVerticalTextView nextDuration;

        @BindByTag("claim")
        CustomVerticalEditText claim;
        @BindByTag("content")
        CustomVerticalEditText content;

        @BindByTag("chkBox")
        CheckBox chkBox;


        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_yhgl_maintenance;
        }

        @Override
        protected void initView() {
            super.initView();
            chkBox.setVisibility(View.GONE);
            if (!editable) {
                itemViewDelBtn.setVisibility(View.GONE);
            }
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();


            main.setOnLongClickListener(v -> {
                itemSwipeLayout.open();
                return true;
            });

            itemViewDelBtn.setOnClickListener(v -> {
                MaintainEntity maintainEntity = getItem(getAdapterPosition());
                itemSwipeLayout.close();
                if (!editable) {
                    ToastUtils.show(context, tableStatus + "环节，维修人员不允许删除!");
                    return;
                }
                new CustomDialog(context)
                        .twoButtonAlertDialog("确认删除该备件：" + Util.strFormat(maintainEntity.getJwxItem().getSparePartId().getProductID().productName))
                        .bindView(R.id.redBtn, "确认")
                        .bindView(R.id.grayBtn, "取消")
                        .bindClickListener(R.id.redBtn, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                List<MaintainEntity> list = MaintenanceAdapter.this.getList();
                                list.remove(getAdapterPosition());
                                EventBus.getDefault().post(new RefreshEvent(maintainEntity.id));
                            }
                        }, true)
                        .bindClickListener(R.id.grayBtn, null, true)
                        .show();
            });
        }

        @Override
        protected void update(MaintainEntity data) {
            index.setText(String.valueOf(getAdapterPosition() + 1));
            if (data.jwxItemID != null){
                if (data.jwxItemID.sparePartId != null && data.jwxItemID.sparePartId.productID != null){
                    sparePartName.setContent(data.jwxItemID.sparePartId.productID.productName);
                }
                if (data.jwxItemID.accessoryEamId != null && data.jwxItemID.accessoryEamId.attachEamId != null){
                    attachEam.setContent(data.jwxItemID.accessoryEamId.attachEamId.name);
                }
                claim.setContent(data.jwxItemID.claim);
                content.setContent(data.jwxItemID.content);
            }

            if (data.getJwxItem().isDuration()) {
                durationLayout.setVisibility(View.VISIBLE);
                lastDuration.setValue(Util.bigDecimal2Str(data.lastDuration,0));
                nextDuration.setValue(Util.bigDecimal2Str(data.nextDuration,0));
            } else {
                timeLayout.setVisibility(View.VISIBLE);
                lastTime.setContent(data.lastTime != null ? DateUtil.dateFormat(data.lastTime) : "");
                nextTime.setContent(data.nextTime != null ? DateUtil.dateFormat(data.nextTime) : "");
            }


        }
    }

}
