package android.taole.boulderprogression.UI;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.taole.boulderprogression.R;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class Fragment_Statistics extends Fragment
{

    private static final String TAG = "fragment_2";
    private Button btnTest;
    int[] sessionID;
    int[] sessionDate;
    int[] sessionVSum;
    int[] sessionAvg;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_2, container, false);
        btnTest = view.findViewById(R.id.btnTest2);

        Bundle bundle = getArguments();
        Bundle IDBundle = bundle.getBundle("IDBundle");
        Bundle dateBundle = bundle.getBundle("dateBundle");
        Bundle sumBundle = bundle.getBundle("sumBundle");
        Bundle avgBundle = bundle.getBundle("avgBundle");

        Log.i("bundle content", bundle.toString());
        try
        {
            sessionID = IDBundle.getIntArray("sessionID");
            sessionDate = dateBundle.getIntArray("sessionDate");
            sessionVSum = sumBundle.getIntArray("sessionVSum");
            sessionAvg = avgBundle.getIntArray("sessionAvg");
            //Log.i("SessionID Length", Integer.toString(sessionID.length));
        } catch (Exception e)
        {
            e.printStackTrace();
            Log.i("ERROR", "Bundle import unsuccessful!");
        }

        try
        {
            Log.i("SessionID Length", Integer.toString(sessionID.length));
        } catch (Exception e)
        {
            Log.i("ERROR", "NULL POINTERRRRR");
        }
        try
        {
            Log.i("SessionDate Length", Integer.toString(sessionDate.length));
        } catch (Exception e)
        {
            Log.i("ERROR", "NULL POINTERRRRR 22222");
        }
        try
        {
            Log.i("SessionSUM Length", Integer.toString(sessionVSum.length));
        } catch (Exception e)
        {
            Log.i("ERROR", "NULL POINTERRRRR 333333");
        }
        try
        {
            Log.i("SessionAbg Length", Integer.toString(sessionAvg.length));
        } catch (Exception e)
        {
            Log.i("ERROR", "NULL POINTERRRRR 444444");
        }


        btnTest.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getActivity(), "Test Button 2", Toast.LENGTH_SHORT).show();
                try
                {
                    for (int i : sessionID) Log.i("sessionID", Integer.toString(sessionID[i]));
                } catch (Exception e)
                {
                    e.printStackTrace();
                    Log.i("ERROR", "sessionID is fucked! sessionID Length: " + Integer.toString(sessionID.length));
                }
                //for (int i : sessionDate) Log.i("sessionDate", Integer.toString(sessionDate[i]));
                //for (int i : sessionVSum) Log.i("sessionVSum", Integer.toString(sessionVSum[i]));
            }
        });

        return view;
    }


}
