package com.weixiaokang.locationrecord;

import android.app.ActionBar;
import android.app.ListActivity;
import android.os.Bundle;

import com.weixiaokang.locationrecord.adapter.ListViewAdapter;
import com.weixiaokang.locationrecord.database.DBHelper;
import com.weixiaokang.locationrecord.database.LocationData;

import java.util.ArrayList;
import java.util.LinkedList;

public class DataActivity extends ListActivity {


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
    }
}
