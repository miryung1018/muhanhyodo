package kr.hs.emirim.app2015.muhanhyodo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Student on 2015-10-22.
 */
public class NormalAdapter extends BaseAdapter {
    private static String TAG="ListView_Adapter";
    private Normal mNormal;
    private Context mContext;
    public List<Normal> mNormals;


    public NormalAdapter(Context aContext, List<Normal> normals) {
        mContext = aContext;
        mNormals = normals;
    }


    @Override
    public int getCount() {
//        return mTodoDatas.size();
        return  mNormals.size();
    }

    @Override
    public Object getItem(int position) {
        return mNormals.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout tmpLL;

        if(convertView==null)
        {
            tmpLL=(LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.activity_todo_list_item,parent,false);
        }
        else
        {
            tmpLL=(LinearLayout)convertView;
        }

/*
        if( position >= mFixed.size())
        {*/
        //checked
        CheckBox DeadLineCb=(CheckBox)tmpLL.findViewById(R.id.CbDeadLine);
        TextView tmpTV=(TextView)tmpLL.findViewById(R.id.textView1);
            mNormal=mNormals.get(position);
        Log.d(TAG, "아이템 생성 : " + mNormal.toString());
        Log.d(TAG, "getView호출");
        tmpTV.setText(mNormal.getTitle());
        DeadLineCb.setVisibility(View.VISIBLE);
/*


        }
        else
        {
            // fixed
            CheckBox DeadLineCb=(CheckBox)tmpLL.findViewById(R.id.CbDeadLine);
            TextView tmpTV=(TextView)tmpLL.findViewById(R.id.textView1);
            mTodo=mTodoDatas.get(position);
            Log.d(TAG, "아이템 생성 : " + mTodo.toString());
            Log.d(TAG, "getView호출");
            tmpTV.setText(mTodo.getTitle());
            DeadLineCb.setVisibility(View.INVISIBLE);
        }


*/


       /* if(mTodo.getChecked())
        {
            DeadLineCb.setVisibility(View.INVISIBLE);
//            tmpLL.setEnabled(false);
        }
        else
        {
            DeadLineCb.setVisibility(View.VISIBLE);
//            tmpLL.setClickable(true);
        }*/


        return tmpLL;


        //ImageButton btnig=(ImageButton)tmpLL.findViewById(R.id.btn_send);

        /*RadioButton
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // 터치 시 해당 아이템 이름 출력
                String phone= "tel:" + mUser.getPhoneNumber();
                Intent myIntent = new Intent(Intent.ACTION_CALL, Uri.parse(phone));
                mContext.startActivity(myIntent);
            }
        });*/


    }
}
