package com.jxw.onmessenger.settings;

import com.jxw.onmessenger.models.User;

public interface SettingsView {
    void handleError(String message);
    void onFetchProfileSuccess(User user);
    void onUpdateProfileSuccess();
}
