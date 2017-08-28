package com.reid.smail.net.api;

import com.reid.smail.model.Shot;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by reid on 2017/8/26.
 */

public interface ShotApi {
    /**
     * 获取一个shot列表
     *
     * @param accessToken token  默认值为公用token
     * @param list  列表类型，比如带GIF的，团队的  animated ，attachments ，debuts ，playoffs，rebounds，teams
     * @param sort 排序 comments评论最多的，recent 最近的，views查看最多的
     * @param timeFrame 时间   week一周，month一个月，year一键，ever无论何时
     *
     * @return List<Shot>
     */
//    fun getShots(@NotNull access_token: String,
//                 list: String?,
//                 timeframe: String?,
//                 sort: String?,
//                 page: Int?,
//                 subscriber: NetSubscriber<MutableList<Shot>>): Subscription
    @GET("shots")
    Call<List<Shot>> getShots(@Query("access_token") String accessToken,
                              @Query("list") String list,
                              @Query("timeframe") String timeFrame,
                              @Query("sort") String sort,
                              @Query("page") int page);

}
