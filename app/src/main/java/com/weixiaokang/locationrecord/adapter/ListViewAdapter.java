package com.weixiaokang.locationrecord.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.weixiaokang.locationrecord.MyActivity;
import com.weixiaokang.locationrecord.R;
import com.weixiaokang.locationrecord.database.DBHelper;
import com.weixiaokang.locationrecord.util.Constants;
import com.weixiaokang.locationrecord.util.LogUtil;

import java.util.LinkedList;

public class ListViewAdapter extends BaseAdapter {
    public LinkedList<String> list;
    private Context mContext;
    private float mDownX, mUpX;
    private Button BtnDelete;
    private boolean IsShowButton;
    private DBHelper dbHelper;

    public ListViewAdapter(Context context, LinkedList<String> list) {
        this.list = list;
        this.mContext = context;
        dbHelper = new DBHelper(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int Index) {
        return list.get(Index);
    }

    @Override
    public long getItemId(int Index) {
        return Index;
    }

    @Override
    public View getView(final int mPosition, View mConvertView, ViewGroup parent) {
        ViewHolder mHolder = null;
        if (mConvertView == null) {
            mConvertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_list, null);
            mHolder = new ViewHolder();
            mHolder.Header = (ImageView) mConvertView.findViewById(R.id.list_image);
            mHolder.Content = (TextView) mConvertView.findViewById(R.id.list_tv);
            mHolder.Delete = (Button) mConvertView.findViewById(R.id.list_button);
            mHolder.Delete.setVisibility(View.INVISIBLE);
            mConvertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) mConvertView.getTag();
        }
        mConvertView.setOnTouchListener(mTouchListener);
        mHolder.Delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (BtnDelete != null) {
                    BtnDelete.setVisibility(View.INVISIBLE);
                    list.remove(mPosition);
                    notifyDataSetChanged();
                }
            }
        });

        mHolder.Content.setText((CharSequence) list.get(mPosition));
        return mConvertView;
    }

    OnTouchListener mTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View mView, MotionEvent mEvent) {
            final ViewHolder holder = (ViewHolder) mView.getTag();
            BtnDelete = holder.Delete;
            switch (mEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mDownX = mEvent.getX();

                    break;
                case MotionEvent.ACTION_UP:
                    mUpX = mEvent.getX();

                    if (BtnDelete != null) {
                        if (Math.abs(mDownX - mUpX) > 25) {
                            LogUtil.i(Constants.DATA, "-->bad");
                            if (IsShowButton) {
                                HideAnimation(holder.Delete);
                                holder.Delete.setVisibility(View.INVISIBLE);
                                IsShowButton = false;
                            } else {
                                ShowAnimation(holder.Delete);
                                holder.Delete.setVisibility(View.VISIBLE);
                                IsShowButton = true;
                            }

                            BtnDelete = holder.Delete;
                        }
                    }
                    if (!IsShowButton && Math.abs(mDownX - mUpX) < 25) {
                        LogUtil.i(Constants.DATA, "-->good");
                        String text = holder.Content.getText().toString();
                        String longtitude = text.substring(23, text.lastIndexOf(","));
                        String latitude = text.substring(text.lastIndexOf(":") + 1, text.length());
                        double a = Double.parseDouble(longtitude);
                        double b = Double.parseDouble(latitude);
                        LogUtil.i(Constants.DATA, " " + a + " " + b);
                        Intent intent = new Intent(mContext, MyActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putDouble("longtitude", a);
                        bundle.putDouble("latitude", b);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }
                    break;
            }
            return true;
        }
    };

    public void ShowAnimation(View v) {
        Animation Ani_Alpha = new AlphaAnimation(0.1f, 1.0f);
        Ani_Alpha.setDuration(500);
        v.setAnimation(Ani_Alpha);

    }

    public void HideAnimation(View v) {
        Animation Ani_Alpha = new AlphaAnimation(1.0f, 0.1f);
        Ani_Alpha.setDuration(500);
        v.setAnimation(Ani_Alpha);
    }


    static class ViewHolder {
        ImageView Header;
        TextView Content;
        Button Delete;
    }

}