package org.d3ifcool.timework;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class AlarmBroadcastReciever extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            context.startActivity(new Intent(context,MainActivity.class));
        }
    }
}
