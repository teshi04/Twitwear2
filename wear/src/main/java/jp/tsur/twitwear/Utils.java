package jp.tsur.twitwear;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

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

    public static String getRelativeTime(Date date) {
        int diff = (int) (((new Date()).getTime() - date.getTime()) / 1000);
        if (diff < 1) {
            return "now";
        } else if (diff < 60) {
            return diff + "s";
        } else if (diff < 3600) {
            return (diff / 60) + "m";
        } else if (diff < 86400) {
            return (diff / 3600) + "h";
        } else {
            return (diff / 86400) + "d";
        }
    }
}
