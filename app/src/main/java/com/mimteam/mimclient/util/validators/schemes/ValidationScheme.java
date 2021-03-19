package com.mimteam.mimclient.util.validators.schemes;

import com.mimteam.mimclient.App;

public abstract class ValidationScheme {
    public abstract boolean validate(String text);
    public abstract int getErrorMessageId();

    public String getErrorMessage() {
        return App.getAppContext().getString(getErrorMessageId());
    }
}
