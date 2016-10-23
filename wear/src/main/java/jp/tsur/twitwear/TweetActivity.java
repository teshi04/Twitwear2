package jp.tsur.twitwear;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.activity.WearableActivity;
import android.view.View;

import jp.tsur.twitwear.databinding.ActivityTweetBinding;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;
import twitter4j.Status;

public class TweetActivity extends WearableActivity {

    private static final String EXTRA_TEXT = "text";
    private static final String EXTRA_IN_REPLY_TO_STATUS = "in_reply_to_status";

    private Subscription subscription = Subscriptions.empty();
    private String text;

    @NonNull
    public static Intent createIntent(@NonNull Context context, @NonNull String text) {
        Intent intent = new Intent(context, TweetActivity.class);
        intent.putExtra(EXTRA_TEXT, text);
        return intent;
    }

    @NonNull
    public static Intent createIntent(@NonNull Context context,
                                      @NonNull String text, @NonNull Status inReplyToStatus) {
        Intent intent = new Intent(context, TweetActivity.class);
        intent.putExtra(EXTRA_TEXT, text);
        intent.putExtra(EXTRA_IN_REPLY_TO_STATUS, inReplyToStatus);
        return intent;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTweetBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_tweet);

        Intent intent = getIntent();
        text = intent.getStringExtra(EXTRA_TEXT);

        final Status inReplyToStatus = (Status) intent.getSerializableExtra(EXTRA_IN_REPLY_TO_STATUS);
        if (inReplyToStatus != null) {
            text = "@" + inReplyToStatus.getUser().getScreenName() + " " + text;
        }
        binding.text.setText(text);

        binding.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatus(text, inReplyToStatus != null ? inReplyToStatus.getId() : 0);
            }
        });
        binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }

    private void updateStatus(@NonNull String text, long inReplyToStatusId) {
        if (inReplyToStatusId != 0) {
            subscription = Twitter.updateStatus(this, text, inReplyToStatusId)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Status>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            startConfirmationActivity(ConfirmationActivity.FAILURE_ANIMATION, getString(R.string.tweet_failure));
                        }

                        @Override
                        public void onNext(Status status) {
                            startConfirmationActivity(ConfirmationActivity.SUCCESS_ANIMATION, getString(R.string.tweet_success));
                        }
                    });
        } else {
            subscription = Twitter.updateStatus(this, text)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Status>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            startConfirmationActivity(ConfirmationActivity.FAILURE_ANIMATION, getString(R.string.tweet_failure));
                        }

                        @Override
                        public void onNext(Status status) {
                            startConfirmationActivity(ConfirmationActivity.SUCCESS_ANIMATION, getString(R.string.tweet_success));
                        }
                    });
        }
    }

    private void startConfirmationActivity(int animationType, String message) {
        Intent confirmationActivity = new Intent(this, ConfirmationActivity.class)
                .putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, animationType)
                .putExtra(ConfirmationActivity.EXTRA_MESSAGE, message);
        startActivity(confirmationActivity);
        finish();
    }
}
