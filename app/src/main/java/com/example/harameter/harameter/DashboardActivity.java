package com.example.harameter.harameter;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.app.Activity;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.widget.Spinner;
import android.view.Gravity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.widget.ArrayAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class DashboardActivity extends Activity {

    private DatabaseReference haraDB;
    private Spinner select;
    private String name, email, date;
    private int value;
    private Button demo;
    private TextView demo1, demomessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_dashboard);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/dd");
        date = dateFormat.format(calendar.getTime());
        haraDB = FirebaseDatabase.getInstance().getReference();

        name = getIntent().getStringExtra("GOOGLE_NAME");
        if (!name.equals("Demo")) email = getIntent().getStringExtra("GOOGLE_EMAIL");
        else {
            demo = findViewById(R.id.demo);
            demo1 = findViewById(R.id.demo1);
            demomessage = findViewById(R.id.demomessage);
            demomessage.setText("Sign in with Google to track your results");
            demo.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            demo1.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            demomessage.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            demo.setVisibility(View.VISIBLE);
            demo1.setVisibility(View.VISIBLE);
        }

        //Date TextView
        TextView dateView = findViewById(R.id.date);
        SpannableStringBuilder dateSpannable = new SpannableStringBuilder("Today\n" +
                date);
        dateSpannable.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                (dateSpannable.length() - date.length()), dateSpannable.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        dateView.setText(dateSpannable);
        dateView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        TextView accuracyView = findViewById(R.id.accuracy);
        accuracyView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        TextView streakView = findViewById(R.id.streak);
        streakView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        //Accuracy TextView
        value = 74;
        String accuracy = Integer.toString(value);
        SpannableStringBuilder accuracySpannable;
        if (name.equals("Demo")) {
            accuracy = "--";
            accuracySpannable = new SpannableStringBuilder("Accuracy\n" + accuracy);
            accuracySpannable.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                    (accuracySpannable.length() - accuracy.length()),
                    accuracySpannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            accuracyView.setText(accuracySpannable);
        }
        else {
            accuracySpannable = new SpannableStringBuilder("Accuracy\n" +
                    accuracy + "%");
            accuracySpannable.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                    (accuracySpannable.length() - (accuracy.length() + 1)),
                    accuracySpannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (value >= 70) {
                accuracySpannable.setSpan(new ForegroundColorSpan(0XFF81c784),
                        (accuracySpannable.length() - (accuracy.length() + 1)),
                        accuracySpannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                accuracyView.setText(accuracySpannable);
            } else if (value >= 40) {
                accuracySpannable.setSpan(new ForegroundColorSpan(0XFFffd54f),
                        (accuracySpannable.length() - (accuracy.length() + 1)),
                        accuracySpannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                accuracyView.setText(accuracySpannable);
            } else {
                accuracySpannable.setSpan(new ForegroundColorSpan(0XFFe57373),
                        (accuracySpannable.length() - (accuracy.length() + 1)),
                        accuracySpannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                accuracyView.setText(accuracySpannable);
            }
        }

        //Streak
        value = 13;
        String streak = Integer.toString(value);
        if (name.equals("Demo")) streak = "--";
        SpannableStringBuilder streakSpannable = new SpannableStringBuilder("Streak\n" + streak);
        streakSpannable.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                (streakSpannable.length() - streak.length()),
                streakSpannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        streakView.setText(streakSpannable);

        //Difficulty Select Spinner
        String[] difficulty = new String[] {"Beginner", "Advanced"};
        select = findViewById(R.id.select);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner, difficulty);
        adapter.setDropDownViewResource(R.layout.dropdown_item);
        select.setAdapter(adapter);

        //Utility
        TextView message = findViewById(R.id.message);
        message.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        TextView title = findViewById(R.id.DashboardTitle);
        title.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        Button initial = findViewById(R.id.initial);
        initial.setText(name.substring(0,1));
        Button signout = findViewById(R.id.signout);
        TextView username = findViewById(R.id.username);
        username.setText(name);
        TextView haraButton = findViewById(R.id.HaraButton);
        TextView abdominalButton = findViewById(R.id.AbdominalButton);
        if (name.equals("Demo")) {
            initial.setText("H");
            username.setText("Demo");
            initial.setBackgroundResource(R.drawable.demo_circle);
            initial.setTextColor(0xFF000000);
        }
        initial.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        signout.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        username.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        haraButton.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        abdominalButton.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
    }

    @Override
    public void onBackPressed() {
        //disabled for signout
    }

    public void onClickInitialButton(View view){
        Button signout = findViewById(R.id.signout);
        TextView username = findViewById(R.id.username);
        if (signout.getVisibility() == View.INVISIBLE) {
            username.setVisibility(View.VISIBLE);
            signout.setVisibility(View.VISIBLE);
        }
        else if (signout.getVisibility() == View.VISIBLE) {
            username.setVisibility(View.INVISIBLE);
            signout.setVisibility(View.INVISIBLE);
        }
    }

    public void onClickSignOutButton(View view){
        Intent signoutIntent = new Intent(this, LoginActivity.class);
        startActivity(signoutIntent);
    }

    public void onClickDemo(View view){
        if (demo1.getVisibility() == View.VISIBLE) {
            demo1.setVisibility(View.INVISIBLE);
            demomessage.setVisibility(View.VISIBLE);
        }
        else if (demo1.getVisibility() == View.INVISIBLE) {
            demomessage.setVisibility(View.INVISIBLE);
            demo1.setVisibility(View.VISIBLE);
        }
    }

    public void onClickHaraButton(View view){
        Intent haraIntent = new Intent(this, BluetoothActivity.class);
        haraIntent.putExtra("DIFFICULTY", select.getSelectedItem().toString());
        haraIntent.putExtra("METHOD", "Hara");
        if (!name.equals("Demo")) haraIntent.putExtra("GOOGLE_EMAIL", email);
        else haraIntent.putExtra("GOOGLE_EMAIL", "Demo");
        startActivity(haraIntent);
    }
    public void onClickAbdominalButton(View view){
        Intent abdIntent = new Intent(this, BluetoothActivity.class);
        abdIntent.putExtra("DIFFICULTY", select.getSelectedItem().toString());
        abdIntent.putExtra("METHOD", "Abdominal");
        if (!name.equals("Demo")) abdIntent.putExtra("GOOGLE_EMAIL", email);
        else abdIntent.putExtra("GOOGLE_EMAIL", "Demo");
        startActivity(abdIntent);
    }


}
