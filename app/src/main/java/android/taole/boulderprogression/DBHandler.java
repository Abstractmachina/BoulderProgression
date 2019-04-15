package android.taole.boulderprogression;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.HashSet;
import java.util.TreeSet;

import static android.content.Context.MODE_PRIVATE;


public class DBHandler
{

    private SQLiteDatabase db;
    public static final String DATABASE_NAME = "ProgressionDB";
    public static final String KEY_ID = "id";
    public static final String KEY_SESSION_NUMBER = "session_id";
    public static final String KEY_SESSION_TYPE = "session_type";
    public static final String KEY_SESSION_DATE = "session_date";
    public static final String KEY_SESSION_DURATION = "session_duration";
    public static final String KEY_GRADE = "grade";
    public static final String KEY_STYLE = "style";
    public static final String KEY_ANGLE = "angle";
    public static final String KEY_NUMBER_OF_MOVES = "number_of_moves";

    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + DATABASE_NAME + "("
                    + KEY_ID + " INTEGER PRIMARY KEY, "
                    + KEY_SESSION_NUMBER + " INTEGER, "
                    + KEY_SESSION_TYPE + " VARCHAR, "
                    + KEY_SESSION_DATE + " INT(6), "
                    + KEY_SESSION_DURATION + " INTEGER, "
                    + KEY_GRADE + " INT(2), "
                    + KEY_STYLE + " VARCHAR, "
                    + KEY_ANGLE + " INTEGER, "
                    + KEY_NUMBER_OF_MOVES + " INTEGER"
                    + ")";


    public DBHandler(Context context)
    {

        db = context.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        initDatabase();

    }

    private void initDatabase()
    {
        db.execSQL(CREATE_TABLE);
    }


    /**
     *
     * @param columnName
     * @return
     */
    public int getMaxValue(String columnName)
    {
        Cursor c = db.rawQuery("SELECT * FROM " + DATABASE_NAME, null);
        int columnIndex = c.getColumnIndex(columnName);
        HashSet<Integer> values = new HashSet<>();
        if (c.moveToFirst())
        {
            while (!c.isAfterLast())
            {
                values.add(c.getInt(columnIndex));
                c.moveToNext();
            }
            TreeSet<Integer> sortedVal = new TreeSet();
            sortedVal.addAll(values);
            return sortedVal.last();
        } else
            return -1;

    }


    public boolean writeToDataBase(Session session)
    {

        if (session.getProblemCount() == 0) return false;

        int currentDuration = session.getSessionDuration();
        for (Problem p : session.getProblems())
        {
            String sqlEntry = String.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s, %s) VALUES (?,?,?,?,?,?,?,?)", DATABASE_NAME, KEY_SESSION_NUMBER, KEY_SESSION_TYPE, KEY_SESSION_DATE, KEY_SESSION_DURATION, KEY_GRADE, KEY_STYLE, KEY_ANGLE, KEY_NUMBER_OF_MOVES);
            SQLiteStatement statement = db.compileStatement(sqlEntry);
            //Log.i("sql", sql);


            statement.bindString(1, Integer.toString(session.getSessionNumber()));
            statement.bindString(2, session.getSessionType().toString());
            statement.bindString(3, Integer.toString(session.getDate()));
            statement.bindString(4, Integer.toString(currentDuration));
            statement.bindString(5, Integer.toString(p.getGrade()));
            statement.bindString(6, p.getStyle().toString());
            statement.bindString(7, Integer.toString(p.getAngle()));
            statement.bindString(8, Integer.toString(p.getNumberOfMoves()));

            statement.execute();
        }
        return true;
/*

            */
    }


    public String readDataBase()
    {
        Cursor c = db.rawQuery("SELECT * FROM " + DATABASE_NAME, null);

        c.moveToFirst();
        int i = 0;
        while (!c.isAfterLast())
        {
            Log.i("entry "+ Integer.toString(i), readEntry(c));
            i++;
            c.moveToNext();
        }
        return "";
    }

    private String readEntry(Cursor c)
    {
        int idi = c.getColumnIndex(KEY_ID);
        int sessionNoInd = c.getColumnIndex(KEY_SESSION_NUMBER);
        int sessionTypeInd = c.getColumnIndex(KEY_SESSION_TYPE);
        int dateInd = c.getColumnIndex(KEY_SESSION_DATE);
        int durationInd = c.getColumnIndex(KEY_SESSION_DURATION);
        int gradeInd = c.getColumnIndex(KEY_GRADE);
        int styleInd = c.getColumnIndex(KEY_STYLE);
        int angleInd = c.getColumnIndex(KEY_ANGLE);
        int numberOfMovesInd = c.getColumnIndex(KEY_NUMBER_OF_MOVES);

        String s = "{\n"
                + KEY_ID + " : " + c.getString(idi) +",\n"
                + KEY_SESSION_NUMBER + " : " + c.getString(sessionNoInd) +",\n"
                + KEY_SESSION_TYPE + " : " + c.getString(sessionTypeInd) +",\n"
                + KEY_SESSION_DATE + " : " + c.getString(dateInd) +",\n"
                + KEY_SESSION_DURATION + " : " + c.getString(durationInd) +",\n"
                + KEY_GRADE + " : " + c.getString(gradeInd) +",\n"
                + KEY_STYLE + " : " + c.getString(styleInd) +",\n"
                + KEY_ANGLE + " : " + c.getString(angleInd) +",\n"
                + KEY_NUMBER_OF_MOVES + " : " + c.getString(numberOfMovesInd) +",\n"
                + "}";

        return s;
    }


    public SQLiteDatabase getDataBase() {return db;}

}
