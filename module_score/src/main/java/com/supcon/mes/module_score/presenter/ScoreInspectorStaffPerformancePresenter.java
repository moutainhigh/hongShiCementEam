package com.supcon.mes.module_score.presenter;

import android.text.TextUtils;

import com.supcon.mes.mbap.constant.ListType;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.module_score.constant.ScoreConstant;
import com.supcon.mes.module_score.model.bean.ScoreDutyEamEntity;
import com.supcon.mes.module_score.model.bean.ScoreStaffPerformanceEntity;
import com.supcon.mes.module_score.model.bean.ScoreStaffPerformanceListEntity;
import com.supcon.mes.module_score.model.contract.ScoreInspectorStaffPerformanceContract;
import com.supcon.mes.module_score.model.network.ScoreHttpClient;
import com.supcon.mes.module_score.ui.ScoreInspectorStaffPerformanceActivity;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class ScoreInspectorStaffPerformancePresenter extends ScoreInspectorStaffPerformanceContract.Presenter {

    private int position = 0;
    private String category = "";//评分标题
    private ScoreStaffPerformanceEntity scorePerformanceTitleEntity;

    @Override
    public void getDutyEam(long staffId, String scoreType) {
        mCompositeSubscription.add(ScoreHttpClient.getDutyEamNew(staffId, scoreType)
                .onErrorReturn(new Function<Throwable, ScoreDutyEamEntity>() {
                    @Override
                    public ScoreDutyEamEntity apply(Throwable throwable) throws Exception {
                        ScoreDutyEamEntity scoreDutyEamEntity = new ScoreDutyEamEntity();
                        scoreDutyEamEntity.errMsg = throwable.toString();
                        return scoreDutyEamEntity;
                    }
                })
                .subscribe(new Consumer<ScoreDutyEamEntity>() {
                    @Override
                    public void accept(ScoreDutyEamEntity scoreDutyEamEntity) throws Exception {
                        if (scoreDutyEamEntity.success) {
                            getView().getDutyEamSuccess(scoreDutyEamEntity);
                        } else {
                            getView().getDutyEamFailed(scoreDutyEamEntity.errMsg);
                        }
                    }
                }));
//        mCompositeSubscription.add(ScoreHttpClient.getDutyEam(staffId, scoreType)
//                .onErrorReturn(throwable -> {
//                    CommonListEntity commonListEntity = new CommonListEntity();
//                    commonListEntity.errMsg = throwable.toString();
//                    return commonListEntity;
//                })
//                .subscribe(new Consumer<CommonListEntity<ScoreDutyEamEntity>>() {
//                    @Override
//                    public void accept(CommonListEntity<ScoreDutyEamEntity> scoreDutyEamEntityCommonListEntity) throws Exception {
//                        if (TextUtils.isEmpty(scoreDutyEamEntityCommonListEntity.errMsg)) {
//                            getView().getDutyEamSuccess(scoreDutyEamEntityCommonListEntity);
//                        } else {
//                            getView().getDutyEamFailed(scoreDutyEamEntityCommonListEntity.errMsg);
//                        }
//                    }
//                }));
    }

    @Override
    public void getInspectorStaffScore(Long staffId,Long scoreId) {
        List<String> urls = new ArrayList<>();
        //设备责任到人
        urls.add("/BEAM/patrolWorkerScore/workerScoreHead/data-dg1592634082809.action");
        //专业巡检
        urls.add("/BEAM/patrolWorkerScore/workerScoreHead/data-dg1592635533062.action");
        //规范化管理
        urls.add("/BEAM/patrolWorkerScore/workerScoreHead/data-dg1560222990407.action");
        //安全生产
        urls.add("/BEAM/patrolWorkerScore/workerScoreHead/data-dg1560223948889.action");
        //工作表现
        urls.add("/BEAM/patrolWorkerScore/workerScoreHead/data-dg1560224145331.action");
        LinkedHashMap<String, ScoreStaffPerformanceEntity> scoreMap = new LinkedHashMap();
        mCompositeSubscription.add(Flowable.fromIterable(urls)
                .concatMap((Function<String, Flowable<ScoreStaffPerformanceListEntity>>) url -> ScoreHttpClient.getInspectorStaffScore(url, scoreId))
                .onErrorReturn(throwable -> {
                    ScoreStaffPerformanceListEntity scoreStaffPerformanceListEntity = new ScoreStaffPerformanceListEntity();
                    scoreStaffPerformanceListEntity.errMsg = throwable.toString();
                    return scoreStaffPerformanceListEntity;
                })
                .concatMap((Function<ScoreStaffPerformanceListEntity, Publisher<ScoreStaffPerformanceEntity>>) scoreStaffPerformanceListEntity -> {
                    if (scoreStaffPerformanceListEntity.result != null) {
                        return Flowable.fromIterable(scoreStaffPerformanceListEntity.result);
                    }
                    return Flowable.fromIterable(new ArrayList<>());
                })
                .subscribe(scoreStaffPerformanceEntity -> {
                    if (!TextUtils.isEmpty(scoreStaffPerformanceEntity.project) && !scoreStaffPerformanceEntity.project.equals(category)) {
                        position = 0;
                        category = scoreStaffPerformanceEntity.project;
                        scorePerformanceTitleEntity = new ScoreStaffPerformanceEntity();
                        scorePerformanceTitleEntity.project = scoreStaffPerformanceEntity.project;
                        scorePerformanceTitleEntity.score = scoreStaffPerformanceEntity.score;
                        scorePerformanceTitleEntity.fraction = scoreStaffPerformanceEntity.fraction;
                        scorePerformanceTitleEntity.viewType = ListType.TITLE.value();
                        scoreMap.put(scorePerformanceTitleEntity.project, scorePerformanceTitleEntity);
                    }
                    position++;
                    scoreStaffPerformanceEntity.index = position;
                    scoreStaffPerformanceEntity.viewType = ListType.CONTENT.value();
                    if (scorePerformanceTitleEntity != null) {
                        scorePerformanceTitleEntity.scorePerformanceEntities.add(scoreStaffPerformanceEntity);
                        scoreStaffPerformanceEntity.scoreEamPerformanceEntity = scorePerformanceTitleEntity;
                    }
                    scoreMap.put(scoreStaffPerformanceEntity.item, scoreStaffPerformanceEntity);
                }, throwable -> {
                    getView().getInspectorStaffScoreFailed(throwable.toString());
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        ScoreInspectorStaffPerformancePresenter.this.getView().getInspectorStaffScoreSuccess(new ArrayList<>(scoreMap.values()));

                        if (staffId != -1){
                            getDutyEam(staffId, ScoreConstant.ScoreType.INSPECTION_STAFF);
                        }
                    }
                }));
    }
}
