package kr.hs.emirim.app2015.muhanhyodo;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.squareup.okhttp.OkHttpClient;

import java.sql.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class MainMenuActivity extends AppCompatActivity {
    private static final String TAG = "무한효도:MainMenuActivity";
    RestAdapter restAdapter;
    int m_user_id = -1;
    TextView m_user_name;
    TextView m_user_tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        //------------------------------------------------------------
        m_user_name = (TextView)findViewById(R.id.user_name);
        m_user_tel = (TextView)findViewById(R.id.user_tel);

        SharedPreferences prefs = getSharedPreferences("muhanhyodo", MODE_PRIVATE);
        m_user_id = prefs.getInt("user_id", 0);

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

        ((Button)findViewById(R.id.goTodo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, TodoListActivity.class);
                startActivity(intent);            }
        });

        ((Button)findViewById(R.id.goMed)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, MedicineListActivity.class);
                startActivity(intent);            }
        });

        ((Button)findViewById(R.id.goAdd)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, AddressListActivity.class);
                startActivity(intent);            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i(TAG, "user 가져오기");
        restAdapter.create(MuhanhyodoService.class).user(m_user_id, new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                Log.d(TAG, "User ID:" + m_user_id + " / name: " + user.getName() + " / tel: " + user.getTel());
                m_user_name.setText( "이름 : " +user.getName() );
                m_user_tel.setText( "전화번호 : " +user.getTel() );
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, " user 정보 가져오기 에러  ");
            }
        });
    }
}