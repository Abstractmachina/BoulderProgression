package android.taole.boulderprogression.UI;

import android.database.Cursor;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.taole.boulderprogression.DataBaseManager;
import android.taole.boulderprogression.R;
import android.taole.boulderprogression.SectionsPageAdapter;
import android.util.Log;

import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

public class Activity_Log extends AppCompatActivity
{

    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;

    //DBHandler dbHandler;
    DataBaseManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);


        mManager = new DataBaseManager(getApplicationContext());

        //build grade table
        //send grade table to fragment
        Bundle bundle = new Bundle();
        bundle.putIntArray("gradeTable", buildGradeTable());
        Fragment_Logbook frag1 = new Fragment_Logbook();
        frag1.setArguments(bundle);

        //build stats
        HashMap<String, int[]> statsTable = buildStatsTable();

        Bundle IDBundle = new Bundle();
        Bundle dateBundle = new Bundle();
        Bundle sumBundle = new Bundle();
        Bundle avgBundle = new Bundle();
        Bundle durationBundle = new Bundle();
        Bundle denBundle = new Bundle();
        int[] a = statsTable.get("sessionAvg");

        for (int i : a) Log.i("content of sum array", Integer.toString(i));
        IDBundle.putIntArray("sessionID", statsTable.get("sessionID"));
        dateBundle.putIntArray("sessionDate", statsTable.get("sessionDate"));
        sumBundle.putIntArray("sessionVSum", statsTable.get("sessionVSum"));
        avgBundle.putIntArray("sessionAvg", statsTable.get("sessionAvg"));
        durationBundle.putIntArray("sessionDuration", statsTable.get("sessionDuration"));
        denBundle.putIntArray("sessionDuration", statsTable.get("sessionDuration"));

        Bundle compoundBundle = new Bundle();
        compoundBundle.putBundle("IDBundle", IDBundle);
        compoundBundle.putBundle("dateBundle", dateBundle);
        compoundBundle.putBundle("sumBundle", sumBundle);
        compoundBundle.putBundle("avgBundle", avgBundle);
        compoundBundle.putBundle("durationBundle", durationBundle);
        compoundBundle.putBundle("denBundle", denBundle);

        Fragment_Statistics frag2 = new Fragment_Statistics();
        frag2.setArguments(compoundBundle);


        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.vp_tabs);
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(frag1, "Log Book");
        adapter.addFragment(frag2, "Statistics");
        adapter.addFragment(new Fragment_3(), "Analysis");

        mViewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        Log.i("maxSessionNo", Integer.toString(mManager.getMaxValue(mManager.KEY_SESSION_NUMBER)));

    }


    /**
     *
     * @return
     */
    public HashMap<String, int[]> buildStatsTable()
    {

        HashMap<String, int[]> outTable = new HashMap<>();

        //get relevant data
        Cursor c = mManager.getWritableDatabase().rawQuery("SELECT " + mManager.KEY_SESSION_NUMBER
                + "," + mManager.KEY_SESSION_DATE
                + "," + mManager.KEY_GRADE
                + "," + mManager.KEY_SESSION_DURATION
                + " FROM " + mManager.DATABASE_NAME, null);


        //find session ids.
        int[] sessionId = getUniqueSessionId(c);
        int sessionCount = sessionId.length;

        //find unique session date
        int[] sessionDate = getSessionDates(c, sessionId, sessionCount);

        //calc Vsum for each session
        int[] sessionSum = getVSum(c, sessionCount);

        //calc Vavg for each session
        int[] sessionAvg = getVAvg(c, sessionCount, sessionSum);

        int[]sessionDuration = getSessionDuration(c, sessionCount);
        int[] sessionDen = getSessionDensity(c, sessionDuration, sessionSum);

        c.close();

        outTable.put("sessionID", sessionId);
        outTable.put("sessionDate", sessionDate);
        outTable.put("sessionVSum", sessionSum);
        outTable.put("sessionAvg", sessionAvg);
        outTable.put("sessionDuration", sessionDuration);
        outTable.put("sessionDen", sessionDen);


        return outTable;

    }

    /**
     * Get duration of each session.
     * @param c
     * @param sessionCount
     * @return
     */
    private int[] getSessionDuration(Cursor c, int sessionCount)
    {
        int[] sessionDuration = new int[sessionCount];

        for (int i = 0; i < sessionCount; i++)
        {
            c.moveToFirst();

            while (!c.isAfterLast())
            {

                if (c.getInt(c.getColumnIndex(mManager.KEY_SESSION_NUMBER)) == i)
                {
                    sessionDuration[i] = c.getInt(c.getColumnIndex(mManager.KEY_SESSION_DURATION));
                    break;
                }
                c.moveToNext();
            }
        }
        return sessionDuration;

    }

    /**
     * Calculate VDensity for each session.
     * @param c
     * @param sessionDuration
     * @param sessionSum
     * @return
     */
    private int[] getSessionDensity(Cursor c, int[] sessionDuration, int[] sessionSum)
    {

        int[] sessionDensity = new int[sessionDuration.length];
        if (sessionDuration.length != sessionSum.length)
        {return sessionDensity;}

        for(int i =0; i < sessionDensity.length; i++)
        {
            sessionDensity[i] = (int) ((double) sessionSum[i] / (double) sessionDuration[i] * 1000);
        }
        return sessionDensity;

    }


    /**
     * calculate VAVG for each session.
     *
     * @param c
     * @param sessionCount
     * @param session_VSum
     * @return
     */
    private int[] getVAvg(Cursor c, int sessionCount, int[] session_VSum)
    {
        int[] sessionProblemCount = new int[sessionCount];
        int[] sessionAVG = new int[sessionCount];

        c.moveToFirst();
        while (!c.isAfterLast())
        {
            int sessionID = c.getInt(c.getColumnIndex(mManager.KEY_SESSION_NUMBER));
            sessionProblemCount[sessionID]++;
            c.moveToNext();
        }

        for (int i = 0; i < sessionAVG.length; i++)
        {
            sessionAVG[i] = (int) (((double) session_VSum[i] / (double) sessionProblemCount[i]) * 1000);
        }
        return sessionAVG;
    }


    /**
     * Calculate VSum for each session.
     *
     * @param c
     * @param sessionCount
     * @return
     */
    private int[] getVSum(Cursor c, int sessionCount)
    {
        int[] session_VSum = new int[sessionCount];
        int gradeIndex = c.getColumnIndex(mManager.KEY_GRADE);
        c.moveToFirst();
        while (!c.isAfterLast())
        {
            int currentGrade = c.getInt(gradeIndex);
            int tempId = c.getInt(c.getColumnIndex(mManager.KEY_SESSION_NUMBER));
            session_VSum[tempId] += currentGrade;
            c.moveToNext();
        }
        return session_VSum;
    }

    /**
     * Find date for all sessions.
     *
     * @param c
     * @param sessionId
     * @param sessionCount
     * @return
     */
    private int[] getSessionDates(Cursor c, int[] sessionId, int sessionCount)
    {
        int sessionDateIndex = c.getColumnIndex(mManager.KEY_SESSION_DATE);
        int[] sessionDate = new int[sessionId.length];
        for (int i = 0; i < sessionCount; i++)
        {
            c.moveToFirst();
            while (!c.isAfterLast())
            {
                if (c.getInt(c.getColumnIndex(mManager.KEY_SESSION_NUMBER)) == sessionId[i])
                {
                    sessionDate[i] = c.getInt(sessionDateIndex);
                    break;
                } else c.moveToNext();
            }
        }
        return sessionDate;
    }

    /**
     * Find unique session ids.
     *
     * @param c
     * @return sessionId in numerical order.
     */
    private int[] getUniqueSessionId(Cursor c)
    {
        int sessionNumberIndex = c.getColumnIndex(mManager.KEY_SESSION_NUMBER);
        HashSet<Integer> uniqueSessions = new HashSet<>();
        c.moveToFirst();
        while (!c.isAfterLast())
        {
            uniqueSessions.add(c.getInt(sessionNumberIndex));
            c.moveToNext();
        }
        TreeSet<Integer> sorted = new TreeSet<>();
        sorted.addAll(uniqueSessions);

        Integer[] sortedSessionID = sorted.toArray(new Integer[sorted.size()]);
        int[] sessionIDs = new int[sortedSessionID.length];


        for (int i = 0; i < sessionIDs.length; i++)
        {
            sessionIDs[i] = sortedSessionID[i];
        }

        return sessionIDs;
    }


    /**
     * Build grade table as array. Index corresponds to respective boulder grade.
     *
     * @return
     */
    public int[] buildGradeTable()
    {

        int[] gradeTable = new int[16];
        Cursor c = mManager.getWritableDatabase().rawQuery("SELECT " + mManager.KEY_GRADE + " FROM " + mManager.DATABASE_NAME, null);
        //sqlite> SELECT Name FROM Database;

        int gradeInd = c.getColumnIndex(mManager.KEY_GRADE);

        c.moveToFirst();
        while (!c.isAfterLast())
        {

            int currentGrade = c.getInt(gradeInd);
            gradeTable[currentGrade]++;
            c.moveToNext();
        }

        c.close();
//        for (int i = 0; i < gradeTable.length; i++)
//        {
//
//            Log.i("gradeTable, V" + Integer.toString(i), Integer.toString(gradeTable[i]));
//        }

        return gradeTable;
    }

    public void setupViewPager(ViewPager vp)
    {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Fragment_Logbook(), "Log Summary");
        adapter.addFragment(new Fragment_Statistics(), "fragment_2");
        adapter.addFragment(new Fragment_3(), "fragment_3");

        vp.setAdapter(adapter);
    }


}
