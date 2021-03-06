package com.chirpper.cwalker2209.chirpper;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chirpper.cwalker2209.chirpper.database.AppDatabase;
import com.chirpper.cwalker2209.chirpper.database.Profile;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity {

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get database
        db = App.get().getDB();

        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button editProfileButton = findViewById(R.id.buttonEditProfile);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfile();
            }
        });

        //Remove original toolbar tittle
        getSupportActionBar().setTitle(null);
    }

    @Override
    protected void onStart () {
        super.onStart();
        new getProfileTask().execute();
    }

    protected void editProfile(){
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }

    protected class getProfileTask extends AsyncTask<Void, Void, Boolean> {

        protected Profile profile;

        @Override
        protected Boolean doInBackground(Void... params){

            try{
                long id = App.get().getUserId();
                profile = db.profileDAO().findByUserId(id);
                return true;
            }
            catch (Exception e){
                Log.e("DB", e.getMessage());
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                TextView name = findViewById(R.id.textViewName);
                TextView date = findViewById(R.id.textViewDate);
                TextView description = findViewById(R.id.textViewDescription);
                ImageView imageViewAvatar = findViewById(R.id.imageViewAvatar);

                name.setText(profile.name);
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                date.setText(fmt.format(profile.created));
                description.setText(profile.description);
                imageViewAvatar.setImageBitmap(profile.image);
            } else {
                Context context = getApplicationContext();
                CharSequence text = "Profile failed to load";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    }

}
