package jp.tsur.twitwear;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;
import twitter4j.ResponseList;
import twitter4j.Status;

public class MainActivity extends WearableActivity {

    private Subscription subscription = Subscriptions.empty();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getTimeLine();
    }

    private void getTimeLine() {
        subscription = Twitter.getHomeTimeline(this)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseList<Status>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ResponseList<Status> statuses) {
                        for (Status status : statuses) {
                            Log.d("MainActivity", status.getText());
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }
}
