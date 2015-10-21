package kr.hs.emirim.app2015.muhanhyodo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.content.PermissionChecker.checkCallingOrSelfPermission;
import static android.support.v4.content.PermissionChecker.checkSelfPermission;

/**
 * Created by Student on 2015-10-21.
 */
public class AddressAdapter extends BaseAdapter {
    private static String TAG = "Adapter_Address";
    private Address mAddress;
    private Context mContext;
    private List<Address> mUserDatas;

    public AddressAdapter(Context aContext, List<Address> a) {
        mContext = aContext;
        mUserDatas = a;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout tmpLL;
        Log.d(TAG, "getView호출");
        if (convertView == null) {
            tmpLL = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.activity_address_list_item, parent, false);

        } else {
            tmpLL = (LinearLayout) convertView;
        }
        //ImageButton btnig=(ImageButton)tmpLL.findViewById(R.id.btn_send);
        TextView tmpTV = (TextView) tmpLL.findViewById(R.id.textView1);
        mAddress = mUserDatas.get(position);
        Log.d(TAG, "아이템 생성 : " + mAddress.toString());
        tmpTV.setText(mAddress.getName());
        Button btn = (Button) tmpLL.findViewById(R.id.btn_send);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 터치 시 해당 아이템 이름 출력
                String phone = "tel:" + mAddress.getTel();
                Intent myIntent = new Intent(Intent.ACTION_CALL, Uri.parse(phone));

                if (checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    mContext.startActivity(myIntent);
                    // TODO: Consider calling
                    //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }

            }
        });

        TextView tmpTV2=(TextView)tmpLL.findViewById(R.id.textView2);
        tmpTV2.setText(mAddress.getAddress());
        return tmpLL;
    }
}
