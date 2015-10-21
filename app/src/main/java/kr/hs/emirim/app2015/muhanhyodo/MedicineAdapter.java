package kr.hs.emirim.app2015.muhanhyodo;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Student on 2015-10-22.
 */
public class MedicineAdapter extends BaseAdapter {

    private static String TAG="Adapter_Medicine";
    //static final String RECORDED_FILE = "/sdcard/muhanrecord.mp4";
    private Medicine mMed;
    MediaPlayer player;
    private Context mContext;
    private List<Medicine> mUserDatas;
    MediaPlayer mediaPlayer;
    public MedicineAdapter(Context aContext, List<Medicine> aUserData) {
        mContext = aContext;
        mUserDatas = aUserData;
    }

    @Override
    public int getCount() {
        return mUserDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mUserDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        LinearLayout tmpLL;
        Log.d(TAG, "getView호출");
        if(convertView==null)
        {
            tmpLL=(LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.activity_medicine_list_item, parent,false);
        }
        else
        {
            tmpLL=(LinearLayout)convertView;
        }
        //ImageButton btnig=(ImageButton)tmpLL.findViewById(R.id.btn_send);
        TextView tmpTV=(TextView)tmpLL.findViewById(R.id.MedName);
        mMed=mUserDatas.get(position);
        Log.d(TAG, "아이템 생성 : " + mMed.toString());
        tmpTV.setText(mMed.getTitle());
        Button btn=(Button)tmpLL.findViewById(R.id.btn_play);
        btn.setOnClickListener(new View.OnClickListener() {

            MediaPlayer mediaPlayer = new MediaPlayer();
            AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

            @Override
            public void onClick(View v) {

                try {
                    // audioManager.setStreamVolume(볼륨컨트롤, 뷰륨크기, 볼륨상태(audioManager.FLAG...으로 시작하는 인자들...) );

                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
                    audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM), 0);

                    //대상 파일 지정
                    mediaPlayer.setVolume(1.0f, 1.0f);
                    mediaPlayer.setDataSource(mUserDatas.get(position).getSound());
                    Log.d(TAG, "재생: " +mUserDatas.get(position).getSound());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                }catch(Exception e){
                    e.printStackTrace();
                }


//                try {
//                    playAudio(RECORDED_FILE);
//
//                    Toast.makeText(mContext, "음악파일 재생 시작됨.", Toast.LENGTH_SHORT).show();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        });

        TextView tmpTV2=(TextView)tmpLL.findViewById(R.id.MedDetale);
        tmpTV2.setText(mMed.getMessage());
        return tmpLL;
    }

//
//
//    private void playAudio(String url) throws Exception{
//        killMediaPlayer();
//
//        player = new MediaPlayer();
//        player.setDataSource(url);
//        player.prepare();
//        player.start();
//    }
//    private void killMediaPlayer() {
//        if(player != null){
//            try {
//                player.release();
//            } catch(Exception e){
//                e.printStackTrace();
//            }
//        }
//
//    }
}
