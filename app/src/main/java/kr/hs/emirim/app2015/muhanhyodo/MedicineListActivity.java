package kr.hs.emirim.app2015.muhanhyodo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class MedicineListActivity extends AppCompatActivity {
    private static final String TAG = "무한효도:MedicineListAct";
    RestAdapter restAdapter;
    int m_user_id = -1;
    boolean m_isGrand = true;
    static final String RECORDED_FILE =  "muhanrecord.mp4";
    ListView listView;
    int mPosition;
    Medicine mMedicne;
    List<Medicine> mMedicines;
    MedicineAdapter adapter;
    MediaRecorder recorder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_list);

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
        }else {
            //작동하게
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context mContext = getApplicationContext();
                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.activity_medicine_list_popup, (ViewGroup) findViewById(R.id.layout_root));
                    (layout.findViewById(R.id.btnRecord)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d(TAG,"record Onclick");
                            if (recorder != null) {
                                recorder.stop();
                                recorder.release();
                                recorder = null;
                            }// TODO Auto-generated method stub
                            recorder = new MediaRecorder();
                            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                            recorder.setOutputFile(Environment.getExternalStorageDirectory() + "/" + RECORDED_FILE);
                            try {
                                Toast.makeText(getApplicationContext(), "녹음을 시작합니다.", Toast.LENGTH_LONG).show();
                                recorder.prepare();
                                recorder.start();
                            } catch (Exception ex) {
                                Log.e("SampleAudioRecorder", "Exception : ", ex);
                            }
                        }
                    });
                    (layout.findViewById(R.id.btnRecordStop)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d(TAG, "온클릭2" );
                            if (recorder == null)
                                return;

                            recorder.stop();
                            recorder.release();
                            recorder = null;

                            Toast.makeText(getApplicationContext(), "녹음이 중지되었습니다.", Toast.LENGTH_LONG).show();
                        }
                    });


                    //여기서buttontest는 패키지이름
                    aDialog.setTitle("약 알림 추가하기");
                    aDialog.setView(layout);

                    aDialog.setPositiveButton("전송", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            EditText edit = (EditText) ((AlertDialog) dialog).findViewById(R.id.EtmName);
                            EditText edit2 = (EditText) ((AlertDialog) dialog).findViewById(R.id.Etdetail);
                            String str2 = edit2.getText().toString();
                            String str = edit.getText().toString();
                            Button btn1 = (Button) ((AlertDialog) dialog).findViewById(R.id.btnRecord);
                            Button btn2 = (Button) ((AlertDialog) dialog).findViewById(R.id.btnRecordStop);

                           addData(new Medicine(-1, str, 0, 0, 0, "", str2, m_user_id));

                            //adapter.notifyDataSetChanged();
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
            View layout = inflater.inflate(R.layout.activity_medicine_list_item_select_popup, (ViewGroup) findViewById(R.id.layout_m_item));
            Medicine mMed = mMedicines.get(position);
            mPosition = position;
            Log.d(TAG, "아이템 생성 : " + mMed.toString());
            TextView nameTv = (TextView) (layout).findViewById(R.id.nameTv);
            TextView detailTv = (TextView) (layout).findViewById(R.id.detailTv);


            //여기서buttontest는 패키지이름
            aDialog.setTitle("약 정보 자세히 보기");
            aDialog.setView(layout);
            Log.d(TAG, "이름 : " + mMed.getTitle());
            nameTv.setText(mMed.getTitle());
            detailTv.setText(mMed.getMessage());
            detailTv.setMovementMethod(new ScrollingMovementMethod());

            aDialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //입력된 값 지우기
                    // e1.setText("");

                    showDialog(1);
                    //getWindow().setBackgroundDrawable(new PaintDrawable(Color.TRANSPARENT));

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
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 1:
                return new AlertDialog.Builder(this).setMessage("정말 삭제 하시겠습니까?").setPositiveButton("네",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                               // list.remove(mPosition);
                                //adapter.notifyDataSetChanged();
                                deleteData(mMedicines.get(mPosition));
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
        Log.i(TAG, "medicine 가져오기");
        restAdapter.create(MuhanhyodoService.class).medicine(m_user_id, new Callback<List<Medicine>>() {
            @Override
            public void success(List<Medicine> medicine, Response response) {
                mMedicines = medicine;
                adapter = new MedicineAdapter(getApplicationContext(),medicine );
                listView.setAdapter(adapter);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i(TAG, "약 가져오기 에러");
            }
        });
    }
    private void deleteData(Medicine medicine){

        Log.d(TAG, "삭제되 약" + medicine.getTitle());

        restAdapter.create(MuhanhyodoService.class).deleteMedicine
                (m_user_id,
                        medicine.getId(),
                        medicine.getTitle(),
                        medicine.getMorning(),
                        medicine.getAfternoon(),
                        medicine.getEvening(),
                        medicine.getSound(),
                        medicine.getMessage(),
                        new Callback<Medicine>() {
                            @Override
                            public void success(Medicine medicine, Response response) {
                                Log.d(TAG, "medicine 삭제하기");
                                Log.d(TAG, medicine.toString());
                                getData();
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.d(TAG, " medicine삭제하기 에러!" + error.getMessage());
                            }
                        });
    }

    private void addData(Medicine medicine){

        Log.d(TAG, "추가 약" + medicine.getTitle());

        TypedFile typedFile = new TypedFile(
                "multipart/form-data",
                new File(Environment.getExternalStorageDirectory() + "/" + RECORDED_FILE)
        );
       // Medicine medicine = new Medicine(0, "아스피린", 1, 1, 1, "", "아스피린 드세요~", 0);
        restAdapter.create(MuhanhyodoService.class).createMedicine(
                medicine.getUser_id(),
                typedFile,
                medicine.getTitle(),
                medicine.getMorning(),
                medicine.getAfternoon(),
                medicine.getEvening(),
                medicine.getMessage(),
                new Callback<Medicine>() {
                    @Override
                    public void success(Medicine medicine, Response response) {
                        Log.d(TAG, "약 추가하기");
                        Log.d(TAG, medicine.getMessage());

                        getData();
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(TAG, "약 추가하기 에러!" + error.getMessage());
                    }
                });

    }
}
