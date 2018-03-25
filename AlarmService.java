package org.d3ifcool.timework;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class AlarmService extends Service {

    private boolean mIsOpen = false;
    private ArrayList<Schedule> mCurrSchedule;
    private int mDelay =0;
    private int mCountDown;

    private String mNameSchedule = "" ;

    //convert time to int
    private int convertToInt (String time) {
        String dataSplit[] = time.split(":");
        int dataTimeInt  = Integer.parseInt(dataSplit[0]+dataSplit[1]);

        return dataTimeInt;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean isLoop = false;
        try{
            //recieve data from intent put extra
            isLoop = intent.getExtras().getBoolean("isLoop"); //when service want to run real time
            mDelay = intent.getExtras().getInt("delay");// get current delay
            mNameSchedule = intent.getExtras().getString("nameSchedule");//get name schedule
        }catch (Exception e){

        }

        //start Thread
        CheckTime checkTime = new CheckTime(this,isLoop,mNameSchedule);
        Thread thread = checkTime;
        thread.start();
        //
        //when thread is finish
        boolean isClose = stopService(intent);

        Log.d("Is close " , String.valueOf(isClose ? 1 : 0));

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class CheckTime extends Thread {
         private Context mContext;//context from parent
         private boolean mIsLoop;//variabel to while  condition
         private String mCurrentActive;//Schedule Active

        /**
         *
         * @param context is context from parent
         * @param loop is variabel to while  condition
         * @param mNameShedule is Schedule Active
         */
        public CheckTime(Context context,Boolean loop,String mNameShedule) {
            this.mContext = context;
            this.mIsLoop = loop;
            this.mCurrentActive = mNameShedule;

            Log.i(" Make It" , "true");

        }


        @Override
        public void run() {
            //set while condition
            boolean loop = true;
            //mCountDown when Thread is not run in real time
            mCountDown = mDelay * 60;

            while (loop) {
                //get current active data schedule from database with same day in system android
                CurrentSchedule currentSchedule = new CurrentSchedule();
                currentSchedule.setCurrentSchedule(mContext);
                mCurrSchedule = currentSchedule.getmCurrentSchedule();
                //

                //if mCountDown not zero then count down
                if(mCountDown !=0) {
                    mCountDown--;
                }

                Log.i("Delay" , String.valueOf(mCountDown));
                //sleep app for 1 second
                try {
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //

                //get timr now
                Calendar cal = Calendar.getInstance(); //is variabel to get calender

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                String time = simpleDateFormat.format(cal.getTime());
                ///

                //check for each data in mCurrSchedule
                for(int n=0; n <mCurrSchedule.size() ; n++) {
                    //this condition is use to check if time now in range start time and end time
                    if ( convertToInt(time) >= convertToInt(mCurrSchedule.get(n).getStartTime()) &&
                            convertToInt(time) <=  convertToInt(mCurrSchedule.get(n).getEndTime())
                            && !mIsOpen && mCurrSchedule.get(n).getActive() == 1 &&
                            (mCountDown == 0 || mDelay == -1)) {
                       //count down is stop
                        mCountDown =0;
                        //the lock activity was launch
                        mIsOpen = true;

                        //start Lock activity
                        Intent lockActivity = new Intent(mContext, LockActivity.class);
                        lockActivity.putExtra("end_time",mCurrSchedule.get(n).getEndTime());
                        lockActivity.putExtra("name_scheduling",mCurrSchedule.get(n).getNameSchedule());
                        lockActivity.putExtra("current_delay",mDelay);
                        lockActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(lockActivity);
                        //
//
                        //thread is stop
                        loop = mIsLoop;
                        //set Current schedule active
                        mCurrentActive = mCurrSchedule.get(n).getNameSchedule();

                    }

                    //stop Thread when data schedule not active
                    if(mCurrSchedule.get(n).getNameSchedule().equals(mCurrentActive)
                           && mCurrSchedule.get(n).getActive()==0 ) {

                        if(mCountDown ==0) {
                            mDelay = 0;
                            mIsOpen = false;
                            mCountDown = 0;
                            mCurrentActive = "" ;
                        }else{
                            loop = false;
                        }

                    }

                }

            }

        }

    }

}