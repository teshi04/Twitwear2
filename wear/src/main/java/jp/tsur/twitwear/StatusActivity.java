package jp.tsur.twitwear;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.activity.WearableActivity;
import android.view.View;

import com.squareup.picasso.Picasso;

import jp.tsur.twitwear.databinding.ActivityStatusBinding;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import twitter4j.Status;
import twitter4j.User;

public class StatusActivity extends WearableActivity {

    private static final String EXTRA_STATUS = "status";

    private CompositeSubscription subscriptions = new CompositeSubscription();

    @NonNull
    public static Intent createIntent(@NonNull Context context, @NonNull Status status) {
        Intent intent = new Intent(context, StatusActivity.class);
        intent.putExtra(EXTRA_STATUS, status);
        return intent;
    }

    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStatusBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_status);

        final Status status = (Status) getIntent().getSerializableExtra(EXTRA_STATUS);
        User user = status.getUser();
        binding.name.setText(user.getName());
        binding.screenName.setText("@" + user.getScreenName());
        binding.text.setText(status.getText());
        binding.date.setText(Utils.getRelativeTime(status.getCreatedAt()));
        Picasso.with(binding.icon.getContext()).load(user.getProfileImageURL()).fit().into(binding.icon);

        binding.replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        binding.retweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retweet(status.getId());
            }
        });
        binding.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                like(status.getId());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscriptions.unsubscribe();
    }

    private void like(long statusId) {
        Subscription subscription = Twitter.createFavorite(this, statusId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Status>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        startConfirmationActivity(ConfirmationActivity.FAILURE_ANIMATION,
                                getString(R.string.tweet_failure));
                    }

                    @Override
                    public void onNext(Status response) {
                        startConfirmationActivity(ConfirmationActivity.SUCCESS_ANIMATION, getString(R.string.tweet_success));
                    }
                });
        subscriptions.add(subscription);
    }

    private void retweet(long statusId) {
        Subscription subscription = Twitter.retweetStatus(this, statusId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Status>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        startConfirmationActivity(ConfirmationActivity.FAILURE_ANIMATION,
                                getString(R.string.tweet_failure));
                    }

                    @Override
                    public void onNext(Status response) {
                        startConfirmationActivity(ConfirmationActivity.SUCCESS_ANIMATION, getString(R.string.tweet_success));
                    }
                });
        subscriptions.add(subscription);
    }

    private void startConfirmationActivity(int animationType, String message) {
        Intent confirmationActivity = new Intent(this, ConfirmationActivity.class)
                .putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, animationType)
                .putExtra(ConfirmationActivity.EXTRA_MESSAGE, message);
        startActivity(confirmationActivity);
        finish();
    }
}
