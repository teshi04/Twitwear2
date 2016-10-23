package jp.tsur.twitwear;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.activity.WearableActivity;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;


public class LoginActivity extends WearableActivity
        implements GoogleApiClient.ConnectionCallbacks {

    private static final String PATH_OPEN_APP = "/open_app";

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.open_phone_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOnPhone();
            }
        });

        setupClient();
    }

    private void setupClient() {
        client = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .build();
        if (!client.isConnected()) {
            client.connect();
        }
    }

    private void openOnPhone() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(client).await();
                for (Node node : nodes.getNodes()) {
                    Wearable.MessageApi.sendMessage(
                            client, node.getId(), PATH_OPEN_APP,
                            new byte[]{}).setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                        @Override
                        public void onResult(@NonNull MessageApi.SendMessageResult sendMessageResult) {
                            if (sendMessageResult.getStatus().isSuccess()) {
                                Intent confirmationActivity = new Intent(LoginActivity.this, ConfirmationActivity.class)
                                        .putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.OPEN_ON_PHONE_ANIMATION)
                                        .putExtra(ConfirmationActivity.EXTRA_MESSAGE, getString(R.string.open_on_phone));
                                startActivity(confirmationActivity);
                                finish();
                            }
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
