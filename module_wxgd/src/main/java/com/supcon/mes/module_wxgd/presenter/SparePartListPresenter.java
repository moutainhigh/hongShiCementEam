package com.supcon.mes.module_wxgd.presenter;

import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.ResultEntity;
import com.supcon.mes.middleware.model.bean.StandingCropEntity;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;
import com.supcon.mes.module_wxgd.model.contract.SparePartListContract;
import com.supcon.mes.module_wxgd.model.network.HttpClient;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * SparePartListPresenter 表体备件列表Presenter
 * created by zhangwenshuai1 2018/10/10
 */
public class SparePartListPresenter extends SparePartListContract.Presenter {

    @Override
    public void updateStandingCrop(String productCode) {
        Flowable<CommonListEntity<StandingCropEntity>> commonListEntityFlowable;
//        if (EamApplication.isHongshi()) {
            commonListEntityFlowable = MiddlewareHttpClient.updateStandingCrop(productCode);
//        } else {
//            commonListEntityFlowable = HttpClient.updateStandingCrop(productCode);
//        }
        mCompositeSubscription.add(
                commonListEntityFlowable
                        .onErrorReturn(new Function<Throwable, CommonListEntity<StandingCropEntity>>() {
                            @Override
                            public CommonListEntity<StandingCropEntity> apply(Throwable throwable) throws Exception {
                                CommonListEntity resultEntity = new CommonListEntity();
                                resultEntity.success = false;
                                resultEntity.errMsg = throwable.toString();
                                return resultEntity;
                            }
                        })
                        .subscribe(new Consumer<CommonListEntity<StandingCropEntity>>() {
                            @Override
                            public void accept(CommonListEntity<StandingCropEntity> resultEntity) throws Exception {
                                if (resultEntity.success) {
                                    getView().updateStandingCropSuccess(resultEntity);
                                } else {
                                    getView().updateStandingCropFailed(resultEntity.errMsg);
                                }
                            }
                        }));
    }

    @Override
    public void generateSparePartApply(String listStr) {

//        Map<String, RequestBody> formBody = FormDataHelper.createDataFormBody(map);
        mCompositeSubscription.add(
                HttpClient.generateSparePartApply(listStr)
                        .onErrorReturn(new Function<Throwable, ResultEntity>() {
                            @Override
                            public ResultEntity apply(Throwable throwable) throws Exception {
                                ResultEntity resultEntity = new ResultEntity();
                                resultEntity.success = false;
                                resultEntity.errMsg = throwable.toString();
                                return resultEntity;
                            }
                        }).subscribe(new Consumer<ResultEntity>() {
                    @Override
                    public void accept(ResultEntity resultEntity) throws Exception {
                        if (resultEntity.success) {
                            getView().generateSparePartApplySuccess(resultEntity);
                        } else {
                            getView().generateSparePartApplyFailed(resultEntity.errMsg);
                        }
                    }
                })
        );
    }
}
