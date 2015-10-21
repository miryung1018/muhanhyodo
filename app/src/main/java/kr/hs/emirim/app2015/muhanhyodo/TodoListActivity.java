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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

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

public class TodoListActivity extends AppCompatActivity {
    private static final String TAG = "무한효도:AddressListAct";
    RestAdapter restAdapter;
    int m_user_id = -1;
    boolean m_isGrand = true;

    ListView m_notice_listView;
    ListView m_normal_listView;
    int mPosition;
    List<Notice> m_notices;
    List<Normal> m_normals;

    NoticeAdapter m_notice_adapter;
    NormalAdapter m_normal_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
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

        m_notice_listView = (ListView) findViewById(R.id.myListView);
        m_normal_listView= (ListView) findViewById(R.id.myListView02);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(m_isGrand){
            //감추고
            fab.setVisibility(View.INVISIBLE);
        }else{
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context mContext = getApplicationContext();
                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.activity_todo_list_popup, (ViewGroup) findViewById(R.id.layout_dialog));

                    //여기서buttontest는 패키지이름
                    aDialog.setTitle("메모 입력");
                    aDialog.setView(layout);

                    final AlertDialog.Builder builder = aDialog.setPositiveButton("전송", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            EditText edit = (EditText) ((AlertDialog) dialog).findViewById(R.id.EtDetail);
                            RadioButton FixRbtn = (RadioButton) ((AlertDialog) dialog).findViewById(R.id.FixRbtn);
                            boolean Checked = FixRbtn.isChecked();//true면 고정된 알람
                            String str = edit.getText().toString();

                            if (FixRbtn.isChecked()) {
                                addData(new Notice(-1, str));
                            } else {
                                addData(new Normal(-1, str, (Checked) ? 1 : 0));
                            }

                            //입력된 값 지우기
                            // e1.setText("");
                            Toast.makeText(getApplicationContext(), "선택됨", Toast.LENGTH_SHORT).show();
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


        m_notice_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Context mContext = getApplicationContext();
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.activity_todo_list_select_popup, (ViewGroup) findViewById(R.id.layout_popup));
                Notice notice = m_notices.get(position);
                mPosition = position;
                //Log.d(TAG, "아이템 생성 : " + mUser.toString());
                TextView TodoTv = (TextView) (layout).findViewById(R.id.TodoTv);

                //여기서buttontest는 패키지이름
                aDialog.setTitle("공지 보기");
                aDialog.setView(layout);
                // Log.d(TAG, "이름 : " + mUser.getName());
                TodoTv.setText(notice.getTitle());

                aDialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //입력된 값 지우기

                        showDialog(1);

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

        m_normal_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context mContext = getApplicationContext();
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.activity_todo_list_select_popup, (ViewGroup) findViewById(R.id.layout_popup));
                Normal normal = m_normals.get(position);
                mPosition = position;
                //Log.d(TAG, "아이템 생성 : " + mUser.toString());
                TextView TodoTv = (TextView) (layout).findViewById(R.id.TodoTv);

                //여기서buttontest는 패키지이름
                aDialog.setTitle("자세히 보기");
                aDialog.setView(layout);
                // Log.d(TAG, "이름 : " + mUser.getName());
                TodoTv.setText(normal.getTitle());

                aDialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //입력된 값 지우기
                        // e1.setText("");

                        showDialog(2);

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


    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }


    private void getData(){
        /**
         * 통신 콜백 메서드 Callback<List<Address>> callback
         */

        Log.i(TAG, "notice 가져오기");
        restAdapter.create(MuhanhyodoService.class).notice(m_user_id, new Callback<List<Notice>>() {
            @Override
            public void success(List<Notice> notice, Response response) {
                m_notices = notice;
                m_notice_adapter = new NoticeAdapter(getApplicationContext(), m_notices);
                m_notice_listView.setAdapter(m_notice_adapter);
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
                m_normals = normal;
                m_normal_adapter = new NormalAdapter(getApplicationContext(), m_normals);
                m_normal_listView.setAdapter(m_normal_adapter);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i(TAG, "공지 가져오기 에러 ");
            }
        });

    }


    private void addData(Notice notice){

        Log.d(TAG, "추가되는 공지사항" + notice.getTitle());

        restAdapter.create(MuhanhyodoService.class).createNotice(m_user_id,
                notice.getTitle(), new Callback<Notice>() {
                    @Override
                    public void success(Notice notice, Response response) {
                        Log.d(TAG, "Notice 추가하기");
                        Log.d(TAG, notice.toString());
                        getData();
                        m_notice_adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(TAG, "Notice 추가하기 에러!" + error.getMessage());
                    }
                });
    }

    private void addData(Normal normal){

        Log.d(TAG, "추가되는 메모" + normal.getTitle());

        restAdapter.create(MuhanhyodoService.class).createNormal(m_user_id,
                normal.getTitle(), normal.getChk(), new Callback<Normal>() {
                    @Override
                    public void success(Normal normal, Response response) {
                        Log.d(TAG, "Normal 추가하기");
                        Log.d(TAG, normal.toString());
                        getData();
                        m_normal_adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(TAG, "Normal 추가하기 에러!" + error.getMessage());
                    }
                });
    }



    private void deleteData(Notice notice){

        Log.d(TAG, "삭제되는 공지사항" + notice.getTitle());

        restAdapter.create(MuhanhyodoService.class).deleteNotice(m_user_id, notice.getId(),
                notice.getTitle(), new Callback<Notice>() {
                    @Override
                    public void success(Notice notice, Response response) {
                        Log.d(TAG, "Notice 삭제하기");
                        Log.d(TAG, notice.toString());
                        getData();
                        m_notice_adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(TAG, "Notice 삭제하기 에러!" + error.getMessage());
                    }
                });
    }

    private void deleteData(Normal normal){

        Log.d(TAG, "삭제되는 메모" + normal.getTitle());

        restAdapter.create(MuhanhyodoService.class).deleteNormal(m_user_id,normal.getId(),
                normal.getTitle(), normal.getChk(), new Callback<Normal>() {
                    @Override
                    public void success(Normal normal, Response response) {
                        Log.d(TAG, "Normal 삭제하기");
                        Log.d(TAG, normal.toString());

                        getData();
                        m_normal_adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(TAG, "Normal 삭제하기 에러!" + error.getMessage());
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
                                deleteData(m_notices.get(mPosition));
                                getData();

                            }
                        }).setNegativeButton("아니요",null).create();
            case 2:
                return new AlertDialog.Builder(this).setMessage("정말 삭제 하시겠습니까?").setPositiveButton("네",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                deleteData(m_normals.get(mPosition));
                                getData();
                            }
                        }).setNegativeButton("아니요",null).create();

        }
        return null;
    }

}// class