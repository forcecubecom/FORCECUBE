package ru.forcecube.sample;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import ru.forcecube.tagit.Callback;
import ru.forcecube.tagit.Config;
import ru.forcecube.tagit.constant.Constants;
import ru.forcecube.tagit.manager.ForceCuBe;
import ru.forcecube.tagit.model.Event;


public class MainActivity extends Activity {

    private static final int REQUEST_ENABLE_BT = 1;

    private TextView messageView;
    private Button startServiceButton;
    private SeekBar touchDistanceSeekbar;

    private StringBuilder logStringBuilder = new StringBuilder();

    private boolean started;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageView = (TextView) findViewById(R.id.message_view);
        messageView.setMovementMethod(new ScrollingMovementMethod());

        touchDistanceSeekbar = (SeekBar) findViewById(R.id.sb_touch_distance);
        touchDistanceSeekbar.setProgress((int) (Constants.PROXIMITY_DISTANCE_TOUCH * 100));
        touchDistanceSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                ForceCuBe.getInstance().setConfiguration(new Config.Builder().setProximityDistanceTouch(i / 100D).build());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        startServiceButton = (Button) findViewById(R.id.btn_start_service);
        startServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!started) {
                    startServiceButton.setEnabled(false);
                    startServiceButton.setText("STARTING");

                    // Checking whether BLE is accessible or not
                    if (!ForceCuBe.getInstance().isBLEAccessible(MainActivity.this)) {

                        // If it's not accessible then we show user dialog to turn it on
                        startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_ENABLE_BT);
                    } else {

                        // If it's accessible, then we start our service
                        startForceCubeService(new Callback<Void>() {
                            @Override
                            public void onSuccess(Void data) {
                                started = true;
                                startServiceButton.setText("STARTED");
                            }

                            @Override
                            public void onFailure(Throwable error) {
                                started = false;

                                Toast.makeText(getApplicationContext(), "Failed to start service", Toast.LENGTH_LONG).show();

                                startServiceButton.setText("START SERVICE");
                                startServiceButton.setEnabled(true);
                            }
                        });
                    }
                }
            }
        });
    }

    private void startForceCubeService(Callback<Void> callback) {
        ForceCuBe.getInstance().startService(this, "developmentTestKey", callback, Config.defaultConfiguration());
        ForceCuBe.getInstance().addBeaconEventListener(new ForceCuBe.BeaconEventListener() {
            @Override
            public void onEnterRegionAction(Event event) {
                Log.d("sample", "onEnterRegionAction");
            }

            @Override
            public void onExitRegionAction(Event event) {
                Log.d("sample", "onExitRegionAction");
            }

            @Override
            public void onRangeAction(Event event) {
                Log.d("sample", "onRangeAction");
            }
        });
    }

    private void stopForceCubeService(Callback<Void> callback) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK) {
                    startForceCubeService(new Callback<Void>() {
                        @Override
                        public void onSuccess(Void data) {
                            started = true;
                            startServiceButton.setText(R.string.stop_service);
                        }

                        @Override
                        public void onFailure(Throwable error) {

                        }
                    });
                } else {
                    Toast.makeText(this, "Service needs bluetooth to be enabled to work properly", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        ForceCuBe.getInstance().setAppInBackground(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ForceCuBe.getInstance().setAppInBackground(false);
    }

    private void logMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                logStringBuilder.append(message);

                messageView.setText(logStringBuilder.toString());
            }
        });
    }
}
