package com.mimteam.mimclient.util.validators.schemes;

import com.mimteam.mimclient.R;

public class NonEmptyValidationScheme extends ValidationScheme {
    @Override
    public boolean validate(String text) {
        return text != null && text.length() > 0;
    }

    @Override
    public int getErrorMessageId() {
        return R.string.non_empty_validation_error;
    }
}
