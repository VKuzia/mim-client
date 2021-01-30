package com.mimteam.mimclient.components.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mimteam.mimclient.R;
import com.mimteam.mimclient.util.validors.Validator;

import org.jetbrains.annotations.NotNull;

public class ExtendedEditText extends androidx.appcompat.widget.AppCompatEditText {
    private String fieldName;

    public ExtendedEditText(@NonNull Context context) {
        super(context);
    }

    public ExtendedEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initFieldName(context, attrs);
    }

    public ExtendedEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFieldName(context, attrs);
    }

    private void initFieldName(Context context, AttributeSet attrs) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ExtendedEditText, 0, 0);
        try {
            fieldName = attributes.getString(R.styleable.ExtendedEditText_field_name);
        } finally {
            attributes.recycle();
        }
    }

    public String getStringValue() {
        if (getText() == null) {
            return null;
        }
        return getText().toString();
    }

    public void validate(@NotNull Validator validator) {
        if (!validator.validate(getStringValue())) {
            setError(fieldName + " " + validator.getErrorMessage());
        }
    }
}
