package com.example.harameter.harameter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.view.Gravity;

public class SplashActivity extends AppCompatActivity {

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.
                FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        TextView message = findViewById(R.id.message);
        TextView activityinfo = findViewById(R.id.activityinfo);
        TextView accuracy = findViewById(R.id.accuracy);
        TextView streak = findViewById(R.id.streak);

        String difficulty = getIntent().getStringExtra("DIFFICULTY");
        String method = getIntent().getStringExtra("METHOD");
        email = getIntent().getStringExtra("GOOGLE_EMAIL");
        String accuracyValue = getIntent().getStringExtra("ACCURACY");
        String streakValue = getIntent().getStringExtra("STREAK");

        message.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        activityinfo.setText(method + ", " + difficulty);
        activityinfo.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        SpannableStringBuilder accuracySpannable = new SpannableStringBuilder("Accuracy\n" + accuracyValue + "%");
        accuracySpannable.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                (accuracySpannable.length() - (accuracyValue.length() + 1)),
                accuracySpannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (Integer.valueOf(accuracyValue) >= 70) {
            accuracySpannable.setSpan(new ForegroundColorSpan(0XFF81c784),
                    (accuracySpannable.length() - (accuracyValue.length() + 1)),
                    accuracySpannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            accuracy.setText(accuracySpannable);
            accuracy.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        }
        else if (Integer.valueOf(accuracyValue) >=40) {
            accuracySpannable.setSpan(new ForegroundColorSpan(0XFFffd54f),
                    (accuracySpannable.length() - (accuracyValue.length() + 1)),
                    accuracySpannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            accuracy.setText(accuracySpannable);
            accuracy.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        }
        else {
            accuracySpannable.setSpan(new ForegroundColorSpan(0XFFe57373),
                    (accuracySpannable.length() - (accuracyValue.length() + 1)),
                    accuracySpannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            accuracy.setText(accuracySpannable);
            accuracy.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        }

        SpannableStringBuilder streakSpannable = new SpannableStringBuilder("Streak\n" + streakValue);
        streakSpannable.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                (streakSpannable.length() - streakValue.length()),
                streakSpannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        accuracy.setText(accuracySpannable);
        accuracy.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        streak.setText(streakSpannable);
        streak.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        //write data

    }

    @Override
    public void onBackPressed() {
        //disabled for splash
    }

    public void onClickNextButton(View view){
        this.finish();
    }
}
