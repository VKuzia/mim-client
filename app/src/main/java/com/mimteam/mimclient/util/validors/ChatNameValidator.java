package com.mimteam.mimclient.util.validors;

import com.mimteam.mimclient.R;

public class ChatNameValidator extends Validator {
    @Override
    public boolean validate(String data) {
        return data != null && data.length() > 0;
    }

    @Override
    public int getErrorMessageId() {
        return R.string.chat_name_validation_error;
    }
}
