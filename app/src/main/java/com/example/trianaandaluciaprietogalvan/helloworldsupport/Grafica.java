package com.example.trianaandaluciaprietogalvan.helloworldsupport;

import android.support.v7.app.AppCompatActivity;

public class Grafica extends AppCompatActivity {

   /* Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafica);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        numeros = (TextView) findViewById(R.id.textView2);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("speedExceeded"));
        startService();
    }

    BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            int val =intent.getIntExtra("num", 0);
            String valnum = Integer.toString(val);
            numeros.setText(valnum);
        }
    };

    // Method to start the service
    public void startService() {
        intent = new Intent(getBaseContext(), ServiceECG.class);
        ComponentName name = startService(intent);
    }

    // Method to stop the service
    public void stopService(View view) {
        stopService(intent);
    }*/

}
