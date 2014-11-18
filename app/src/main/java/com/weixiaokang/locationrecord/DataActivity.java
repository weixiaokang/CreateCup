package com.weixiaokang.locationrecord;

import android.app.ActionBar;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.weixiaokang.locationrecord.adapter.ListViewAdapter;
import com.weixiaokang.locationrecord.database.DBHelper;
import com.weixiaokang.locationrecord.database.LocationData;
import com.weixiaokang.locationrecord.util.Constants;
import com.weixiaokang.locationrecord.util.LogUtil;

import java.util.ArrayList;
import java.util.LinkedList;

public class DataActivity extends ListActivity implements View.OnTouchListener {


    private ListView listView;
    private int x, y, position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setIcon(R.drawable.air_nut_tips_in);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        DBHelper dbHelper = new DBHelper(this);
        ArrayList<LocationData> list = dbHelper.queryAll();
        LinkedList<String> a = new LinkedList<String>();
        for (int i = 0; i < list.size(); i++) {
            LocationData locationData = list.get(i);
            a.add(locationData.getTime() + "\n" + "经度:" + locationData.getLongitude() + ",纬度:" + locationData.getLatitude());
        }
        ListViewAdapter adapter = new ListViewAdapter(this, a);
        setListAdapter(adapter);
        listView = getListView();
        listView.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LogUtil.i(Constants.TEST, "-->onTouch: " + event.getX());
                x = (int) event.getX();
                y = (int) event.getY();
                position = listView.pointToPosition(x, y);
                if (position == -1) {
                    position = listView.getChildCount() - 1;
                }
            case MotionEvent.ACTION_UP:
                LogUtil.i(Constants.TEST, "-->onTouch: " + event.getX());
                ((ListViewAdapter)listView.getAdapter()).setOnMyTouchListener(listView.getChildAt(position), event);
                break;
        }
        return false;
    }
}
