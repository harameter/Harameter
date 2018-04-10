package com.example.harameter.harameter;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


public class LoginActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.
                FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        // Button listeners
        findViewById(R.id.GuestButton).setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.GuestButton:
                Intent loginIntent = new Intent(this, DashboardActivity.class);
                startActivity(loginIntent);
                break;
        }
    }

}
