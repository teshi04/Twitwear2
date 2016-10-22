package jp.tsur.twitwear;

import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;


public class ListenerService extends WearableListenerService {

    private GoogleApiClient client;

    @Override
    public void onCreate() {
        super.onCreate();
        client = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        client.connect();
        Wearable.MessageApi.addListener(client, this);
        Log.d("ListenerService", "onCreate");
    }


    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        String token = new String(messageEvent.getData());
        Log.d("ListenerService", token);
    }
}
