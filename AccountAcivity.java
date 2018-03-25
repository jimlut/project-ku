package org.d3ifcool.timework;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountAcivity extends Activity {

    private TextView mUsername;
    private  CircleImageView mAvatar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_acivity);

        //call class account and get data from database in showAcccount
        Account account = showAccount();

        //initial all component in layout account_activiy
        mAvatar = (CircleImageView) findViewById(R.id.avatar_image_view) ; //is an avatar image
        mUsername = (TextView) findViewById(R.id.username_text_view) ;// is a username account
        TextView myQuote = (TextView) findViewById(R.id.quote_text_view); // is a quote from user
        ///

        //show data account in component layout account_activity
        mUsername.setText(account.getmUsername().toString());
        mAvatar.setImageResource(account.getmImage());
        myQuote.setText(account.getQuote());
        ///
    }

    //event on Click when user want to change the avatar
    public void changeAvatar(View view) {
        //make imageview array to save all image avatar
        ImageView avatar[] = new ImageView[4];
        final int avatarImage[] = {R.drawable.avatar_satu,R.drawable.avatar_dua,R.drawable.avatar_tiga,R.drawable.avatar_empat};
        ///

        //make alert dialog
        AlertDialog.Builder showListAvatar = new AlertDialog.Builder(this);
        //get layout from avatar_list
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.avatar_list,null);
        //set content alert dialog
        showListAvatar.setView(dialogView);
        //set title alert dialog
        showListAvatar.setTitle(R.string.choose_avatar);
        //show alert dialog
        final AlertDialog alertDialog = showListAvatar.create();
        alertDialog.show();

        //set ImageView array for each component in avatar list
        avatar[0] = (ImageView) dialogView.findViewById(R.id.avatar1_image_view);
        avatar[1] = (ImageView) dialogView.findViewById(R.id.avatar2_image_view);
        avatar[2] = (ImageView) dialogView.findViewById(R.id.avatar3_image_view);
        avatar[3] = (ImageView) dialogView.findViewById(R.id.avatar4_image_view);

        //set image for variabel avatar
        for(int n=0;n<avatar.length;n++){
            final int finalN = n;//to save variabel n
            //set on click listener for each image view
            avatar[n].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //hide alert dialog
                    alertDialog.hide();
                    //make variabel account to save data account from database
                    Account account = showAccount();

                    //set image from avatarImageView
                    CircleImageView avatarImageView = (CircleImageView) findViewById(R.id.avatar_image_view);
                    avatarImageView.setImageResource(avatarImage[finalN]);
                    //send data update account  in database
                    account.setmImage(avatarImage[finalN]);
                    sendToDatabase(account,account.getmUsername());
                }
            });
        }

    }

    //this method is to save data Account from Database
    public void sendToDatabase(Account account,String currUsername){
        DatabaseAdapter databaseAdapter = new DatabaseAdapter(this);
        databaseAdapter.updateAccount(account,currUsername);
    }

    //this method is to get data account from database
    public Account showAccount(){
        DatabaseAdapter databaseAdapter = new DatabaseAdapter(this);
        Account account = databaseAdapter.getAccount();
        return account;
    }

    //method to edit username
    public void editUsername(View view) {
        //create an alert dialog
        AlertDialog.Builder editUsername = new AlertDialog.Builder(this);
        //get layout edit_username
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.edit_username,null);
        //set editText with mUsername
        EditText username  = (EditText) dialogView.findViewById(R.id.username_edit_text);
        username.setText(mUsername.getText());
        //set content alert dialog
        editUsername.setView(dialogView);
        //set title alert dialog
        editUsername.setTitle(R.string.text_edit_username);

        //set positive button in alert dialog
        editUsername.setPositiveButton(R.string.text_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //set Text View mUsername from variabel edit
                EditText edit = (EditText) dialogView.findViewById(R.id.username_edit_text);
                mUsername.setText(edit.getText().toString());
                //get data account from database
                Account account = showAccount();

                //save data account to database
                account.setmUsername(edit.getText().toString());
                DatabaseAdapter databaseAdapter = new DatabaseAdapter(AccountAcivity.this);
                databaseAdapter.updateAccount(account,showAccount().getmUsername());

            }
        });

        //set Negative Button in alert dialog
        editUsername.setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //back to AccountActivity
            }
        });

        //show alert dialog
        AlertDialog alertDialog = editUsername.create();
        alertDialog.show();
    }
}
