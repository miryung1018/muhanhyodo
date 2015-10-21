package kr.hs.emirim.app2015.muhanhyodo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.io.IOException;
import java.sql.Date;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedFile;

public class InputGrandMotherDataActivity extends AppCompatActivity {
    private static final String TAG = "무한효도:InputGrandMotherDataActivity";
    RestAdapter restAdapter;

     EditText mName;
    EditText mTel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_grand_mother_data);

        mName = (EditText) findViewById(R.id.et_name);
        mTel = (EditText) findViewById(R.id.et_tel);

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
        restAdapter = new RestAdapter.Builder()
                //로그 레벨 설정
                .setLogLevel(RestAdapter.LogLevel.FULL)
                        //BASE_URL 설정
                .setEndpoint(MuhanhyodoService.API_URL)
                        //OkHttpClient 이용
                .setClient(new OkClient(new OkHttpClient()))
                        //Gson Converter 설정
                .setConverter(new GsonConverter(gson))
                .build();
        //------------------------------------------------------------

        Button btn = (Button) findViewById(R.id.btnSave);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String iid = InstanceID.getInstance(InputGrandMotherDataActivity.this).getId();
                String token = null;
//                try {
//                    InstanceID instanceID = InstanceID
//                            .getInstance(getApplicationContext());
//                    token = instanceID.getToken("1:309962768473:android:326586b7c62c4e71",
//                            GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                User user = new User( -1,  mName.getText().toString() , mTel.getText().toString(), iid);
                addData(user);

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void addData(User user){
        // Medicine medicine = new Medicine(0, "아스피린", 1, 1, 1, "", "아스피린 드세요~", 0);
        restAdapter.create(MuhanhyodoService.class).createUser(
                user.getName(),
                user.getTel(),
                user.getIid(),
                new Callback<User>() {
                    @Override
                    public void success(User user, Response response) {

                            SharedPreferences prefs = getSharedPreferences("muhanhyodo", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putInt("user_id", user.getId());
                            editor.commit();
                            Intent intent = new Intent(InputGrandMotherDataActivity.this, MainMenuActivity.class);
                            startActivity(intent);
                            finish();
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });

    }
}
