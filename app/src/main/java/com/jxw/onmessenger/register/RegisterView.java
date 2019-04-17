package com.jxw.onmessenger.register;

public interface RegisterView {

    /**
     * Called when registration succeeds.
     */
    void onRegistrationSuccess();

    /**
     * Called when registration fails.
     * @param msg error message
     */
    void onRegistrationFailure(String msg);
}
