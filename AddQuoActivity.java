package org.d3ifcool.timework;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddQuoActivity extends AppCompatActivity {

    private String mQuote; //is a quote from user
    private Button mDatabaseButton ; //is a Button to send data Quote from database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_quo);

        //get data from put Extra intent
        mDatabaseButton = (Button) findViewById(R.id.database_button);
        Bundle extra = getIntent().getExtras();
        //if there is data from extra
        if (extra !=null){
            //set Text mDatabase button "Update"
            mDatabaseButton.setText("Update");
            mQuote = extra.getString("quote","");

            if(!mQuote.equals("")) {
                //set text in variabel dataQuote
                EditText dataQuote = (EditText) findViewById(R.id.quote_edit_text);
                dataQuote.setText(mQuote);
            }
        }else{
            //set Text mDatabase button "Tambah"
            mDatabaseButton.setText("Tambah");
        }
        ///

    }

    //when user click backpress button
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            //go to main activity with tab number 1
            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra("current_tab",1);
            startActivity(intent);
            //destroy this activity
            this.finish();
            //
        }
        return super.onKeyDown(keyCode, event);
    }


    //event onClick to save quote data in database
    public void addQuotes(View view) {

        //intial EditText and Database Adapter
        EditText dataQuote = (EditText) findViewById(R.id.quote_edit_text);
        DatabaseAdapter databaseAdapter = new DatabaseAdapter(this);
        //

        //if mDatabaseButton have text "Tambah"
        if(mDatabaseButton.getText().equals("Tambah")){
            //save to database
            databaseAdapter.addQuote(dataQuote.getText().toString());

            //if mDatabaseButton have text "Update"
        }else if(mDatabaseButton.getText().equals("Update")){
            //update to database
            databaseAdapter.updateQuote(dataQuote.getText().toString(),mQuote);
        }
        //go to MainActivity with set tab 1
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("current_tab",1);
        //destroy this activity
        this.finish();
        startActivity(intent);
    }

    //reset all for input
    public void reset (View view){
        EditText dataQuote = (EditText) findViewById(R.id.quote_edit_text);
        dataQuote.setText("");
    }
}
