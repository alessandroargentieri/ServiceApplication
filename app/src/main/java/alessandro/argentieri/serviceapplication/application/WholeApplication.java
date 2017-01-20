package alessandro.argentieri.serviceapplication.application;


import android.app.Application;
import android.content.Intent;
import alessandro.argentieri.serviceapplication.activities.CallerInterface;
import alessandro.argentieri.serviceapplication.service.MyService;

public class WholeApplication extends Application {

    static CallerInterface caller;


    @Override
    public void onCreate(){
        super.onCreate();
        //create and start the service
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
    }



    //in realtà verrà passata una vera activity
    public static void setCaller(CallerInterface mCaller){
        caller = mCaller;
        MyService.setActivity(caller);
    }

    public static synchronized void azzeraCount(){
        MyService.azzeraCount();
    }


}
