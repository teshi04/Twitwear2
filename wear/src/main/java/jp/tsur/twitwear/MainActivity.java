package jp.tsur.twitwear;

import android.app.FragmentManager;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.drawer.WearableNavigationDrawer;
import android.view.Gravity;

import jp.tsur.twitwear.databinding.ActivityMainBinding;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

public class MainActivity extends WearableActivity {

    private Subscription subscription = Subscriptions.empty();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.navigationDrawer.setAdapter(new NavigationAdapter());
        binding.drawerLayout.peekDrawer(Gravity.TOP);

        getFragmentManager().beginTransaction().replace(R.id.content_container, new HomeFragment()).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
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
                    fragmentManager.beginTransaction().replace(R.id.content_container, new HomeFragment()).commit();
                case 1:
                    // TODO: NotificationFragment
                    fragmentManager.beginTransaction().replace(R.id.content_container, new HomeFragment()).commit();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
