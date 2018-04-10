package com.example.harameter.harameter;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.widget.Spinner;
import android.view.Gravity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.widget.ArrayAdapter;


public class DashboardActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Date TextView
        TextView dateView = findViewById(R.id.date);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/dd");
        String date = dateFormat.format(calendar.getTime());
        SpannableStringBuilder dateSpannable = new SpannableStringBuilder("Today\n" +
                date);
        dateSpannable.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                (dateSpannable.length() - date.length()), dateSpannable.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        dateView.setText(dateSpannable);
        dateView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        //Accuracy TextView
        TextView accuracyView = findViewById(R.id.accuracy);
        int value = 74;
        String accuracy = Integer.toString(value);
        SpannableStringBuilder accuracySpannable = new SpannableStringBuilder("Accuracy\n" +
                accuracy + "%");
        accuracySpannable.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                (accuracySpannable.length() - (accuracy.length() + 1)),
                accuracySpannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (value >= 70) {
            accuracySpannable.setSpan(new ForegroundColorSpan(0XFF81c784),
                    (accuracySpannable.length() - (accuracy.length() + 1)),
                    accuracySpannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            accuracyView.setText(accuracySpannable);
            accuracyView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        }
        else if (value >=40) {
            accuracySpannable.setSpan(new ForegroundColorSpan(0XFFffd54f),
                    (accuracySpannable.length() - (accuracy.length() + 1)),
                    accuracySpannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            accuracyView.setText(accuracySpannable);
            accuracyView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        }
        else {
            accuracySpannable.setSpan(new ForegroundColorSpan(0XFFe57373),
                    (accuracySpannable.length() - (accuracy.length() + 1)),
                    accuracySpannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            accuracyView.setText(accuracySpannable);
            accuracyView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        }

        //Difficulty Select Spinner
        String[] difficulty = new String[] {"Beginner", "Advanced"};
        Spinner select = findViewById(R.id.select);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner, difficulty);
        adapter.setDropDownViewResource(R.layout.dropdown_item);
        select.setAdapter(adapter);

        //Utility
        TextView message = findViewById(R.id.message);
        message.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        TextView title = findViewById(R.id.DashboardTitle);
        title.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

    }

    public void onClickHaraButton(View view){
        Intent haraIntent = new Intent(this, BluetoothActivity.class);
        startActivity(haraIntent);
    }
    public void onClickAbdominalButton(View view){
        Intent abdIntent = new Intent(this, BluetoothActivity.class);
        startActivity(abdIntent);
    }
}
