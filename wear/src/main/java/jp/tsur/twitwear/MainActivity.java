package jp.tsur.twitwear;

import android.app.FragmentManager;
import android.app.RemoteInput;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.input.RemoteInputIntent;
import android.support.wearable.view.drawer.WearableActionDrawer;
import android.support.wearable.view.drawer.WearableNavigationDrawer;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;

import jp.tsur.twitwear.databinding.ActivityMainBinding;

public class MainActivity extends WearableActivity implements WearableActionDrawer.OnMenuItemClickListener {

    private static final int REQUEST_REMOTE_INPUT = 0;
    private static final String KEY_REMOTE_INPUT = "remote_input";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.navigationDrawer.setAdapter(new NavigationAdapter());
        binding.drawerLayout.peekDrawer(Gravity.TOP);

        binding.actionDrawer.setOnMenuItemClickListener(this);
        binding.drawerLayout.peekDrawer(Gravity.BOTTOM);

        getFragmentManager().beginTransaction()
                .replace(R.id.content_container, new HomeFragment()).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_REMOTE_INPUT) {
            Bundle results = RemoteInput.getResultsFromIntent(data);
            String text = results.getCharSequence(KEY_REMOTE_INPUT).toString();
            if (!TextUtils.isEmpty(text)) {
                startActivity(TweetActivity.createIntent(this, text));
            }
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_tweet) {
            RemoteInput remoteInput = new RemoteInput.Builder(KEY_REMOTE_INPUT)
                    .setLabel(getString(R.string.main_remote_input_label))
                    .build();
            RemoteInput[] remoteInputs = new RemoteInput[]{remoteInput};

            Intent intent = new Intent(RemoteInputIntent.ACTION_REMOTE_INPUT);
            intent.putExtra(RemoteInputIntent.EXTRA_REMOTE_INPUTS, remoteInputs);
            startActivityForResult(intent, REQUEST_REMOTE_INPUT);
            return true;
        }
        return false;
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
            FragmentManager fragmentManager = getFragmentManager();
            switch (position) {
                case 0:
                    fragmentManager.beginTransaction().replace(
                            R.id.content_container, new HomeFragment()).commit();
                case 1:
                    // TODO: NotificationFragment
                    fragmentManager.beginTransaction().replace(
                            R.id.content_container, new HomeFragment()).commit();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
