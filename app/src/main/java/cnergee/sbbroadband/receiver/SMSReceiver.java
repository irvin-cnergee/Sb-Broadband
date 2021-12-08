package cnergee.sbbroadband.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import cnergee.sbbroadband.utils.MyUtils;
import developer.app.interface_.SmsListener;

/**
 * Created by Siddhesh on 8/16/2017.
 */

public class SMSReceiver extends BroadcastReceiver {

    private static final String TAG = "SMSReceiver";

    private static SmsListener mListener;
    String otp = "";

    @Override
    public void onReceive(Context context, Intent intent) {

        MyUtils.l(TAG,"inside SMSReceiver");

        Bundle bundle = intent.getExtras();

        if(bundle != null){
            Object[] pdus = (Object[]) bundle.get("pdus");

            for (int i = 0; i < pdus.length; i++) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

                String phoneNum = smsMessage.getDisplayOriginatingAddress();

                String messageBody = smsMessage.getMessageBody();

                String sender = phoneNum;

                if(sender.contains("SBNETS")) {

//                    Toast.makeText(context, "sender : " + sender+"\n msg : "+messageBody, Toast.LENGTH_SHORT).show();

                    String[] arr = messageBody.split(" ");
                    if (arr.length>0){
                        otp = arr[3];
                    }

                    mListener.messageReceived(otp);
                }
            }
        }

    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}
