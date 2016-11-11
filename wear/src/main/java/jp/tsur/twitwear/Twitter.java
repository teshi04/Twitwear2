package jp.tsur.twitwear;


import android.content.Context;
import android.support.annotation.NonNull;

import rx.Single;
import rx.SingleSubscriber;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class Twitter {

    public static Single<ResponseList<Status>> getHomeTimeline(final Context context) {
        return Single.create(new Single.OnSubscribe<ResponseList<Status>>() {
            @Override
            public void call(SingleSubscriber<? super ResponseList<Status>> singleSubscriber) {
                try {
                    Paging paging = new Paging();
                    paging.setCount(30);
                    singleSubscriber.onSuccess(getTwitterInstance(context).getHomeTimeline(paging));
                } catch (TwitterException e) {
                    singleSubscriber.onError(e);
                }
            }
        });
    }

    /**
     * @param context
     * @param statusText
     * @param inReplyToStatusId when not reply 0
     * @return
     */
    public static Single<Status> updateStatus(
            final Context context, @NonNull final String statusText, final long inReplyToStatusId) {
        return Single.create(new Single.OnSubscribe<Status>() {
            @Override
            public void call(SingleSubscriber<? super Status> singleSubscriber) {
                try {
                    StatusUpdate statusUpdate = new StatusUpdate(statusText);
                    if (inReplyToStatusId != 0) {
                        statusUpdate.setInReplyToStatusId(inReplyToStatusId);
                    }
                    singleSubscriber.onSuccess(getTwitterInstance(context).updateStatus(statusUpdate));
                } catch (TwitterException e) {
                    singleSubscriber.onError(e);
                }
            }
        });
    }

    public static Single<Status> createFavorite(
            final Context context, final long statusId) {
        return Single.create(new Single.OnSubscribe<Status>() {
            @Override
            public void call(SingleSubscriber<? super Status> singleSubscriber) {
                try {
                    singleSubscriber.onSuccess(getTwitterInstance(context).createFavorite(statusId));
                } catch (TwitterException e) {
                    singleSubscriber.onError(e);
                }
            }
        });
    }

    public static Single<Status> retweetStatus(
            final Context context, final long statusId) {
        return Single.create(new Single.OnSubscribe<Status>() {
            @Override
            public void call(SingleSubscriber<? super Status> singleSubscriber) {
                try {
                    singleSubscriber.onSuccess(getTwitterInstance(context).retweetStatus(statusId));
                } catch (TwitterException e) {
                    singleSubscriber.onError(e);
                }
            }
        });
    }

    private static twitter4j.Twitter getTwitterInstance(Context context) {
        TwitterFactory factory = new TwitterFactory();
        twitter4j.Twitter twitter = factory.getInstance();
        twitter.setOAuthConsumer(BuildConfig.TWITTER_KEY, BuildConfig.TWITTER_SECRET);
        AccessToken accessToken = Utils.loadAccessToken(context);
        if (accessToken != null) {
            twitter.setOAuthAccessToken(accessToken);
        }
        // TODO: ログインしていないときなんとかする
        return twitter;
    }
}
