package com.weixiaokang.rescueteam;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.weixiaokang.rescueteam.fragment.NumberFragment;
import com.weixiaokang.rescueteam.util.ActionBarUtil;


public class HelpActivity extends ActionBarActivity {

    private TextView textView;
    private Spinner spinner;
    private NumberFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        textView = (TextView) findViewById(R.id.content_tv);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        textView.setText(R.string.help_one_s);
                        break;
                    case 1:
                        textView.setText(R.string.help_two_s);
                        break;
                    case 2:
                        textView.setText(R.string.help_three_s);
                        break;
                    case 3:
                        textView.setText(R.string.help_four_s);
                        break;
                    case 4:
                        textView.setText(R.string.help_five_s);
                        break;
                    case 5:
                        textView.setText(R.string.help_six_s);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ActionBarUtil.hide(this);

        fragment = new NumberFragment();
        fragment.show(getSupportFragmentManager(), "TAG");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!fragment.isHidden()) {
                    fragment.dismiss();
                }
            }
        }, 10000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_help, menu);
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
