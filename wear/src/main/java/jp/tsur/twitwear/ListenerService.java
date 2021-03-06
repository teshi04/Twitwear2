package jp.tsur.twitwear;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;


public class ListenerService extends WearableListenerService {

    private static final String PATH_AUTH_TOKEN = "/auth_token";

    private GoogleApiClient client;

    @Override
    public void onCreate() {
        super.onCreate();
        client = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        client.connect();
        Wearable.MessageApi.addListener(client, this);
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals(PATH_AUTH_TOKEN)) {
            String string = new String(messageEvent.getData());
            String[] token = string.split(",");
            Utils.saveAccessToken(this, token[0], token[1]);
        }
    }
}
