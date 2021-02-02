package com.mimteam.mimclient.util.validators;

import com.mimteam.mimclient.util.validators.schemes.ValidationScheme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TextValidator {
    private final List<ValidationScheme> validationSchemes = new ArrayList<>();

    private String errorMessage;

    public void with(ValidationScheme... schemes) {
        Collections.addAll(validationSchemes, schemes);
    }

    public boolean validate(String text) {
        for (ValidationScheme validationScheme : validationSchemes) {
            if (!validationScheme.validate(text)) {
                errorMessage = validationScheme.getErrorMessage();
                return false;
            }
        }
        return true;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
