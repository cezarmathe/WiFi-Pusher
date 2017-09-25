package ro.around25.cezar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;

import ro.around25.cezar.constants.Constants;
import ro.around25.cezar.helpers.MqttHelper;

public class Controller extends AppCompatActivity {

    private static final String LOGGER = AddServer.class.getSimpleName();
    ListView listView;
    ArrayList<Chanel> groups;
    ArrayList<Chanel> devices;
    ArrayList<Chanel> dataSet;
    private static ChanelAdapter adapter;
    SharedPreferences preferences;
    MqttHelper mqttHelper;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_devices:
                    // load devices chanels
                    dataSet = devices;
                    reloadData();
                    return true;
                case R.id.navigation_groups:
                    // load groups chanels
                    dataSet = groups;
                    reloadData();
                    return true;
            }
            return false;
        }

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_button:
                Intent setI = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(setI);
                return true;

            case R.id.how_to_setup_button:
                Intent htsI = new Intent(getApplicationContext(), HowToSetupActivity.class);
                startActivity(htsI);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void reloadData() {
        adapter = new ChanelAdapter(dataSet, getApplicationContext(), mqttHelper);
        listView.setAdapter(adapter);
    }

    protected void moveBack() {
        Intent i = new Intent(getApplicationContext(), AddServer.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        preferences = getSharedPreferences(Constants.STORAGE, Context.MODE_PRIVATE);

        connectMqtt();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        listView = (ListView) findViewById(R.id.pushers);

        devices = new ArrayList<>();
        groups = new ArrayList<>();
        dataSet = devices;

        adapter = new ChanelAdapter(dataSet, getApplicationContext(), mqttHelper);

        listView.setAdapter(adapter);

    }

    void connectMqtt() {
        String host = preferences.getString("host", "");
        int port = preferences.getInt("port", 0);
        String user = preferences.getString("user", "");
        String password = preferences.getString("password", "");

        mqttHelper = new MqttHelper(getApplicationContext());

        mqttHelper.setOptions(host, port, user, password);

        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                Log.w("mqtt", serverURI);
            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d(LOGGER, topic);
                Log.d(LOGGER, message.toString());
                switch (topic) {
                    case "connected-device":
                        devices.add(new Chanel(message.toString()));
                        reloadData();
                        break;
                    case "connected-in-group":
                        groups.add(new Chanel(message.toString()));
                        reloadData();
                        break;
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

    }

}
