package alessandro.argentieri.serviceapplication.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import alessandro.argentieri.serviceapplication.activities.CallerInterface;

public class MyService extends Service {

    public static int count = 0;
    private static CallerInterface callerActivity;
    private static String LOG_TAG = "BoundService";
    public boolean is_thread_active = false; //NOT THE SERVICE BUT THE THREAD WITHIN THE SERVICE
    MyThread t;


    public MyService() {
        //empty costructor
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startThread();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        killerThread();
    }


    //******************DALL'INTERFACCIA (dall'esterno)**********************//
    public static void setActivity(CallerInterface mainActivity){
        callerActivity = mainActivity;
    }

    public static void azzeraCount(){
        count = 0;
    }

    public void startThread(){
        if(is_thread_active==false){ //se il thread non è già partito
            is_thread_active = true;
            Handler handler = new MyHandler();
            t = new MyThread(handler);
            t.start();
        }else{
            Toast.makeText(this, "Thread already in action!",Toast.LENGTH_LONG).show();
        }
    }


    //**************************VOID UTILITY********************************//

    public void killerThread(){
        try {
            if (t.isAlive()) {
                t.interrupt();
                t = null;
            }
        }catch(Exception e){
            Log.e(LOG_TAG,"ThreadKiller error: " + e.toString());
        }
    }

    //********************NESTED CLASSES (Thread + Handler)*****************//


    //RECEPISCE DAL THREAD INTERNO AL SERVICE TUTTI I MESSAGGI CON CHIAVE "count" INVIATI AL SERVICE STESSO E LI GESTISCE CHIAMANDO L'ACTIVITY PER NOTIFICARE I RISULTATI DEL THREAD
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            if(bundle.containsKey("count")) {
                int value = bundle.getInt("count");
                if(callerActivity!=null)
                    try {
                        callerActivity.setCountText(value);
                    }catch(Exception e){
                        Log.e(LOG_TAG, e.toString());
                        Toast.makeText(MyService.this, e.toString(), Toast.LENGTH_LONG).show();
                    }
                else
                    Toast.makeText(MyService.this, "callerActivity is still null", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public class MyThread extends Thread{
        private Handler handler;

        //COSTRUTTORE DEL THREAD: LEGA L'HANDLER DEL SERVICE AL THREAD STESSO PER FARLI COMUNICARE
        public MyThread(Handler handler){
            this.handler = handler;
        }

        @Override
        public void run(){
            long instant = System.currentTimeMillis();
            while(is_thread_active){
                if(System.currentTimeMillis() - instant > 1000){
                    count++;
                    instant = System.currentTimeMillis();
                    //RICHIAMO LA VOID DEL THREAD STESSO CHE NOTIFICA ALL'HANDLER DEL SERVICE I RISULTATI
                    notifyMessage(count);
                }
                if(count == 1000){
                    count = 0;
                }
            }
        }
        //VOID INTERNA AL THREAD PER NOTIFICARE ALL'HANDLER DEL SERVICE COLLEGATO A QUESTO THREAD I RISULTATI
        private void notifyMessage(int index) {
            Message msg = handler.obtainMessage();
            Bundle b = new Bundle();
            b.putInt("count", index);
            msg.setData(b);
            handler.sendMessage(msg);
        }
    } //end of myThread


}
