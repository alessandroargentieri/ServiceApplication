package alessandro.argentieri.serviceapplication.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import alessandro.argentieri.serviceapplication.R;
import alessandro.argentieri.serviceapplication.application.WholeApplication;

public class MainActivity extends AppCompatActivity implements CallerInterface{

    TextView countText;


    //************************METODI DI CALLBACK*********************************//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        countText = (TextView)findViewById(R.id.timestamp_text);
    }

    @Override
    protected void onResume(){
        super.onResume();
        //pass the reference of the current Activity to the static method of WholeApplication (that's why we don't create an instance of it): it's necessary to keep track to the Activity which is currently engaged in the communication with the Service started and bound from the WholeApplication. setCaller, to be adapted to both MainActivity type and Main2Activity type requires the CallerInterface which both the two kinds of Activities implement.
        WholeApplication.setCaller(this);
    }

    //****************************BOTTONI****************************************//

    public void Azzera(View v){
        //chiede a Whole Application di azzerare il count: l'applicazione rimanderà lo stesso comando al Service
        WholeApplication.azzeraCount();
    }

    public void PassToOtherActivity(View v){
        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
        startActivity(intent);
        finish();
    }

    //*******************DALL'INTERFACCIA (dall'esterno)*************************//

    @Override
    public void setCountText(int count){
        countText.setText(count + "");
    }





}
