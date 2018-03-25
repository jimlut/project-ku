package org.d3ifcool.timework;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;


public class DatabaseAdapter extends SQLiteOpenHelper{

    private Context mContext;

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "timeWorks.db";

    // All table name
    private static final String TABLE_SCHEDULE = "schedule";
    private static final String TABLE_QUOTES = "quotes";
    private static final String TABLE_ACCOUNT = "account";


    // schedule Table Columns names
    private static final String KEY_ID = "id_schedule";
    private static final String KEY_NAME = "name_schedule";
    private static final String KEY_DAY = "day";
    private static final String KEY_START_TIME = "start_time";
    private static final String KEY_END_TIME = "end_time";
    private static final String KEY_ACTIVE = "active";


    // quotes Table Columns names
    private static final String KEY_NAME_QUOTE = "name_quote";

    //account table Columns names
    private static final String KEY_USERNAME = "username";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_IS_LOGIN = "is_login";
    private static final String KEY_MY_QUOTE = "my_quote";

    //query to create table
    private static final String CREATE_QUOTES_TABLE = "CREATE TABLE " + TABLE_QUOTES + "("
            + KEY_NAME_QUOTE + " TEXT  )";

    private static final String CREATE_SCHEDULING_TABLE = "CREATE TABLE " + TABLE_SCHEDULE + "("
            + KEY_ID + " TEXT," + KEY_NAME + " TEXT," +KEY_DAY + " TEXT,"
            + KEY_START_TIME + " TEXT," + KEY_END_TIME  + " TEXT," + KEY_ACTIVE + " INTEGER)";

    private static final String CREATE_ACCOUNT_TABLE = "CREATE TABLE " + TABLE_ACCOUNT + "("
            + KEY_USERNAME + " TEXT PRIMARY KEY," + KEY_IMAGE + " INTEGER," +KEY_IS_LOGIN + " INTEGER, "+
            KEY_MY_QUOTE +" TEXT)";








    public DatabaseAdapter(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL(CREATE_SCHEDULING_TABLE);
            db.execSQL(CREATE_QUOTES_TABLE);
            db.execSQL(CREATE_ACCOUNT_TABLE);

        }catch(Exception e) {
           Log.e("Create Database", "Error to Create");
        }

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUOTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);

        // Create tables again
        onCreate(db);
    }

    public long addQuote (String quotes){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_NAME_QUOTE ,quotes);//data quote
        // Inserting Row
        long i = db.insert(TABLE_QUOTES, null, values);
        db.close(); // Closing database connection

        Log.i("data",quotes);

        return i;
    }


    public long addSchedule(Schedule schedule) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID , String.valueOf(schedule.getIdSchedule()) ); //schedule id
        values.put(KEY_NAME, String.valueOf(schedule.getNameSchedule())); //schedule nam
        values.put(KEY_DAY,String.valueOf(schedule.getDay()));//schedule day
        values.put(KEY_START_TIME, String.valueOf(schedule.getStartTime()));//schedule start time
        values.put(KEY_END_TIME, String.valueOf(schedule.getEndTime()));//schedule end time
        values.put(KEY_ACTIVE,schedule.getActive());//schedule active

        Toast.makeText(mContext,KEY_ACTIVE,Toast.LENGTH_SHORT).show();

        // Inserting Row
        long i = db.insert(TABLE_SCHEDULE, null, values);
        db.close(); // Closing database connection
        return i;
    }

    public long addAccount (Account account){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME ,String.valueOf(account.getmUsername()));//account username
        values.put(KEY_IMAGE ,account.getmImage());//account image
        values.put(KEY_IS_LOGIN ,account.getmIsLogin());//account is login
        values.put(KEY_MY_QUOTE,String.valueOf(account.getQuote()));//account my quote
        // Inserting Row
        long i = db.insert(TABLE_ACCOUNT, null, values);
        db.close(); // Closing database connection


        return i;
    }



    public ArrayList<Schedule> getAllSchedule() {
        ArrayList<Schedule> schedulestList = new ArrayList<Schedule>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SCHEDULE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Schedule schedule = new Schedule(cursor.getString(0),
                        cursor.getString(1), cursor.getString(2),cursor.getString(3),
                        cursor.getString(4),cursor.getInt(5));
                // Adding contact to list
                schedulestList.add(schedule);
            } while (cursor.moveToNext());
        }

        // return schedule list
        return schedulestList;
    }

    public Quotes getAllQuote(){
        Quotes quotes = new Quotes(mContext);
        String selectQuery = "SELECT  * FROM " + TABLE_QUOTES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String data = cursor.getString(0);
                quotes.setQuote(data);

            } while (cursor.moveToNext());
        }

        // return quote list
        return  quotes;
    }


    public Account getAccount() {
        Account account = null;
        String selectQuery = "SELECT  * FROM " + TABLE_ACCOUNT;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                account = new Account(cursor.getString(0),
                        cursor.getInt(1), cursor.getInt(2),
                        cursor.getString(3));

            } while (cursor.moveToNext());
        }

        // return account
        return account;

    }


    // Updating single scheduling
    public int updateSchedule(Schedule schedule) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, String.valueOf(schedule.getNameSchedule())); //schedule name
        values.put(KEY_DAY,String.valueOf(schedule.getDay()));//schedule daya
        values.put(KEY_START_TIME, String.valueOf(schedule.getStartTime())); //schedule start time
        values.put(KEY_END_TIME, String.valueOf(schedule.getEndTime()));//schedule end time
        values.put(KEY_ACTIVE, schedule.getActive());//schedule active
        // updating row
        return db.update(TABLE_SCHEDULE, values, KEY_ID + " = ?",
                new String[] { String.valueOf(schedule.getIdSchedule()) });
    }

    // Updating single contact
    public int updateQuote(String newQuote , String currentQuote ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME_QUOTE , newQuote);//data quote

        // updating row
        return db.update(TABLE_QUOTES, values, KEY_NAME_QUOTE + " = ?",
                new String[] { currentQuote });
    }

    public int updateAccount(Account account, String curremtUsername) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME ,account.getmUsername());//account username
        values.put(KEY_IMAGE ,account.getmImage());//account image
        values.put(KEY_IS_LOGIN ,account.getmIsLogin());//account is login
        values.put(KEY_MY_QUOTE,account.getQuote());//account my quote
        // updating row
        return db.update(TABLE_ACCOUNT, values, KEY_USERNAME + " = ?",
                new String[] { String.valueOf(curremtUsername) });
    }

    public void deleteSchedule(Schedule schedule) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SCHEDULE, KEY_ID + " = ?",
                new String[] { String.valueOf(schedule.getIdSchedule()) });
        db.close();
    }


    public void deleteQuote(String quote) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_QUOTES, KEY_NAME_QUOTE + " = ?",
                new String[] { String.valueOf(quote) });
        db.close();
    }

}

