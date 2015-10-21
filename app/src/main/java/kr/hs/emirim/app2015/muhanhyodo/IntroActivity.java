package kr.hs.emirim.app2015.muhanhyodo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class IntroActivity extends AppCompatActivity {
    private static final String TAG="무한효도:IntroActivity";
    private static final int WAIT_TIME = 3000;
    private Intent m_intent;

    //푸시 처리를 위한 코드
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ProgressBar mRegistrationProgressBar;
    private TextView mInformationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_intro);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        Log.d(TAG, "GCM 등록 시작");
        mRegistrationProgressBar = (ProgressBar) findViewById(R.id.registrationProgressBar);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    Log.d(TAG, "푸시 등록이 정상적으로 되었습니다.");
                    //mInformationTextView.setText(getString(R.string.gcm_send_message));
                } else {
                    Log.d(TAG, "푸시 등록이 실패했습니다.");
                   // mInformationTextView.setText(getString(R.string.token_error_message));
                }
            }
        };

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

        SharedPreferences prefs = getSharedPreferences("muhanhyodo", MODE_PRIVATE);
        int user_id = prefs.getInt("user_id", 0);

        Log.d(TAG, "user_id는 " + user_id);
        if (user_id < 0) {
            Log.d(TAG, "최초 가입이므로 등록화면으로 넘어감");
            Snackbar.make( findViewById(R.id.layout) , R.string.first_user , Snackbar.LENGTH_LONG).show();
            m_intent = new Intent(IntroActivity.this, InputGrandMotherDataActivity.class);
        } else {
            Log.d(TAG, "등록된 사용자이므로 리스트로 넘어감");
            Snackbar.make( findViewById(R.id.layout) , R.string.welcome, Snackbar.LENGTH_SHORT).show();
            m_intent = new Intent(IntroActivity.this, MainMenuActivity.class);
        }

        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(m_intent);
                finish();
            }
        }, WAIT_TIME);

    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));

    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "지원하지 못하는 기기.");
                finish();
            }
            return false;
        }
        return true;
    }

}
