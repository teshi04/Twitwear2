package jp.tsur.twitwear;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;

import com.squareup.picasso.Picasso;

import jp.tsur.twitwear.databinding.ActivityStatusBinding;
import twitter4j.Status;
import twitter4j.User;

public class StatusActivity extends WearableActivity {

    private static final String EXTRA_STATUS = "status";

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

        Status status = (Status) getIntent().getSerializableExtra(EXTRA_STATUS);
        User user = status.getUser();
        binding.name.setText(user.getName());
        binding.screenName.setText("@" + user.getScreenName());
        binding.text.setText(status.getText());
        binding.date.setText(Utils.getRelativeTime(status.getCreatedAt()));
        Picasso.with(binding.icon.getContext()).load(user.getProfileImageURL()).fit().into(binding.icon);

        binding.replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("StatusActivity", "a");
            }
        });
    }
}
