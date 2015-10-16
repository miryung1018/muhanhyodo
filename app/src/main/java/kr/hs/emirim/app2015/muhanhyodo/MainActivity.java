package kr.hs.emirim.app2015.muhanhyodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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
        Log.i(TAG, "address 가져오기");
        restAdapter.create(MuhanhyodoService.class).address(new Callback<List<Address>>() {
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
                Log.i(TAG, "무슨 영문인지 이해할 수 없음");
            }
        });

        Log.i(TAG, "address 가져오기");
        restAdapter.create(MuhanhyodoService.class).medicine(new Callback<List<Medicine>>() {
            @Override
            public void success(List<Medicine> medicine, Response response) {
                Medicine m;
                for (int i = 0; i < medicine.size(); i++) {
                    m = medicine.get(i);
                    Log.i(TAG, "[" + (i + 1) + "] " + m.getId() + " / " + m.getTitle() + " / " + m.getMorning() + " / " + m.getAfternoon() + " / " + m.getEvening() + " / " + m.getSound());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i(TAG, "무슨 영문인지 이해할 수 없음");
            }
        });

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
                Log.i(TAG, "무슨 영문인지 이해할 수 없음");
            }
        });

        restAdapter.create(MuhanhyodoService.class).family(new Callback<List<Family>>() {
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
                Log.i(TAG, "무슨 영문인지 이해할 수 없음");
            }
        });

        restAdapter.create(MuhanhyodoService.class).notice(new Callback<List<Notice>>() {
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
                Log.i(TAG, "무슨 영문인지 이해할 수 없음");
            }
        });

        restAdapter.create(MuhanhyodoService.class).normal(new Callback<List<Normal>>() {
            @Override
            public void success(List<Normal> normal, Response response) {
                Normal m;
                for (int i = 0; i < normal.size(); i++) {
                    m = normal.get(i);
                    Log.i(TAG, "[" + (i + 1) + "] " + m.getId() + " / " + m.getTitle() + " / " + m.getChk());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i(TAG, "무슨 영문인지 이해할 수 없음");
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
