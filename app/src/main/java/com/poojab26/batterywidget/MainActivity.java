package com.poojab26.batterywidget;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver mReceiver;
    int battery_level;
    String TAG = "Battery";


    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.tvBattery) TextView tvBattery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mReceiver = new BatteryBroadcastReceiver();

    }

    @Override
    protected void onStart() {
        registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        super.onStart();
    }
    @Override
    protected void onStop() {
        unregisterReceiver(mReceiver);
        super.onStop();
    }

    public void setValueToWidget() {
        BatteryAppWidget.setBatteryLevel(battery_level+"%");

        Intent widgetIntent = new Intent(MainActivity.this, BatteryAppWidget.class);
        widgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        int[] ids = AppWidgetManager.getInstance(getApplication())
                .getAppWidgetIds(new ComponentName(getApplication(), BatteryAppWidget.class));
        widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(widgetIntent);    }


    private class BatteryBroadcastReceiver extends BroadcastReceiver {
        private final static String BATTERY_LEVEL = "level";
        @Override
        public void onReceive(Context context, Intent intent) {
            battery_level = intent.getIntExtra(BATTERY_LEVEL, 0);
           setValueToWidget();
           tvBattery.setText(battery_level+"");
           progressBar.setProgress(battery_level);
        }
    }
}
