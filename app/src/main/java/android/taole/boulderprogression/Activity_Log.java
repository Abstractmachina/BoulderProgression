package android.taole.boulderprogression;

import android.database.Cursor;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class Activity_Log extends AppCompatActivity
{

    private static final String TAG = "Activity_Log";
    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;

    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);


        dbHandler = new DBHandler(getApplicationContext());
        dbHandler.readDataBase();

        //build grade table
        //send grade table to fragment
        Bundle bundle = new Bundle();
        bundle.putIntArray("gradeTable", buildGradeTable());
        Fragment_Grades frag1 = new Fragment_Grades();
        frag1.setArguments(bundle);



        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.vp_tabs);
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(frag1, "Grade Fragment");
        adapter.addFragment(new Fragment_2(), "fragment_2");
        adapter.addFragment(new Fragment_3(), "fragment_3");

        mViewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }


    /**
     * Build grade table as array. Index corresponds to respective boulder grade.
     * @return
     */
    private int[] buildGradeTable()
    {

        int[] gradeTable = new int[16];
        Cursor c = dbHandler.getDataBase().rawQuery("SELECT " + dbHandler.KEY_GRADE + " FROM " + dbHandler.DATABASE_NAME, null);
        //sqlite> SELECT Name FROM Database;

        int gradeInd = c.getColumnIndex(dbHandler.KEY_GRADE);

        c.moveToFirst();
        while (!c.isAfterLast())
        {

            int currentGrade = c.getInt(gradeInd);
            gradeTable[currentGrade]++;

            Log.i("grade", "V" + c.getString(gradeInd));
            c.moveToNext();
        }

        c.close();
        for (int i = 0; i < gradeTable.length; i++)
        {

            Log.i("gradeTable, V" + Integer.toString(i), Integer.toString(gradeTable[i]));
        }

        return gradeTable;
    }

    private void setupViewPager(ViewPager vp)
    {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Fragment_Grades(), "Grade Fragment");
        adapter.addFragment(new Fragment_2(), "fragment_2");
        adapter.addFragment(new Fragment_3(), "fragment_3");

        vp.setAdapter(adapter);
    }



}
