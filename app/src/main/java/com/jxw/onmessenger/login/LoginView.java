package com.jxw.onmessenger.login;

public interface LoginView {
    void onLoginSuccess();
    void handleError(String message);
}
