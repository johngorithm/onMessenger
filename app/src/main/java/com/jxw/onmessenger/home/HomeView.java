package com.jxw.onmessenger.home;

public interface HomeView {
    void handleNetworkRequestError(String message);
    void onGroupCreationSuccess(String groupName);
    void handleUsernameVerificationResult(Boolean isUsernameAvailable);
}
