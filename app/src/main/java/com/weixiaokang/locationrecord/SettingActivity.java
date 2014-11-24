package com.weixiaokang.locationrecord;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.weixiaokang.locationrecord.util.SharedPreferencesUtil;
import com.weixiaokang.locationrecord.util.ToastUtil;


public class SettingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Button buSetting = (Button) findViewById(R.id.bu_setting);
        final EditText editText = (EditText) findViewById(R.id.ed_setting);
        String s = SharedPreferencesUtil.getNumber(this);
        editText.setHint(s);

        buSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtil.numberFilter(SettingActivity.this, editText.getText().toString().trim());
//                ToastUtil.showInCenter(SettingActivity.this, "设置完成");
                Toast.makeText(SettingActivity.this, "设置完成", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setting, menu);
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
