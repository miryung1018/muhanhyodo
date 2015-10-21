package kr.hs.emirim.app2015.muhanhyodo;

import android.content.Context;
import android.graphics.Color;
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
public class NoticeAdapter extends BaseAdapter {

    private static String TAG="ListView_Adapter";
    private Notice mNotice;
    private Context mContext;
    public List<Notice> mNotices;

    public NoticeAdapter(Context aContext, List<Notice> notices) {
        mContext = aContext;
        mNotices = notices;
    }

    @Override
    public int getCount() {
//        return mTodoDatas.size();
        return  mNotices.size();
    }

    @Override
    public Object getItem(int position) {
        return mNotices.get(position);
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
        tmpTV.setTextColor(Color.BLACK);
        mNotice = mNotices.get(position);
        Log.d(TAG, "아이템 생성 : " + mNotice.toString());
        Log.d(TAG, "getView호출");
        tmpTV.setText(mNotice.getTitle());
        DeadLineCb.setVisibility(View.INVISIBLE);
        return tmpLL;

    }
}
