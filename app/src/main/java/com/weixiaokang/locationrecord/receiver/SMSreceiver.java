package com.weixiaokang.locationrecord.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.weixiaokang.locationrecord.service.DataService;
import com.weixiaokang.locationrecord.util.DateUtil;
import com.weixiaokang.locationrecord.util.LogUtil;
import com.weixiaokang.locationrecord.util.SharedPreferencesUtil;

public class SMSreceiver extends BroadcastReceiver {

    private static final String action = "android.provider.Telephony.SMS_RECEIVED";
    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.i("momoda", "-->onReceive");
        if (intent.getAction().equals(action)) {
            Bundle bundle = intent.getExtras();
            Object pdus[] = (Object[]) bundle.get("pdus");
            SmsMessage smsMessages[] = new SmsMessage[pdus.length];
            for (int i = 0; i < pdus.length; i++) {
                byte []bytes = (byte[]) pdus[i];
                smsMessages[i] = SmsMessage.createFromPdu(bytes);
            }
            String number = SharedPreferencesUtil.getNumber(context);
            LogUtil.i("momoda", number + "\n" + smsMessages[0].getOriginatingAddress());
            StringBuilder[] stringBuffer = new StringBuilder[pdus.length];
            int count = 0;
            if (smsMessages.length > 0) {
                for (int i = 0; i < smsMessages.length; i++) {
                    if (smsMessages[i].getOriginatingAddress().contains(number)) {
                        stringBuffer[count] = new StringBuilder();
                        stringBuffer[count].append(DateUtil.convertToTime(smsMessages[i].getTimestampMillis()) + " ")
                                .append(smsMessages[i].getDisplayMessageBody());
                        count++;
                    }
                }
            }
            Intent intent1 = new Intent(context, DataService.class);
            intent1.putExtra("number", count);
            for (int i = 0; i < count; i++) {
                intent1.putExtra("message" + i, new String(stringBuffer[i]));
            }
            context.startService(intent1);
        }
    }
}