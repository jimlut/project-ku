package org.d3ifcool.timework;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LockActivity extends AppCompatActivity {

    private String mEndTime ; // recieve data end time from intent put extra
    private String mNameSchedule ; // recieve data name schedule from intent put extra
    private int mCurrentDelay;// recieve data current delay from intent put extra
    private int mNextDelay;// data to set next delay

    private TextView mMessage;// message alert when lock activity active
    private TextView mSchedule;// is a info schedule active

    private MediaPlayer mMediaPlayer ; // to run file music

    private Thread mThread;//to get thread and run it




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);

        //hide action bar when opened
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //

        //get component from activity lock
        mMessage = (TextView) findViewById(R.id.message_text_view);
        mSchedule = (TextView) findViewById(R.id.schedule_text_view);
        //

        //get all data from intent put ectra and set it
        recieveData();
        //

        //run thread
        TimeTick timeTick = new TimeTick(this,mEndTime);
        mThread = timeTick;
        mThread.start();
        //

        //show content
        showData(mCurrentDelay);

        //run music alarm
        releaseMediaPlayer();
        mMediaPlayer = MediaPlayer.create(this,R.raw.sound_alarm);
        mMediaPlayer.start();
    }

    //when destroy stop thread
    @Override
    protected void onDestroy() {
        mThread.interrupt();
        super.onDestroy();

    }
    //to release media player
    private void releaseMediaPlayer(){
        if(mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer =null;
        }
    }

    //start AlarmService when click backpress button
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            startService(new Intent(this,AlarmService.class)
                    .putExtra("delay",mNextDelay)
                    .putExtra("nameSchedule",mNameSchedule));
            //destroy this activity
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    //start AlarmService when click Home button
    @Override
    protected void onUserLeaveHint() {
        startService(new Intent(this,AlarmService.class)
                .putExtra("delay",mNextDelay)
                .putExtra("nameSchedule",mNameSchedule));
        //destroy this activity
        this.finish();
        super.onUserLeaveHint();
    }


    //disable wifi
    private void disableWifi(){
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(false);
    }

    //get Account from database
    private Account getAccount(){
        DatabaseAdapter databaseAdapter = new DatabaseAdapter(this);
        return databaseAdapter.getAccount();
    }

    //recieve data from intent put extra
    private void recieveData(){
        Bundle extra = getIntent().getExtras();
        if(extra== null) {
            //do nothing
        }else{
            mEndTime = extra.getString("end_time");
            mNameSchedule = extra.getString("name_scheduling");
            mCurrentDelay = extra.getInt("current_delay");

            Toast.makeText(this,mEndTime,Toast.LENGTH_SHORT).show();
        }
    }

    //set data mMessage ,mNextDelay ,and mSchedule
    private void showData(int delay) {

        switch (delay) {
            case 0:
                mMessage.setText("Maaf, saat ini sudah memasuki kegiatan\n '" + mNameSchedule +"'\n" +
                        "mohon untuk tidak bermain smartphone");
                mNextDelay = 5;
                break;
            case 5:
                disableWifi();
                mMessage.setText("Mohon untuk tidak bermain smartphone pada kegiatan\n '"+ mNameSchedule +"'" +
                        " ini");
                mNextDelay = 10;
                break;
            case 10:
                disableWifi();
                mMessage.setText("Hallo," + getAccount().getmUsername() + " sekarang anda \n" +"" +
                        " memasuki kegiatan '" + mNameSchedule +"'\n");
                mSchedule.setText("Quote Anda : \n" +
                        getAccount().getQuote());
                mNextDelay =-1;
                break;
            case -1 :
                disableWifi();
                mMessage.setText("Hallo," + getAccount().getmUsername() + " sekarang anda \n" +"" +
                        " memasuki kegiatan '" + mNameSchedule +"'\n");
                mSchedule.setText("Quote Anda : \n" +
                        getAccount().getQuote());
                mNextDelay =-1;
                break;
        }
    }

    //this class to check end time and close it if system time android same with end time
    class TimeTick extends Thread{
        private String mEndTime;
        private Context mContext;

        /**
         *
         * @param context is a perent coontext
         * @param endTime is a current endTime from schedule
         */
        public TimeTick(Context context,String endTime){
            this.mContext = context;
            this.mEndTime = endTime;
        }

        @Override
        public void run() {
            boolean loop = true;
            while (loop){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Calendar cal = Calendar.getInstance();

                Log.d("now","nj");


                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                String time = simpleDateFormat.format(cal.getTime());

                if (time.equals(mEndTime)){
                    ((Activity) mContext).finish();
                    break;
                }

            }
        }


    }

}
