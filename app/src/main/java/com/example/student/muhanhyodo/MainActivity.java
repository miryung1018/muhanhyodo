package com.example.student.muhanhyodo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;

import java.sql.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class MainActivity extends AppCompatActivity {
    public static String TAG = "무한효도메인";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
//        RestAdapter restAdapter = new RestAdapter.Builder()
//                //로그 레벨 설정
//                .setLogLevel(RestAdapter.LogLevel.FULL)
//                        //BASE_URL 설정
//                .setEndpoint(GitHubService.API_URL)
//                        //Gson Converter 설정
//                .setConverter(new GsonConverter(gson))
//                .build();
        RestAdapter restAdapter = new RestAdapter.Builder()
                //로그 레벨 설정
                .setLogLevel(RestAdapter.LogLevel.FULL)
                        //BASE_URL 설정
                .setEndpoint(MuhanhyodoService.API_URL)
                        //Gson Converter 설정
                .setConverter(new GsonConverter(gson))
                .build();
        /**
         * 통신 콜백 메서드 Callback<List<Address>> callback
         */
        restAdapter.create(MuhanhyodoService.class).address(new Callback<List<Address>>() {
            @Override
            public void success(List<Address> addresses, Response response) {
                Address a;
                for( int i = 0; i < addresses.size(); i ++){
                    a = addresses.get(i);
                    Log.i(TAG, "[" + (i+1) + "] " + a.getName() + " / " + a.getTel() + " / " + a.getId() );
                }
            }

            @Override
            public void failure(RetrofitError error) {
                    Log.i(TAG, "무슨말인지 이해할 수 없음");
            }
        });
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
}
