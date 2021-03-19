package com.mimteam.mimclient.util.validators.schemes;

import com.mimteam.mimclient.R;

public class AlphanumericValidationScheme extends ValidationScheme {
    @Override
    public boolean validate(String text) {
        return text != null && text.matches("^[a-zA-Z0-9]*$");
    }

    @Override
    public int getErrorMessageId() {
        return R.string.alphanumeric_validation_error;
    }
}
