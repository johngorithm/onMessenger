package com.jxw.onmessenger.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.jxw.onmessenger.login.LoginActivity;

public class Redirection {

    private Redirection() {
        // Utility class
    }

    public static void sendUserToLogin(Context context) {
        Intent loginIntent = new Intent(context, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(loginIntent);
        ((Activity) context).finish();
    }
}
