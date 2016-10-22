package jp.tsur.twitwear;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterSession;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TwitterSession activeSession = Twitter.getSessionManager().getActiveSession();
        if (activeSession == null) {
            goToLoginScreen();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            Twitter.logOut();
            goToLoginScreen();
            // TODO: wear のほうもログアウトしたほうがよさそう
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        TaskStackBuilder builder = TaskStackBuilder.create(this);
        builder.addNextIntent(intent);
        builder.startActivities();
    }
}
