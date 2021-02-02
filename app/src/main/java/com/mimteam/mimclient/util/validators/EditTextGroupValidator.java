package com.mimteam.mimclient.util.validators;

import com.google.common.collect.LinkedHashMultimap;
import com.mimteam.mimclient.components.ui.NamedEditText;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class EditTextGroupValidator {
    private final LinkedHashMultimap<NamedEditText, TextValidator> editToValidator = LinkedHashMultimap.create();

    public TextValidator setupEditTextValidation(NamedEditText editText) {
        TextValidator validator = new TextValidator();
        editToValidator.put(editText, validator);
        return validator;
    }

    public boolean validate() {
        boolean hasError = false;
        for (Map.Entry<NamedEditText, TextValidator> editValidator : editToValidator.entries()) {
            String text = editValidator.getKey().getStringValue();
            if (editValidator.getValue().validate(text)) {
                continue;
            }

            setEditError(editValidator.getKey(), editValidator.getValue().getErrorMessage());
            if (!hasError) {
                editValidator.getKey().requestFocus();
                hasError = true;
            }
        }
        return !hasError;
    }

    private void setEditError(@NotNull NamedEditText editText, String errorMessage) {
        editText.setError(editText.getFieldName() + " " + errorMessage);
    }
}
