package org.d3ifcool.timework;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;


public class CurrentSchedule {
    private ArrayList<Schedule> mCurrentScheule; //to get data schedule active from database

    public CurrentSchedule() {
        mCurrentScheule  =new ArrayList<>();
    }

    public void setCurrentSchedule (Context context) {

        //get day now
        Calendar c = Calendar.getInstance();

        int day = c.get(Calendar.DAY_OF_WEEK);
        String currentDay = "";

        String[] days = context.getResources().getStringArray(R.array.days);

        switch (day){
            case Calendar.SUNDAY  :currentDay = days[6];break;
            case Calendar.MONDAY  : currentDay = days[0];break;
            case Calendar.TUESDAY  : currentDay = days[1];break;
            case Calendar.WEDNESDAY  : currentDay = days[2];break;
            case Calendar.THURSDAY : currentDay = days[3];break;
            case Calendar.FRIDAY  : currentDay = days[4];break;
            case Calendar.SATURDAY  : currentDay = days[5];break;
        }
        ///

        //get schedule active from database and same day with system android
        DatabaseAdapter databaseAdapter = new DatabaseAdapter(context);
        ArrayList<Schedule> curr = databaseAdapter.getAllSchedule();

        for (Schedule data:curr){
            if (data.getDay().equals(currentDay)){
                mCurrentScheule.add(data);
            }
        }
        //
    }

    public ArrayList<Schedule> getmCurrentSchedule() {
        return mCurrentScheule;
    }
}
