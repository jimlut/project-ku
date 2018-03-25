package org.d3ifcool.timework;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_service);

        //start MainActivity
        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);

        //Start service from AlaramService
        startService(new Intent(this,AlarmService.class).putExtra("isLoop",true));

        finish();

    }

}