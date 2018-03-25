package org.d3ifcool.timework;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;


import de.hdodenhof.circleimageview.CircleImageView;

public class CreateProfileActivity extends AppCompatActivity {

    private final int mAvatarImage[] = {R.drawable.avatar_satu,R.drawable.avatar_dua,
            R.drawable.avatar_tiga, R.drawable.avatar_empat}; //is a all image avatar
    private int mCurrentAvatar  = R.drawable.avatar_satu ; //is a default or avatar image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);;
    }



    public void createProfile(View view) {

        DatabaseAdapter databaseAdapter = new DatabaseAdapter(this);

        try{
            Account account = databaseAdapter.getAccount();
            if(account.getmIsLogin() == 1) {
                Intent gotoMain = new Intent(this,MainActivity.class) ;
                startActivity(gotoMain);
            }else{

            }
        }catch (Exception e) {
            EditText username = (EditText) findViewById(R.id.username_edit_text);
            Quotes quotes = new Quotes(this);
            long i = databaseAdapter.addAccount(new Account(username.getText().toString(),
                    mCurrentAvatar,1,quotes.getmAllQuotes().get(0)));

            if (i ==1 ) {
                Intent createProfile = new Intent(this,MainActivity.class) ;
                startActivity(createProfile);
                this.finish();
            }else{
                startActivity(getIntent());
            }


        }


    }


    //event onClick to change Avatar
    public void changeAvatar(View view) {
        //create ImageView Array
        ImageView avatar[] = new ImageView[4];

        //create alert dialog
        AlertDialog.Builder showListAvatar = new AlertDialog.Builder(this);

        //get layput avatar_list
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.avatar_list,null);

        //set content alert dialog
        showListAvatar.setView(dialogView);
        //set title alert dialog
        showListAvatar.setTitle(R.string.text_choose_avatar);
        final AlertDialog alertDialog = showListAvatar.create();
        alertDialog.show();

        //set all avatar image in variabel avatar
        avatar[0] = (ImageView) dialogView.findViewById(R.id.avatar1_image_view);
        avatar[1] = (ImageView) dialogView.findViewById(R.id.avatar2_image_view);
        avatar[2] = (ImageView) dialogView.findViewById(R.id.avatar3_image_view);
        avatar[3] = (ImageView) dialogView.findViewById(R.id.avatar4_image_view);

        //set onClickListener for each avatar
        for(int n=0;n<avatar.length;n++){
            final int finalN = n;
            avatar[n].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //hide alert dialog
                    alertDialog.hide();
                    //set image CircleImageView
                    CircleImageView avatarImageView = (CircleImageView) findViewById(R.id.avatar_image_view);
                    avatarImageView.setImageResource(mAvatarImage[finalN]);
                    mCurrentAvatar = mAvatarImage[finalN];
                }
            });
        }

    }


}
