package com.mimteam.mimclient.components.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mimteam.mimclient.R;

public class NamedEditText extends androidx.appcompat.widget.AppCompatEditText {
    private String fieldName;

    public NamedEditText(@NonNull Context context) {
        super(context);
    }

    public NamedEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initFieldName(context, attrs);
    }

    public NamedEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFieldName(context, attrs);
    }

    private void initFieldName(Context context, AttributeSet attrs) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.NamedEditText, 0, 0);
        try {
            fieldName = attributes.getString(R.styleable.NamedEditText_field_name);
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

    public String getFieldName() {
        return fieldName;
    }
}
