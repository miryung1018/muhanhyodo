package kr.hs.emirim.app2015.muhanhyodo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.sql.Date;
import java.util.List;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedFile;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "무한효도메인::MainActivity";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ProgressBar mRegistrationProgressBar;
    private TextView mInformationTextView;

    private int m_user_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                    mInformationTextView.setText(getString(R.string.gcm_send_message));
                } else {
                    mInformationTextView.setText(getString(R.string.token_error_message));
                }
            }
        };
        mInformationTextView = (TextView) findViewById(R.id.informationTextView);

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
        getData();

    }


    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private void getData() {

        /**
         * Gson 컨버터 이용
         */
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();

        /**
         * 레트로핏 설정
         */
        RestAdapter restAdapter = new RestAdapter.Builder()
                //로그 레벨 설정
                .setLogLevel(RestAdapter.LogLevel.FULL)
                        //BASE_URL 설정
                .setEndpoint(MuhanhyodoService.API_URL)
                        //OkHttpClient 이용
                .setClient(new OkClient(new OkHttpClient()))
                        //Gson Converter 설정
                .setConverter(new GsonConverter(gson))
                .build();

        /**
         * 통신 콜백 메서드 Callback<List<Address>> callback
         */
        Log.i(TAG, "address 가져오기");
        restAdapter.create(MuhanhyodoService.class).address(m_user_id, new Callback<List<Address>>() {
            @Override
            public void success(List<Address> addresses, Response response) {
                Address a;
                for (int i = 0; i < addresses.size(); i++) {
                    a = addresses.get(i);
                    Log.i(TAG, "[" + (i + 1) + "] " + a.getName() + " / " + a.getTel() + " / " + a.getId() + " / " + a.getAddress());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i(TAG, "address 가져오기 에러 ");
            }
        });

        Log.i(TAG, "medicine 가져오기");
        restAdapter.create(MuhanhyodoService.class).medicine(m_user_id, new Callback<List<Medicine>>() {
            @Override
            public void success(List<Medicine> medicine, Response response) {
                Medicine m;
                for (int i = 0; i < medicine.size(); i++) {
                    m = medicine.get(i);
                    Log.i(TAG, "[" + (i + 1) + "] " + m.getId() + " / " + m.getTitle() + " / " + m.getMorning() + " / " + m.getAfternoon() + " / " + m.getEvening() + " / " + m.getSound() + " / " + m.getMessage());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i(TAG, "약 가져오기 에러");
            }
        });

        Log.i(TAG, "users 가져오기");
        restAdapter.create(MuhanhyodoService.class).user(new Callback<List<User>>() {
            @Override
            public void success(List<User> user, Response response) {
                User u;
                for (int i = 0; i < user.size(); i++) {
                    u = user.get(i);
                    Log.i(TAG, "[" + (i + 1) + "] " + u.getName() + " / " + u.getTel());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i(TAG, " user 명단 가져오기 에러  ");
            }
        });

        Log.i(TAG, "family 가져오기");
        restAdapter.create(MuhanhyodoService.class).family(m_user_id, new Callback<List<Family>>() {
            @Override
            public void success(List<Family> family, Response response) {
                Family f;
                for (int i = 0; i < family.size(); i++) {
                    f = family.get(i);
                    Log.i(TAG, "[" + (i + 1) + "] " + f.getName() + " / " + f.getTel());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i(TAG, " 가족 가져오기 에러  ");
            }
        });

        Log.i(TAG, "notice 가져오기");
        restAdapter.create(MuhanhyodoService.class).notice(m_user_id, new Callback<List<Notice>>() {
            @Override
            public void success(List<Notice> notice, Response response) {
                Notice n;
                for (int i = 0; i < notice.size(); i++) {
                    n = notice.get(i);
                    Log.i(TAG, "[" + (i + 1) + "] " + n.getId() + " / " + n.getTitle());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i(TAG, "공지 가져오기 에러 ");
            }
        });


        restAdapter.create(MuhanhyodoService.class).normal(m_user_id, new Callback<List<Normal>>() {
            @Override
            public void success(List<Normal> normal, Response response) {
                Log.i(TAG, "normal 가져오기");
                Normal m;
                for (int i = 0; i < normal.size(); i++) {
                    m = normal.get(i);
                    Log.i(TAG, "[" + (i + 1) + "] " + m.getId() + " / " + m.getTitle() + " / " + m.getChk());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i(TAG, "normal 가져오기");
                Log.i(TAG, " 일반 메모 가져오기 에러 ");
            }
        });


        User user = new User("미령할머니", "010-1234-1232");
        restAdapter.create(MuhanhyodoService.class).createUser(user, new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                Log.d(TAG, "user 추가하기");
                Log.d(TAG, user.toString() );
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "user 추가하기 에러!" + error.getMessage() );
            }
        });

        TypedFile typedFile = new TypedFile(
                "multipart/form-data",
                new File(Environment.getExternalStorageDirectory() + "/0_1445157306771420.mp3")
        );
        Medicine medicine = new Medicine(0, "아스피린", 1, 1, 1, "", "아스피린 드세요~", 0);
        restAdapter.create(MuhanhyodoService.class).createMedicine(
                typedFile,
                medicine.getTitle(),
                medicine.getMorning(),
                medicine.getAfternoon(),
                medicine.getEvening(),
                medicine.getMessage(),
                medicine.getUser_id(),
                new Callback<Medicine>() {
            @Override
            public void success(Medicine medicine, Response response) {
                Log.d(TAG, "약 추가하기");
                Log.d(TAG, medicine.getMessage());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "약 추가하기 에러!" + error.getMessage());
            }
        });

      }
    }
