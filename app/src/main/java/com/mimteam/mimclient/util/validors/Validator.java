package com.mimteam.mimclient.util.validors;

import com.mimteam.mimclient.App;

public abstract class Validator {
    public abstract boolean validate(String data);
    public abstract int getErrorMessageId();

    public String getErrorMessage() {
        return App.getAppContext().getString(getErrorMessageId());
    }
}
