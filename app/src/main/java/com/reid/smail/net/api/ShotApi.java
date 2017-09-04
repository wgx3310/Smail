package com.reid.smail.net.api;

import com.reid.smail.model.Comment;
import com.reid.smail.model.Shot;
import com.reid.smail.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
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
    @GET("shots")
    Call<List<Shot>> getShots(@Query("access_token") String accessToken,
                              @Query("list") String list,
                              @Query("timeframe") String timeFrame,
                              @Query("sort") String sort,
                              @Query("page") int page);

    /**
     * 获取一个用户的shot
     * @param user 用户类型   user是自己   users是其它用户
     * @param id 用户id   如果是自己的  给null
     * @param accessToken
     * @param page 页码
     */
    @GET("{user}/{id}/shots")
    Call<List<Shot>> getUserShots(@Path("user") String user,
                                  @Path("id") long id,
                                  @Query("access_token") String accessToken,
                                  @Query("page") int page);

    /**
     * 获取一个shot下的评论列表
     *
     * @param id 这条shot的ID
     * @param accessToken token  默认值为公用token
     * @param page 获取哪一页  默认为空，评论列表应为dribbble评论偏少的缘故，不做上拉加载
     * @param perPage 一页
     */
    @GET("shots/{id}/comments")
    Call<List<Comment>> getShotComments(@Path("id") long id,
                                        @Query("access_token") String accessToken,
                                        @Query("page") int page,
                                        @Query("per_page") int perPage);

    @GET("user")
    Call<User> getUserInfo(@Query("access_token") String accessToken);
}
