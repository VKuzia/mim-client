package com.mimteam.mimclient.util.validors;

import com.mimteam.mimclient.R;

public class PasswordValidator extends Validator {
    @Override
    public boolean validate(String data) {
        return data != null && data.length() > 0;
    }

    @Override
    public int getErrorMessageId() {
        return R.string.password_validation_error;
    }
}
