package com.reid.smail.net.api;

import com.reid.smail.model.Item;
import com.reid.smail.model.shot.Bucket;
import com.reid.smail.model.shot.Comment;
import com.reid.smail.model.shot.Like;
import com.reid.smail.model.shot.Shot;
import com.reid.smail.model.shot.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

    /**
     * 获取登录用户信息
     * @param accessToken 登录用户的token
     * @return
     */
    @GET("user")
    Call<User> getMyInfo(@Query("access_token") String accessToken);


    /**
     *喜欢一个shot
     * @param id 这条shot的ID
     * @param accessToken 登录用户的token
     * @return
     */
    @FormUrlEncoded
    @POST("shots/{id}/like")
    Call<Item> likeShot(@Path("id") long id,
                        @Field("access_token") String accessToken);

    /**
     * 取消喜欢一个shot
     * @param id 这条shot的ID
     * @param accessToken 登录用户的token
     * @return
     */
    @DELETE("shots/{id}/like")
    Call<Item> unlikeShot(@Path("id") long id,
                          @Query("access_token") String accessToken);

    /**
     * 检查是否喜欢一个shot
     * @param id 这条shot的ID
     * @param accessToken 登录用户的token
     * @return
     */
    @GET("shots/{id}/like")
    Call<Item> checkShotLiked(@Path("id") long id,
                              @Query("access_token") String accessToken);

    /**
     * 喜欢一个comment
     * @param shotId comment所属shot id
     * @param commentId 该comment的id
     * @param accessToken 登录用户的token
     * @return
     */
    @FormUrlEncoded
    @POST("shots/{shot}/comments/{id}/like")
    Call<Item> likeComment(@Path("shot") long shotId,
                           @Path("id") long commentId,
                           @Field("access_token") String accessToken);

    /**
     * 取消喜欢一个comment
     * @param shotId comment所属shot id
     * @param commentId 该comment的id
     * @param accessToken 登录用户的token
     * @return
     */
    @DELETE("shots/{shot}/comments/{id}/like")
    Call<Item> unlikeComment(@Path("shot") long shotId,
                             @Path("id") long commentId,
                             @Query("access_token") String accessToken);

    /**
     * 检查是否喜欢一个comment
     * @param shotId comment所属shot id
     * @param commentId 该comment的id
     * @param accessToken 登录用户的token
     * @return
     */
    @GET("shots/{shot}/comments/{id}/like")
    Call<Item> checkCommentLiked(@Path("shot") long shotId,
                                 @Path("id") long commentId,
                                 @Query("access_token") String accessToken);

    /**
     * 创建一个评论
     * @param shotId comment所属shot id
     * @param accessToken 登录用户的token
     * @param comment 评论内容
     * @return
     */
    @FormUrlEncoded
    @POST("shots/{shot}/comments")
    Call<Comment> createComment(@Path("shot") long shotId,
                                @Field("access_token") String accessToken,
                                @Field("body") String comment);

    /**
     * 获取登录用户的Buckets
     * @param accessToken 登录用户的token
     * @return
     */
    @GET("user/buckets")
    Call<List<Bucket>> getMyBuckets(@Query("access_token") String accessToken);

    /**
     *获取一个Bucket中的Shots
     * @param bucketId bucket的id
     * @param accessToken 登录用户的token
     * @return
     */
    @GET("buckets/{id}/shots")
    Call<List<Shot>> getBucketShots(@Path("id") long bucketId,
                                    @Query("access_token") String accessToken);

    /**
     * 获取登录用户的Likes
     * @param accessToken 登录用户的token
     * @return
     */
    @GET("user/likes")
    Call<List<Like>> getMyLikes(@Query("access_token") String accessToken);
}
