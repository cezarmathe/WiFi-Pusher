package ro.around25.cezar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;

import ro.around25.cezar.constants.Constants;

public class HowToSetupActivity extends AppCompatActivity {

    private SharedPreferences preferences;

    public void openWebPage() {
        String url = getString(R.string.h2s_url);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences(Constants.STORAGE, Context.MODE_PRIVATE);
        boolean need_tutorial = preferences.getBoolean("need_tutorial", true);
        if (need_tutorial){
            setContentView(R.layout.activity_how_to_setup);
        }
        else {
            openWebPage();
        }
    }

    public void goToWebsite(View view) {
        CheckBox pref = (CheckBox) findViewById(R.id.checkBox);
        if (pref.isChecked()) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("need_tutorial", false);
            editor.apply();
        }
        else {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("need_tutorial", true);
            editor.apply();
        }
        openWebPage();
    }
}
