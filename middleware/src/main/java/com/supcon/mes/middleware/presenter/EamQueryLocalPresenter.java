package com.supcon.mes.middleware.presenter;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.bean.ContactEntity;
import com.supcon.mes.middleware.model.bean.ContactEntityDao;
import com.supcon.mes.middleware.model.bean.EamEntity;
import com.supcon.mes.middleware.model.bean.EamEntityDao;
import com.supcon.mes.middleware.model.contract.EamQueryLocalContract;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by zws on 2020/5/28
 * Email:zhangwenshuai1@supcom.com
 */
public class EamQueryLocalPresenter extends EamQueryLocalContract.Presenter {

    @SuppressLint("CheckResult")
    @Override
    public void listEamLocal(int pageNo, int pageSize, String search, String other) {
        Flowable.timer(100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        QueryBuilder<EamEntity> queryBuilder = EamApplication.dao().getEamEntityDao().queryBuilder();
                        List<EamEntity> eamEntityList;

                        if (!TextUtils.isEmpty(other)) {
                            if (TextUtils.isEmpty(search)) {
                                eamEntityList = queryBuilder
                                        .orderAsc(EamEntityDao.Properties.Name)
                                        .offset((pageNo - 1) * pageSize)
                                        .limit(pageSize)
                                        .list();
                            } else {
                                eamEntityList = queryBuilder
                                        .whereOr(EamEntityDao.Properties.Name.like("%" + search + "%"),
                                                EamEntityDao.Properties.Code.like("%" + search + "%"),
                                                EamEntityDao.Properties.EamAssetCode.like("%" + search + "%"))
                                        .orderAsc(EamEntityDao.Properties.Name)
                                        .offset((pageNo - 1) * pageSize)
                                        .limit(pageSize)
                                        .list();
                            }
                        } else {
                            if (!TextUtils.isEmpty(search)) {
                                eamEntityList = queryBuilder
                                        .whereOr(EamEntityDao.Properties.Name.like("%" + search + "%"),
                                                EamEntityDao.Properties.Code.like("%" + search + "%"),
                                                EamEntityDao.Properties.EamAssetCode.like("%" + search + "%"))
                                        .orderAsc(EamEntityDao.Properties.Name)
                                        .offset((pageNo - 1) * pageSize)
                                        .limit(pageSize)
                                        .list();
                            } else {
                                eamEntityList = queryBuilder
                                        .orderAsc(EamEntityDao.Properties.Name)
                                        .offset((pageNo - 1) * pageSize)
                                        .limit(pageSize)
                                        .list();
                            }
                        }
                        LogUtil.d("recentEamEntities size:" + eamEntityList.size());
                        getView().listEamLocalSuccess(eamEntityList);
                    }
                });
    }
}
