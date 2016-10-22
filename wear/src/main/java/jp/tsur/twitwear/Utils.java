package jp.tsur.twitwear;

import android.content.Context;
import android.content.SharedPreferences;

import twitter4j.auth.AccessToken;


public class Utils {

    private static final String PREF_NAME = "twitter_access_token";
    private static final String TOKEN = "token";
    private static final String TOKEN_SECRET = "token_secret";

    public static void saveAccessToken(Context context, String token, String tokenSecret) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(TOKEN, token);
        editor.putString(TOKEN_SECRET, tokenSecret);
        editor.apply();
    }

    public static AccessToken loadAccessToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        String token = preferences.getString(TOKEN, null);
        String tokenSecret = preferences.getString(TOKEN_SECRET, null);
        if (token != null && tokenSecret != null) {
            return new AccessToken(token, tokenSecret);
        } else {
            return null;
        }
    }
}
