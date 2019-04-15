package android.taole.boulderprogression.UI;

import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.taole.boulderprogression.DataBaseManager;
import android.taole.boulderprogression.R;
import android.util.Log;
import android.view.View;

public class Activity_Main extends AppCompatActivity {

    DataBaseManager mManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
