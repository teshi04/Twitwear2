package jp.tsur.twitwear;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.drawer.WearableNavigationDrawer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import jp.tsur.twitwear.databinding.ActivityMainBinding;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;
import twitter4j.ResponseList;
import twitter4j.Status;

public class MainActivity extends WearableActivity {

    private Subscription subscription = Subscriptions.empty();
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.navigationDrawer.setAdapter(new NavigationAdapter());
        binding.drawerLayout.peekDrawer(Gravity.TOP);

        binding.list.setLayoutManager(new LinearLayoutManager(this));
        binding.list.hasFixedSize();
        binding.list.addItemDecoration(new DividerDecoration(this));

        getTimeLine();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }

    private void getTimeLine() {
        subscription = Twitter.getHomeTimeline(this)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseList<Status>>() {
                    @Override
                    public void onCompleted() {
                        Log.d("MainActivity", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        binding.progress.setVisibility(View.GONE);
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ResponseList<Status> statuses) {
                        binding.progress.setVisibility(View.GONE);
                        binding.list.setVisibility(View.VISIBLE);
                        StatusAdapter adapter = new StatusAdapter(statuses);
                        binding.list.setAdapter(adapter);
                    }
                });
    }

    private class NavigationAdapter extends WearableNavigationDrawer.WearableNavigationDrawerAdapter {

        @Override
        public String getItemText(int position) {
            switch (position) {
                case 0:
                    return "Home";
                case 1:
                    return "Notification";
            }
            return null;
        }

        @Override
        public Drawable getItemDrawable(int position) {
            switch (position) {
                case 0:
                    return getDrawable(R.drawable.home_vector);
                case 1:
                    return getDrawable(R.drawable.notification_vector);
            }
            return null;
        }

        @Override
        public void onItemSelected(int position) {

        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
