package com.supcon.mes.module_score.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.util.LogUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.constant.ListType;
import com.supcon.mes.mbap.network.Api;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomGalleryView;
import com.supcon.mes.mbap.view.CustomNumView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.AttachmentDownloadController;
import com.supcon.mes.middleware.model.bean.AttachmentEntity;
import com.supcon.mes.middleware.util.HtmlParser;
import com.supcon.mes.middleware.util.HtmlTagHandler;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_score.R;
import com.supcon.mes.module_score.constant.ScoreConstant;
import com.supcon.mes.module_score.controller.ScoreCameraController;
import com.supcon.mes.module_score.model.bean.ScoreDutyEamEntity;
import com.supcon.mes.module_score.model.bean.ScoreStaffPerformanceEntity;
import com.supcon.mes.module_score.model.network.ScoreService;
import com.supcon.mes.module_score.ui.ScoreInspectorStaffPerformanceActivity;
import com.supcon.mes.module_score.ui.ScoreMechanicStaffPerformanceActivity;
import com.supcon.mes.module_score.ui.ScoreStaffPerformanceActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ScoreStaffPerformanceNewAdapter extends BaseListDataRecyclerViewAdapter<ScoreStaffPerformanceEntity> {

    private boolean isEdit = false;
    private float total;

    private ScoreCameraController mScoreCameraController;

    public ScoreStaffPerformanceNewAdapter(Context context) {
        super(context);
    }

    public void setEditable(boolean isEdit) {
        this.isEdit = isEdit;
    }

    public void updateTotal(float total) {
        this.total = total;
    }

    @Override
    protected BaseRecyclerViewHolder<ScoreStaffPerformanceEntity> getViewHolder(int viewType) {
        if (viewType == ListType.TITLE.value()) {
            return new TitleViewHolder(context);
        }else if (viewType == ListType.HEADER.value()) {
            return new MuiltViewHolder(context);
        }
        return new ViewHolder(context);
    }

    @Override
    public int getItemViewType(int position, ScoreStaffPerformanceEntity scoreEamPerformanceEntity) {
        if (scoreEamPerformanceEntity.defaultValueType != null && ScoreConstant.ValueType.T4.equals(scoreEamPerformanceEntity.defaultValueType.id)){
            return ListType.HEADER.value();
        }
        return scoreEamPerformanceEntity.viewType;
    }

    class TitleViewHolder extends BaseRecyclerViewHolder<ScoreStaffPerformanceEntity> {

        @BindByTag("contentTitle")
        TextView contentTitle;
        @BindByTag("fraction")
        TextView fraction;

        TitleViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_score_performance_title;
        }

        @Override
        protected void initView() {
            super.initView();
        }

        @Override
        protected void initListener() {
            super.initListener();

        }

        @SuppressLint({"StringFormatMatches", "SetTextI18n"})
        @Override
        protected void update(ScoreStaffPerformanceEntity data) {
            contentTitle.setText(data.category);
            fraction.setText(Util.big0(data.fraction) + "分");
        }
    }

    class ViewHolder extends BaseRecyclerViewHolder<ScoreStaffPerformanceEntity> {
        private AttachmentDownloadController mAttachmentDownloadController;
        @BindByTag("itemIndex")
        TextView itemIndex;
        @BindByTag("scoreItem")
        TextView scoreItem;

        @BindByTag("scoreRadioGroup")
        RadioGroup scoreRadioGroup;
        @BindByTag("scoreRadioBtn1")
        RadioButton scoreRadioBtn1;
        @BindByTag("scoreRadioBtn2")
        RadioButton scoreRadioBtn2;
        @BindByTag("sum")
        CustomNumView sum;
        @BindByTag("handleScore")
        CustomEditText handleScore;
        @BindByTag("itemPics")
        CustomGalleryView itemPics;
        @BindByTag("ufItemPhotoIv")
        ImageView ufItemPhotoIv;


        public ViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_score_performance_content;
        }

        @Override
        protected void initView() {
            super.initView();
//            itemPics = itemView.findViewById(R.id.itemPics);
            sum.setEnabled(isEdit);
            sum.getNumViewInput().setEnabled(false);
            scoreRadioBtn1.setEnabled(isEdit);
            scoreRadioBtn2.setEnabled(isEdit);
            handleScore.setEditable(isEdit);
            handleScore.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            if (!isEdit) {
                itemPics.setEditable(false);
            }

            if (mScoreCameraController == null) {
                if (context instanceof ScoreInspectorStaffPerformanceActivity) {
                    mScoreCameraController = ((ScoreInspectorStaffPerformanceActivity) context).getController(ScoreCameraController.class);
                }
                if (context instanceof ScoreMechanicStaffPerformanceActivity) {
                    mScoreCameraController = ((ScoreMechanicStaffPerformanceActivity) context).getController(ScoreCameraController.class);
                }
                if (context instanceof ScoreStaffPerformanceActivity) {
                    mScoreCameraController = ((ScoreStaffPerformanceActivity) context).getController(ScoreCameraController.class);
                }
                mScoreCameraController.init(Constant.IMAGE_SAVE_SCORE_PATH, Constant.PicType.SCORE_PIC);
            }
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            scoreRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                if (scoreRadioBtn1.isPressed() || scoreRadioBtn2.isPressed()) {
                        ScoreStaffPerformanceEntity item = getItem(getAdapterPosition());
                        item.result = !item.result;
                        float otherTotalScore = total - item.scoreEamPerformanceEntity.fraction; // 除当前标题的总分数
                        if (checkedId == R.id.scoreRadioBtn1) { // 是
                            // 标题分数 -
                            item.scoreEamPerformanceEntity.fraction = item.scoreEamPerformanceEntity.fraction - item.score < 0 ? 0 : item.scoreEamPerformanceEntity.fraction - item.score;
                            item.theoreticalScore = item.score; // 当前理论项扣分数
                        } else if (checkedId == R.id.scoreRadioBtn2) { // 否
                            item.theoreticalScore = 0; // 当前理论项扣分数
                            // 标题分数 +
                            // 判断是否当前类别中的某项已经全部扣完分数
                            itemAddScore(item);
//                            item.scoreEamPerformanceEntity.fraction = item.scoreEamPerformanceEntity.fraction + item.score > item.defaultTotalScore ? item.defaultTotalScore : item.scoreEamPerformanceEntity.fraction + item.score;
                        }
                        // 更新标题分数
                        List<ScoreStaffPerformanceEntity> list = getList();
                        int position = list.indexOf(item.scoreEamPerformanceEntity);
                        notifyItemChanged(position);

                        notifyItemChanged(getAdapterPosition()); // 是否显示拍照

                    //更新总分数
                    item.scoreNum = otherTotalScore + item.scoreEamPerformanceEntity.fraction;
                    onItemChildViewClick(scoreRadioGroup, 0, item);

//                    item.result = !item.result;
//                    float oldTotal = total - item.scoreEamPerformanceEntity.fraction;
//                    if (!item.result) {
//                        item.scoreEamPerformanceEntity.setTotalHightScore(item.scoreEamPerformanceEntity.getTotalHightScore() + item.itemScore);
//                        if (item.scoreEamPerformanceEntity.getTotalHightScore() >= 0) {
//                            item.scoreEamPerformanceEntity.fraction = item.scoreEamPerformanceEntity.getTotalHightScore();
//                        }
//                    } else {
//                        item.scoreEamPerformanceEntity.setTotalHightScore(item.scoreEamPerformanceEntity.getTotalHightScore() - item.itemScore);
//                        item.scoreEamPerformanceEntity.fraction = item.scoreEamPerformanceEntity.fraction - item.itemScore;
//                    }
//                    if (item.scoreEamPerformanceEntity.fraction < 0) {
//                        item.scoreEamPerformanceEntity.fraction = 0;
//                    } else if (item.scoreEamPerformanceEntity.fraction > item.scoreEamPerformanceEntity.score) {
//                        item.scoreEamPerformanceEntity.fraction = item.scoreEamPerformanceEntity.score;
//                    }
//
//                    notifyItemChanged(getAdapterPosition()); // 是否显示拍照
//
//                    List<ScoreStaffPerformanceEntity> list = getList();
//                    int position = list.indexOf(item.scoreEamPerformanceEntity);
//                    notifyItemChanged(position);
//
//                    //更新总分数
//                    item.scoreEamPerformanceEntity.scoreNum = oldTotal + item.scoreEamPerformanceEntity.fraction;
//                    onItemChildViewClick(scoreRadioGroup, 0, item.scoreEamPerformanceEntity);
                    }
        });
            sum.setTextListener(text -> {
                ScoreStaffPerformanceEntity item = getItem(getAdapterPosition());
                if (Util.strToInt(text) == item.defaultNumVal) {
                    return;
                }
                float otherTotalScore = total - item.scoreEamPerformanceEntity.fraction; // 除当前标题的总分数
//                float subScore = item.score * (Util.strToInt(text) - item.defaultNumVal); // 当前项扣分数
                if (item.defaultNumVal < Util.strToInt(text)) { // 次数加
                    // 标题分数 -
                    item.scoreEamPerformanceEntity.fraction = item.scoreEamPerformanceEntity.fraction - item.score < 0 ? 0 : item.scoreEamPerformanceEntity.fraction - item.score;
                    item.theoreticalScore = item.theoreticalScore + item.score; // 当前理论项扣分数
                } else { // 次数减
                    item.theoreticalScore = item.theoreticalScore - item.score;
                    // 标题分数 +
//                    // 判断是否当前类别中的某项已经全部扣完分数
                    itemAddScore(item);

//                    item.scoreEamPerformanceEntity.fraction = item.scoreEamPerformanceEntity.fraction + item.score > item.defaultTotalScore ? item.defaultTotalScore : item.scoreEamPerformanceEntity.fraction + item.score;
                }
                item.defaultNumVal = Util.strToInt(text);
                // 更新标题分数
                List<ScoreStaffPerformanceEntity> list = getList();
                int position = list.indexOf(item.scoreEamPerformanceEntity);
                notifyItemChanged(position);
                notifyItemChanged(getAdapterPosition()); // 是否显示拍照

                //更新总分数
                item.scoreNum = otherTotalScore + item.scoreEamPerformanceEntity.fraction;
                onItemChildViewClick(sum, 0, item);
            });
            RxTextView.textChanges(handleScore.editText())
                    .skipInitialValue()
                    .skip(1)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .filter(charSequence -> {
//                        if (!isEdit)return false;
                        handleScore.editText().setSelection(charSequence.length());
                        ScoreStaffPerformanceEntity item = getItem(getAdapterPosition());
                        if (item == null || charSequence.toString().equals(item.subScore))
                            return false;
                        if (Util.strToFloat(charSequence.toString()) > item.score) {
                            ToastUtils.show(context, "当前项目最高" + item.score + "分");
                            handleScore.setContent(charSequence.subSequence(0, charSequence.length() - 1).toString());
//                            handleScore.editText().setSelection(charSequence.length()-1);
                            return false;
                        }
                        return true;
                    })
                    .subscribe(charSequence -> {
                        ScoreStaffPerformanceEntity item = getItem(getAdapterPosition());
                        item.subScore = charSequence.toString();
                        float otherTotalScore = total - item.scoreEamPerformanceEntity.fraction; // 除当前标题的总分数

                        // 标题更新分数
                        item.scoreEamPerformanceEntity.fraction = item.scoreEamPerformanceEntity.fraction + item.lastSubScore - Util.strToFloat(charSequence.toString());

                        // 更新标题分数
                        List<ScoreStaffPerformanceEntity> list = getList();
                        int position = list.indexOf(item.scoreEamPerformanceEntity);
                        notifyItemChanged(position);
                        notifyItemChanged(getAdapterPosition()); // 是否显示拍照

                        //更新总分数
                        item.scoreNum = otherTotalScore + item.scoreEamPerformanceEntity.fraction;
                        onItemChildViewClick(handleScore, 0, item);
                    });
            RxView.clicks(ufItemPhotoIv).throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe(o -> {
                        mScoreCameraController.setCurrAdapterPosition(getAdapterPosition(), itemPics);
                        itemPics.findViewById(R.id.customCameraIv).performClick();  //调用CustomGalleryView的拍照按钮
//                        mScoreCameraController.showCustomDialog();
                    });
        }

        @SuppressLint({"StringFormatMatches", "SetTextI18n", "CheckResult"})
        @Override
        protected void update(ScoreStaffPerformanceEntity data) {
            mScoreCameraController.addGalleryView(getAdapterPosition(), itemPics, ScoreStaffPerformanceNewAdapter.this);

            if (data.getAttachFileMultiFileIds().size() == 0 && data.getAttachFileFileAddPaths().size() == 0){ // 服务器及本地均未有附件
                itemPics.clear();
            }else {
                initAttachFiles(data, mAttachmentDownloadController, itemPics);
            }

            itemIndex.setText(data.index + ".");
            Spanned item = HtmlParser.buildSpannedText(String.format(context.getString(R.string.device_style12), data.item, Util.big0(data.itemScore == 0 ? data.score : data.itemScore)), new HtmlTagHandler());
            scoreItem.setText(item);

            sum.setNum(data.defaultNumVal);
            data.lastSubScore = Util.strToFloat(data.subScore); // 赋值上一次扣分数
            handleScore.setContent(data.subScore);

            if (data.scoreType != null && data.scoreType.id.equals(ScoreConstant.ScoreItemType.T1)){ // 自动
                handleScore.setVisibility(View.GONE);
                scoreRadioGroup.setVisibility(View.GONE);
                sum.setVisibility(View.GONE);

            }else { // 手动
                if (data.defaultValueType != null && ScoreConstant.ValueType.T1.equals(data.defaultValueType.id)) {
                    sum.setVisibility(View.VISIBLE);
                    scoreRadioGroup.setVisibility(View.GONE);
                    handleScore.setVisibility(View.GONE);
                    data.theoreticalScore = data.score * data.defaultNumVal;
                } else if (data.defaultValueType != null && ScoreConstant.ValueType.T2.equals(data.defaultValueType.id)) {
                    scoreRadioGroup.setVisibility(View.VISIBLE);
                    sum.setVisibility(View.GONE);
                    handleScore.setVisibility(View.GONE);
                    data.theoreticalScore = data.result ? data.score : 0;
                } else if (data.defaultValueType != null && ScoreConstant.ValueType.T3.equals(data.defaultValueType.id)) {
                    handleScore.setVisibility(View.VISIBLE);
                    scoreRadioGroup.setVisibility(View.GONE);
                    sum.setVisibility(View.GONE);
                    data.theoreticalScore = TextUtils.isEmpty(data.subScore) ? 0 : Float.parseFloat(data.subScore);
                } else {
                    handleScore.setVisibility(View.GONE);
                    scoreRadioGroup.setVisibility(View.GONE);
                    sum.setVisibility(View.GONE);
                    data.theoreticalScore = 0;
                }
            }

            scoreRadioBtn1.setText(data.isItemValue);
            scoreRadioBtn2.setText(data.noItemValue);
            scoreRadioBtn1.setChecked(data.result);
            scoreRadioBtn2.setChecked(!data.result);
            if (!isEdit) {
                if (data.result) {
                    scoreRadioBtn1.setButtonDrawable(R.drawable.ic_check_box_true_small_gray);
                    scoreRadioBtn2.setButtonDrawable(R.drawable.ic_check_box_false_small);
                } else {
                    scoreRadioBtn1.setButtonDrawable(R.drawable.ic_check_box_false_small);
                    scoreRadioBtn2.setButtonDrawable(R.drawable.ic_check_box_true_small_gray);
                }
            }
            if (isEdit && (data.result || data.defaultNumVal > 0 || !TextUtils.isEmpty(data.subScore))) {
                ufItemPhotoIv.setVisibility(View.VISIBLE);
            } else {
                ufItemPhotoIv.setVisibility(View.GONE);
            }

        }

        @SuppressLint("CheckResult")
        private void autoCalculationScore(ScoreStaffPerformanceEntity data) {
            Api.getInstance().retrofit.create(ScoreService.class).getDutyEamNew(((ScoreInspectorStaffPerformanceActivity) context).getScoreStaffEntity().patrolWorker.id, ScoreConstant.ScoreType.INSPECTION_STAFF)
                    .subscribeOn(Schedulers.newThread())
                    .onErrorReturn(throwable -> {
                        ScoreDutyEamEntity scoreDutyEamEntity = new ScoreDutyEamEntity();
                        scoreDutyEamEntity.errMsg = throwable.toString();
                        return scoreDutyEamEntity;
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(scoreDutyEamEntity -> {
                        if (scoreDutyEamEntity.success) {
                            if (data.viewType == 1 && "专业巡检".equals(data.project)) {
                                data.scoreEamPerformanceEntity.fraction = scoreDutyEamEntity.professInspScore;
                            } else {
                                data.scoreEamPerformanceEntity.fraction = scoreDutyEamEntity.avgScore;
                            }
                            data.scoreEamPerformanceEntity.scoreNum = total - data.scoreEamPerformanceEntity.fraction;
                            notifyItemChanged(getList().indexOf(data.scoreEamPerformanceEntity));
                            ((ScoreInspectorStaffPerformanceActivity) context).updateTotalScore(data.scoreEamPerformanceEntity);
//                            onItemChildViewClick(itemView,0,data);
//                            updateTotal(total-data.scoreEamPerformanceEntity.fraction);
                        } else {
                            LogUtils.error(scoreDutyEamEntity.errMsg);
                        }
                    });
        }

    }

    class MuiltViewHolder extends BaseRecyclerViewHolder<ScoreStaffPerformanceEntity> {
        private AttachmentDownloadController mAttachmentDownloadController;
        @BindByTag("itemIndex")
        TextView itemIndex;
        @BindByTag("acceptanceItem")
        TextView acceptanceItem;
        @BindByTag("titleItemLl")
        LinearLayout titleItemLl;
        @BindByTag("chkBox")
        CheckBox chkBox;
        @BindByTag("itemDetails")
        TextView itemDetails;
        @BindByTag("itemDetailsLl")
        RelativeLayout itemDetailsLl;
        @BindByTag("itemPics")
        CustomGalleryView itemPics;
        @BindByTag("ufItemPhotoIv")
        ImageView ufItemPhotoIv;


        public MuiltViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_eam_score_mult;
        }

        @Override
        protected void initView() {
            super.initView();
            chkBox.setEnabled(isEdit);
            if (mScoreCameraController == null) {
                if (context instanceof ScoreStaffPerformanceActivity) {
                    mScoreCameraController = ((ScoreStaffPerformanceActivity) context).getController(ScoreCameraController.class);
                }
                mScoreCameraController.init(Constant.IMAGE_SAVE_SCORE_PATH, Constant.PicType.SCORE_PIC);
            }
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            chkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ScoreStaffPerformanceEntity item = getItem(getAdapterPosition());
                    item.result = ((CheckBox)v).isChecked();
                    float otherTotalScore = total - item.scoreEamPerformanceEntity.fraction; // 除当前标题的总分数
                    if (((CheckBox)v).isChecked()) { // 是
                        // 标题分数 -
                        item.scoreEamPerformanceEntity.fraction = item.scoreEamPerformanceEntity.fraction - item.score < 0 ? 0 : item.scoreEamPerformanceEntity.fraction - item.score;
                        item.theoreticalScore = item.score; // 当前理论项扣分数
                    } else { // 否
                        item.theoreticalScore = 0; // 当前理论项扣分数
                        // 标题分数 +
                        // 判断是否当前类别中的某项已经全部扣完分数
                        itemAddScore(item);
//                            item.scoreEamPerformanceEntity.fraction = item.scoreEamPerformanceEntity.fraction + item.score > item.defaultTotalScore ? item.defaultTotalScore : item.scoreEamPerformanceEntity.fraction + item.score;
                    }
                    // 更新标题分数
                    List<ScoreStaffPerformanceEntity> list = getList();
                    int position = list.indexOf(item.scoreEamPerformanceEntity);
                    notifyItemChanged(position);

                    notifyItemChanged(getAdapterPosition()); // 是否显示拍照

                    //更新总分数
                    item.scoreNum = otherTotalScore + item.scoreEamPerformanceEntity.fraction;
                    onItemChildViewClick(chkBox, 0, item);
                }
            });
            RxView.clicks(ufItemPhotoIv).throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe(o -> {
                        mScoreCameraController.setCurrAdapterPosition(getAdapterPosition(), itemPics);
                        itemPics.findViewById(R.id.customCameraIv).performClick();  //调用CustomGalleryView的拍照按钮
//                        mScoreCameraController.showCustomDialog();
                    });
        }

        @SuppressLint({"StringFormatMatches", "SetTextI18n"})
        @Override
        protected void update(ScoreStaffPerformanceEntity data) {
            mScoreCameraController.addGalleryView(getAdapterPosition(), itemPics, ScoreStaffPerformanceNewAdapter.this);

            if (data.getAttachFileMultiFileIds().size() == 0 && data.getAttachFileFileAddPaths() .size() == 0){ // 服务器及本地均未有附件
                itemPics.clear();
            }else {
                initAttachFiles(data, mAttachmentDownloadController, itemPics);
            }

            if (data.viewType == ListType.HEADER.value()){
                titleItemLl.setVisibility(View.VISIBLE);
                itemDetailsLl.setVisibility(View.GONE);

                itemIndex.setText(data.index + ".");
                acceptanceItem.setText(data.item);

            }else {
                titleItemLl.setVisibility(View.GONE);
                itemDetailsLl.setVisibility(View.VISIBLE);

                Spanned item = HtmlParser.buildSpannedText(String.format(context.getString(R.string.device_style12), data.itemDetail, Util.big0(data.score == 0 ? data.score : data.score)), new HtmlTagHandler());
                itemDetails.setText(item);
//                itemDetails.setText(data.itemDetail);
                chkBox.setChecked(data.result);
                if (!isEdit) {
                    if (data.result) {
                        chkBox.setButtonDrawable(R.drawable.ic_check_box_true_small_gray);
                    } else {
                        chkBox.setButtonDrawable(R.drawable.sl_checkbox_selector_small);
                    }
                }

                if (isEdit && (data.result || data.defaultNumVal > 0 || !TextUtils.isEmpty(data.subScore))) {
                    ufItemPhotoIv.setVisibility(View.VISIBLE);
                } else {
                    ufItemPhotoIv.setVisibility(View.GONE);
                }
            }
        }

    }

    /**
     * @author zhangwenshuai1 2020/8/15
     * @param
     * @return
     * @description 判断是否当前类别中的项已经全部扣完类别总分数，否则+
     *
     */
    private void itemAddScore(ScoreStaffPerformanceEntity item) {
        float categoryTotalScore = 0;
        for (ScoreStaffPerformanceEntity entity : getList()){
            if (entity.category.equals(item.category)){
                categoryTotalScore += entity.theoreticalScore;
            }
        }
        if (categoryTotalScore < item.defaultTotalScore){
            item.scoreEamPerformanceEntity.fraction = item.defaultTotalScore - categoryTotalScore;
        }
    }

    private void initAttachFiles(ScoreStaffPerformanceEntity data, AttachmentDownloadController downloadController, CustomGalleryView galleryView) {
        List<AttachmentEntity> attachmentEntities;
        if (downloadController == null) {
            downloadController = new AttachmentDownloadController(Constant.IMAGE_SAVE_SCORE_PATH);
        }
        if (data.getAttachmentEntityList() != null) {
            attachmentEntities = data.getAttachmentEntityList();
        }else {
            AttachmentEntity attachmentEntity;
            attachmentEntities = new ArrayList<>();
            if (data.getAttachFileMultiFileIds() != null) { // 服务器
//                List<String> attachFileIdList = Arrays.asList(data.getAttachFileMultiFileIds().split(","));
//                List<String> attachFileNameList = Arrays.asList(data.getAttachFileMultiFileNames().split(","));
                for (Long id : data.getAttachFileMultiFileIds()) {
                    attachmentEntity = new AttachmentEntity();
                    attachmentEntity.id = id;
                    attachmentEntity.name = data.getAttachFileMultiFileNames().get(data.getAttachFileMultiFileIds().indexOf(id));
                    attachmentEntity.deploymentId = attachmentEntity.id; // 赋值附件id,防止下载过滤
                    attachmentEntities.add(attachmentEntity);
                }
                data.setAttachmentEntityList(attachmentEntities);
            }
            if (data.getAttachFileFileAddPaths() != null) { // 本地添加
//                List<String> attachFileAddPathsList = Arrays.asList(data.getAttachFileFileAddPaths().split(","));
                for (String path : data.getAttachFileFileAddPaths()) {
                    attachmentEntity = new AttachmentEntity();
                    attachmentEntity.id = -1L;
                    attachmentEntity.name = path.substring(path.lastIndexOf("\\")+1);
                    attachmentEntity.deploymentId = attachmentEntity.id; // 赋值附件id,防止下载过滤
                    attachmentEntities.add(attachmentEntity);
                }
                data.setAttachmentEntityList(attachmentEntities);
            }
        }

        downloadController.downloadPic(attachmentEntities, "BEAM_1.0.0_patrolWorkerScore", result -> {
//                gifIv.setVisibility(View.GONE);
            galleryView.setGalleryBeans(result);
        });

//        else {
//            gifIv.setVisibility(View.GONE);
//        }
    }


}
