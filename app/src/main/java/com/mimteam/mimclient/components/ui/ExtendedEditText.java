package com.mimteam.mimclient.components.ui;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mimteam.mimclient.util.validors.Validator;

import org.jetbrains.annotations.NotNull;

public class ExtendedEditText extends androidx.appcompat.widget.AppCompatEditText {

    public ExtendedEditText(@NonNull Context context) {
        super(context);
    }

    public ExtendedEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ExtendedEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void validate(@NotNull Validator validator) {
        if (!validator.validate(getText().toString())) {
            setError(validator.getErrorMessage());
        }
    }
}
