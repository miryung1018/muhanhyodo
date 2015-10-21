package kr.hs.emirim.app2015.muhanhyodo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.squareup.okhttp.OkHttpClient;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class AddressListActivity extends AppCompatActivity {
    private static final String TAG = "무한효도:AddressListAct";
    RestAdapter restAdapter;
    int m_user_id = -1;
    boolean m_isGrand = true;

    ListView listView;
    int mPosition;
    List<Address> mAddress;
    AddressAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
//------------------------------------------------------------
        SharedPreferences prefs = getSharedPreferences("muhanhyodo", MODE_PRIVATE);
        m_user_id = prefs.getInt("user_id", 0);
        m_isGrand = prefs.getBoolean("isGrand", false);

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
        final AlertDialog.Builder aDialog = new AlertDialog.Builder(this);
        listView=(ListView)findViewById(R.id.myListView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(m_isGrand){
            //감추고
            fab.setVisibility(View.INVISIBLE);
        }else{
            //작동하게
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context mContext = getApplicationContext();
                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.activity_address_list_popup, (ViewGroup) findViewById(R.id.layout_root));

                    //여기서buttontest는 패키지이름
                    aDialog.setTitle("주소록 추가");
                    aDialog.setView(layout);

                    aDialog.setPositiveButton("전송", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            EditText edit = (EditText) ((AlertDialog) dialog).findViewById(R.id.image);
                            EditText edit2 = (EditText) ((AlertDialog) dialog).findViewById(R.id.text);
                            EditText edit3 = (EditText) ((AlertDialog) dialog).findViewById(R.id.phone);
                            String str2 = edit2.getText().toString();
                            String str = edit.getText().toString();
                            String str3 = edit3.getText().toString();

                            //m_address.add(new User(str, str2, str3));
                            addData(new Address(-1, str, str3, str2));

                            //입력된 값 지우기
                            // e1.setText("");

                        }
                    });
                    aDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog ad = aDialog.create();
                    ad.show();
                }
            });
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context mContext = getApplicationContext();
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.activity_address_list_item_select_popup, (ViewGroup) findViewById(R.id.layout_popup));
                Address address  = mAddress.get(position);
                mPosition = position;
                Log.d(TAG, "아이템 생성 : " + address.toString());
                TextView nameTv=(TextView)(layout).findViewById(R.id.nameTv);
                TextView addTv=(TextView)(layout).findViewById(R.id.addTv);

                //여기서buttontest는 패키지이름
                aDialog.setTitle("자세히 보기");
                aDialog.setView(layout);
                Log.d(TAG, "이름 : " + address.getName());
                nameTv.setText(address.getName());
                addTv.setText(address.getAddress());
                addTv.setMovementMethod(new ScrollingMovementMethod());

                if(!m_isGrand)
                {
                    aDialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //입력된 값 지우기
                            // e1.setText("");
                            showDialog(1);
                        }
                    });
                }

                aDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog ad = aDialog.create();
                ad.show();
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 1:
                return new AlertDialog.Builder(this).setMessage("정말 삭제 하시겠습니까?").setPositiveButton("네",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                               // mAddress.remove(mPosition);
                                deleteData(mAddress.get(mPosition));
                                getData();
                                adapter.notifyDataSetChanged();
                            }
                        }).setNegativeButton("아니요",null).create();
        }
        return null;
    }
    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData(){
        /**
         * 통신 콜백 메서드 Callback<List<Address>> callback
         */
        Log.i(TAG, "address 가져오기");
        restAdapter.create(MuhanhyodoService.class).address(m_user_id, new Callback<List<Address>>() {
            @Override
            public void success(List<Address> addresses, Response response) {
                mAddress = addresses;
                adapter = new AddressAdapter(getApplicationContext(), mAddress);
                listView.setAdapter(adapter);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i(TAG, "address 가져오기 에러 ");
            }
        });
    }

    private void addData(Address address){

        Log.d(TAG, "추가되는 주소" + address.getName());

        restAdapter.create(MuhanhyodoService.class).createAddress(m_user_id,
                address.getTel(), address.getName(), address.getAddress(), new Callback<Address>() {
                    @Override
                    public void success(Address address, Response response) {
                        Log.d(TAG, "address 추가하기");
                        Log.d(TAG, address.toString());
                        getData();
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(TAG, "user 추가하기 에러!" + error.getMessage());
                    }
                });
    }

    private void deleteData(Address address){

        Log.d(TAG, "삭제되는 주소" + address.getName() );

        restAdapter.create(MuhanhyodoService.class).deleteAddress(m_user_id, address.getId(),
                address.getTel(), address.getName(), address.getAddress(), new Callback<Address>() {
                    @Override
                    public void success(Address address, Response response) {
                        Log.d(TAG, "address 삭제하기");
                        Log.d(TAG, address.toString());
                        getData();
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(TAG, "address 삭제하기 에러!" + error.getMessage());
                    }
                });
    }
} // class
