package com.mimteam.mimclient.util.validors;

import com.mimteam.mimclient.R;

public class NonEmptyValidator extends Validator {
    @Override
    public boolean validate(String data) {
        return data != null && data.length() > 0;
    }

    @Override
    public int getErrorMessageId() {
        return R.string.empty_field_error;
    }
}
