package kr.hs.emirim.app2015.muhanhyodo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SelectModeActivity extends AppCompatActivity {
    RadioButton rb1;
    RadioButton rb2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mode);

        rb1 = (RadioButton) findViewById(R.id.radio_teacher);
        rb2 = (RadioButton) findViewById(R.id.radio_student);
        rb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "어르신 모드를 선택하셨습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        rb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "보호자 모드를 선택해 주세요", Toast.LENGTH_SHORT).show();
            }
        });
        Button btn = (Button)findViewById(R.id.btnOK);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rb1.isChecked()){

                    SharedPreferences prefs = getSharedPreferences("muhanhyodo", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("isGrand", true);
                    editor.commit();
                    Intent intent = new Intent(SelectModeActivity.this, InputGrandMotherDataActivity.class);
                    startActivity(intent);
                    finish();
                }else if(rb2.isChecked()){
                    SharedPreferences prefs = getSharedPreferences("muhanhyodo", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("isGrand", false);
                    editor.commit();
                    Intent intent = new Intent(SelectModeActivity.this, InputGrandMotherDataActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(getBaseContext(), "모드를 선택해 주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
