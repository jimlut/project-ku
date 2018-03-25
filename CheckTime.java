package org.d3ifcool.timework;

import android.content.Intent;


public class CheckTime implements Runnable {
    @Override
    public void run() {
        try {
            for (int i=0;i<100;i++) {
                Thread.sleep(1000);
                if (i==100){

                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
