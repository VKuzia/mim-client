package com.mimteam.mimclient.util.validors;

import com.mimteam.mimclient.R;

public class LoginValidator extends Validator {
    @Override
    public boolean validate(String data) {
        return data != null && data.length() > 0;
    }

    @Override
    public int getErrorMessageId() {
        return R.string.login_validation_error;
    }
}
