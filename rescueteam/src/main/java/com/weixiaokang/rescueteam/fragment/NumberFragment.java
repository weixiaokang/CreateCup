package com.weixiaokang.rescueteam.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.weixiaokang.rescueteam.R;

public class NumberFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("number", Context.MODE_PRIVATE);
        final String[] number = new String[4];
        number[0] = sharedPreferences.getString("num", "15951911977");
        number[1] = sharedPreferences.getString("pf_number", "110");
        number[2] = sharedPreferences.getString("at_number", "120");
        number[3] = sharedPreferences.getString("bxgs_number", "95519");
        String strings[] = new String[4];
        strings[0] = number[0];
        strings[1] = number[1] + " (警察)";
        strings[2] = number[2] + " (医院)";
        strings[3] = number[3] + " (保险公司)";
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("选择求助号码")
                .setItems(strings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String num = number[which];
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.CALL");
                        intent.setData(Uri.parse("tel:" + num));
                        startActivity(intent);
                    }
                });
        return builder.create();
    }
}
