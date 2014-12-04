package com.weixiaokang.rescueteam;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


public class SettingActivity extends ActionBarActivity {

    private EditText editText, bxgsEt;
    private Button button;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private RadioGroup radioGroup;
    private RadioButton radioButton, radioButton1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        editText = (EditText) findViewById(R.id.num_et);
        bxgsEt = (EditText) findViewById(R.id.bxgs_et);
        button = (Button) findViewById(R.id.num_but);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        radioButton = (RadioButton) findViewById(R.id.open);
        radioButton1 = (RadioButton) findViewById(R.id.close);
        sharedPreferences = getSharedPreferences("camera", MODE_PRIVATE);
        boolean flag = sharedPreferences.getBoolean("camera", true);
        if (flag) {
            radioButton.setChecked(true);
            radioButton1.setChecked(false);
        } else {
            radioButton.setChecked(false);
            radioButton1.setChecked(true);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                sharedPreferences = getSharedPreferences("camera", MODE_PRIVATE);
                editor = sharedPreferences.edit();
                switch (checkedId) {
                    case R.id.open:
                      editor.putBoolean("camera", true);
                        break;
                    case R.id.close:
                        editor.putBoolean("camera", false);
                        break;
                }
                editor.apply();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = editText.getText().toString().trim();
                String bsgs = bxgsEt.getText().toString().trim();

                if (string.equals("")) {
                    string = "15951911977";
                }

                if (bsgs.equals("")) {
                    bsgs = "955159";
                }

                sharedPreferences = getSharedPreferences("number", MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putString("num", string);
                editor.putString("bxgs_number", bsgs);
                editor.apply();
                Toast.makeText(SettingActivity.this, "保存成功", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting, menu);
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