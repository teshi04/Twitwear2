package jp.tsur.twitwear;


import android.content.Context;

import rx.Observable;
import rx.Subscriber;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class Twitter {

    public static Observable<ResponseList<Status>> getHomeTimeline(final Context context) {
        return Observable.create(new Observable.OnSubscribe<ResponseList<Status>>() {
            @Override
            public void call(Subscriber<? super ResponseList<Status>> subscriber) {
                try {
                    Paging paging = new Paging();
                    paging.setCount(30);
                    subscriber.onNext(getTwitterInstance(context).getHomeTimeline(paging));
                    subscriber.onCompleted();
                } catch (TwitterException e) {
                    subscriber.onError(e);
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
