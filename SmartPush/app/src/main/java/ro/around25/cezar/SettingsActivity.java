package ro.around25.cezar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ro.around25.cezar.constants.Constants;

public class SettingsActivity extends AppCompatActivity {

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

    }

    public void editServer(View view) {
        preferences = getSharedPreferences(Constants.STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("host").commit();
        editor.remove("port").commit();
        editor.remove("user").commit();
        editor.remove("password").commit();
        Intent i = new Intent(getApplicationContext(), AddServer.class);
        startActivity(i);
        finish();


    }

    public void eraseSettings(View view) {
        preferences = getSharedPreferences(Constants.STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("need_tutorial").commit();
        Intent i = new Intent(getApplicationContext(), Controller.class);
        startActivity(i);
        finish();
    }





}



