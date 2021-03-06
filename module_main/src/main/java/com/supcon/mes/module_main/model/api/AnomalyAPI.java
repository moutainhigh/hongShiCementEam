package com.supcon.mes.module_main.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;

import java.util.Map;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/7/24
 * ------------- Description -------------
 */
@ContractFactory(entites = {CommonBAPListEntity.class})
public interface AnomalyAPI {

    void getAnomalyList(int page, int pageSize, Map<String, Object> params);

//    /**
//     * 获取异常记录的当前登录人待办ID
//     * @param currentUserId
//     * @param tableNo
//     */
//    void getCurrentUserPendingId(Long currentUserId, String tableNo);

}
