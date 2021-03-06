package com.supcon.mes.module_overhaul_workticket.model.network;

import com.app.annotation.apt.ApiFactory;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.module_overhaul_workticket.model.bean.SafetyMeasuresList;
import com.supcon.mes.module_overhaul_workticket.model.bean.WorkTicketList;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/12/11
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
@ApiFactory()
public interface ApiService {

    /**
     * 获取检修工作票安全措施list
     * @param workTicketId
     * @return
     */
    @POST("/WorkTicket/workTicket/ohworkticket/data-dg1575615975095.action?datagridCode=WorkTicket_8.20.3.03_workTicket_workTicketEditdg1575615975095&rt=json")
    Flowable<SafetyMeasuresList> listSafetyMeasures(@Query(value = "ohworkticket.id") Long workTicketId);

    /**
     * 检修作业票list
     * @param url
     * @param pageQueryMap
     * @return
     */
    @POST()
    Flowable<WorkTicketList> workTicketList(@Url String url, @QueryMap Map<String, Object> pageQueryMap);

    /**
     * 检修作业票submit
     * @param paramsMap
     * @param __pc__
     * @param view 视图
     * @return
     */
    @POST("/WorkTicket/workTicket/ohworkticket/{view}/submit.action?_bapFieldPermissonModelCode_=WorkTicket_8.20.3.03_workTicket_Ohworkticket&_bapFieldPermissonModelName_=Ohworkticket&superEdit=false")
    @Multipart
    Flowable<BapResultEntity> submit(@Path(value = "view") String view, @PartMap Map<String, RequestBody> paramsMap, @Part List<MultipartBody.Part> partList, @Query("__pc__") String __pc__);

    /**
     * 停电弃审
     * @param offApplyTableNo
     * @return
     */
//    @POST("/BEAMEle/onOrOff/onoroff/retrial.action?scriptCode=&__pc__=QkVBTUVsZV8xLjAuMF9vbk9yT2ZmX3JldHJpYWx8")
    @POST("/BEAMEle/onOrOff/onoroff/checkPendindIsExists.action")
    Flowable<CommonEntity<Boolean>> retrial(@Query(value = "offApplyTableNo") String offApplyTableNo);
}
