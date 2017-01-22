package com.silver.cat.nilo.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * Created by xiezhenyu on 2017/1/22.
 */

public class KeyPreImeEditText extends EditText {

    private OnKeyPreImgListener listener;

    public KeyPreImeEditText(Context context) {
        super(context);
    }

    public KeyPreImeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyPreImeEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnKeyPreImgListener(OnKeyPreImgListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (listener != null) {
            return listener.onKeyPreIme(keyCode, event);
        }
        return super.onKeyPreIme(keyCode, event);
    }

    public interface OnKeyPreImgListener {
        boolean onKeyPreIme(int keyCode, KeyEvent event);
    }
}
