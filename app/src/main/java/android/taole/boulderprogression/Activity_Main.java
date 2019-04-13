package android.taole.boulderprogression;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Activity_Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
    }

    public void onClick_startSession(View view){

        Intent getSessionScreenIntent = new Intent(this, Activity_Session.class);

        final int result =1;

        getSessionScreenIntent.putExtra("startingSession", "Activity_Main");
        startActivity(getSessionScreenIntent);

    }

    public void onClick_viewLog(View view)
    {
        Intent intent = new Intent(this, Activity_Log.class);
        startActivity(intent);
    }


}
