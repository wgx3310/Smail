package com.reid.smail.net.loader;

import com.reid.smail.content.AccountManager;
import com.reid.smail.content.Constant;
import com.reid.smail.model.Item;
import com.reid.smail.model.shot.Bucket;
import com.reid.smail.model.shot.Comment;
import com.reid.smail.model.shot.Like;
import com.reid.smail.model.shot.Shot;
import com.reid.smail.model.shot.User;
import com.reid.smail.net.api.ShotApi;
import com.reid.smail.net.client.Loader;

import java.util.List;

import io.reactivex.Observable;


/**
 * Created by reid on 2017/10/16.
 */

public class ShotLoader extends Loader<ShotApi> {

    private ShotLoader(){

    }

    private static class Internal{
        private static final ShotLoader instance = new ShotLoader();
    }

    public static ShotLoader get(){
        return Internal.instance;
    }

    public Observable<List<Shot>> getShots(String list, String sort, int page){
        return Impl().getShots(Constant.ACCESS_TOKEN, list, null, sort, page)
                .compose(this.<List<Shot>>transformer());
    }

    public Observable<List<Shot>> getUserShots(long userId, int page){
        return Impl().getUserShots("users", userId, Constant.ACCESS_TOKEN, page)
                .compose(this.<List<Shot>>transformer());
    }

    public Observable<List<Comment>> getShotComments(long shotId, int page){
        return Impl().getShotComments(shotId, Constant.ACCESS_TOKEN, page, 100)
                .compose(this.<List<Comment>>transformer());
    }

    public Observable<User> getMyInfo(String accessToken){
        return Impl().getMyInfo(accessToken)
                .compose(this.<User>transformer());
    }

    public Observable<Item> likeShot(long shotId){
        return Impl().likeShot(shotId, AccountManager.get().getAccessToken())
                .compose(this.<Item>transformer());
    }

    public Observable<Item> unlikeShot(long shotId){
        return Impl().unlikeShot(shotId, AccountManager.get().getAccessToken())
                .compose(this.<Item>transformer());
    }

    public Observable<Item> checkShotLiked(long shotId){
        return Impl().checkShotLiked(shotId, AccountManager.get().getAccessToken())
                .compose(this.<Item>transformer());
    }

    public Observable<Item> likeComment(long shotId, long commentId){
        return Impl().likeComment(shotId, commentId, AccountManager.get().getAccessToken())
                .compose(this.<Item>transformer());
    }

    public Observable<Item> unlikeComment(long shotId, long commentId){
        return Impl().unlikeComment(shotId, commentId, AccountManager.get().getAccessToken())
                .compose(this.<Item>transformer());
    }

    public Observable<Item> checkCommentLiked(long shotId, long commentId){
        return Impl().checkCommentLiked(shotId, commentId, AccountManager.get().getAccessToken())
                .compose(this.<Item>transformer());
    }

    public Observable<Comment> createComment(long shotId, String text){
        return Impl().createComment(shotId, AccountManager.get().getAccessToken(), text)
                .compose(this.<Comment>transformer());
    }

    public Observable<List<Bucket>> getMyBuckets(){
        return Impl().getMyBuckets(AccountManager.get().getAccessToken())
                .compose(this.<List<Bucket>>transformer());
    }

    public Observable<List<Shot>> getBucketShots(long bucketId){
        return Impl().getBucketShots(bucketId, AccountManager.get().getAccessToken())
                .compose(this.<List<Shot>>transformer());
    }

    public Observable<List<Like>> getMyLikes(){
        return Impl().getMyLikes(AccountManager.get().getAccessToken())
                .compose(this.<List<Like>>transformer());
    }

    @Override
    public String getBaseUrl() {
        return Constant.BASE_URL_DESIGN;
    }
}
