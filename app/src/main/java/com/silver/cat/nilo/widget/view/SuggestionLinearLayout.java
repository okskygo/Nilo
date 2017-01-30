package com.silver.cat.nilo.widget.view;

import android.content.Context;
import android.graphics.Typeface;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.location.places.AutocompletePrediction;

import java.util.List;

/**
 * Created by xiezhenyu on 2017/1/30.
 */

public class SuggestionLinearLayout extends LinearLayout {

    public SuggestionLinearLayout(Context context) {
        super(context);
    }

    public SuggestionLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SuggestionLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void bindSuggestion(List<AutocompletePrediction> predictionList) {
        removeAllViews();
        for (AutocompletePrediction autocompletePrediction : predictionList) {
            TextView textView = new TextView(getContext());
            textView.setText(autocompletePrediction.getPrimaryText(new StyleSpan(Typeface.BOLD)));
            addView(textView);
        }
    }

}
