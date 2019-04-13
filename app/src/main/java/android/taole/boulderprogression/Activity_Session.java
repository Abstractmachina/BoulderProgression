package android.taole.boulderprogression;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_Session extends Activity
{

    //Database fields
    private DBHandler dbh;
    SQLiteDatabase db;

    //UI variables
    private Handler handler;
    private PopupWindow pw;


    //session variables
    private Session session;
    ProgressBar[] countBars;


    //current problem variables
    private int selectedGrade = 0;
    private Style selectedStyle;
    private int selectedWallAngle = 0;
    private int selectedMoveNum = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        dbh = new DBHandler(this);

        int sessionNumber = dbh.getMaxValue(dbh.KEY_SESSION_NUMBER) + 1;
        session = new Session(sessionNumber);
        session.setSessionType(SessionType.CAPACITY); //TODO add User input for session type
        handler = new Handler();



        //start timer
        Runnable run = new Runnable()
        {
            @Override
            public void run()
            {
                int sessionDuration = session.getSessionDuration();
                TextView lengthDisplay = (TextView) findViewById(R.id.sessionLength);
                lengthDisplay.setText(session.convertSessionDuration());
                session.setSessionDuration(sessionDuration + 1);
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(run);

        updateSessionData();
        //reset count table values
        session.refreshCountTableDisplay(this, getResources(), getPackageName());
        updateMetrics();


        countBars = new ProgressBar[session.getMaxGrade()];
        for (int i = 0; i < countBars.length; i++)
        {
            //build id string
            String idName = "b" + Integer.toString(i);
            //get id from name
            int id = getResources().getIdentifier(idName, "id", getPackageName());
            //add bar
            countBars[i] = (ProgressBar) findViewById(id);
        }
    }

    /**
     * create and inflate problem log pop up on click.
     *
     * @param view
     */
    public void onClick_addProblem(View view)
    {

        LayoutInflater inflater = (LayoutInflater) Activity_Session.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popup_problem_log, null);
        pw = new PopupWindow(layout, (int) (Utility.getScreenWidth() - 80), (int) (Utility.getScreenHeight() - 180), true);
        pw.setOutsideTouchable(false);

        //set up grade input
        selectedGrade = 0;
        SeekBar gradeSeekBar = (SeekBar) pw.getContentView().findViewById(R.id.gradeInput);
        setupSeekBar(gradeSeekBar, session.getMaxGrade(), 0);

        //setup for style spinner
        setupSpinner((Spinner) pw.getContentView().findViewById(R.id.spinner_style));

        pw.showAtLocation(layout, Gravity.CENTER, 0, -40);
    }


    /**
     * create and add new problem problem to session on click.
     *
     * @param view
     */
    public void onClick_logProblem(View view)
    {
        EditText angle = pw.getContentView().findViewById(R.id.enter_angle);
        EditText moves = pw.getContentView().findViewById(R.id.enterMoves);

        //exceptions
        if (TextUtils.isEmpty(angle.getText()))
        {
            Toast.makeText(this, "You did not enter an angle", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(moves.getText()))
        {
            Toast.makeText(this, "You did not enter the number of moves", Toast.LENGTH_LONG).show();
            return;
        }

        selectedWallAngle = Integer.parseInt(angle.getText().toString());
        if (selectedWallAngle < -45 || selectedWallAngle > 90)
        {
            Toast.makeText(this, "Angle must be between -45 and 90", Toast.LENGTH_LONG).show();
            return;
        }

        selectedMoveNum = Integer.parseInt(moves.getText().toString());
        if (selectedMoveNum < 1)
        {
            Toast.makeText(this, "Number must be equal or larger than 1.", Toast.LENGTH_LONG).show();
            return;
        }


        //instantiate problem if constraints are satisfied.
        Problem problem = new Problem(selectedGrade, selectedStyle, selectedWallAngle, selectedMoveNum);
        session.addProblem(problem);

        //session.updateCountTable();
        session.refreshCountTableDisplay(this, getResources(), getPackageName());
        updateBars();
        updateMetrics();

        pw.dismiss();

    }

    private void updateMetrics()
    {
        session.updateMetrics();
        int sum = session.getvSum();
        double avg = session.getvAvg();
        double den = session.getvDen();

        TextView sv = findViewById(R.id.VSum);
        TextView av = findViewById(R.id.VAvg);
        TextView dv = findViewById(R.id.VDen);

        sv.setText(Integer.toString(sum));
        av.setText(Double.toString(Utility.round(avg, 2)));
        dv.setText(Double.toString(Utility.round(den, 2)));



    }

    public void updateBars()
    {
        //get max value
        int maxNum = 0;
        for (int i = 0; i < countBars.length; i++)
        {
            int n = session.getCountTable()[i];
            if (n > maxNum) maxNum = n;
        }

        //update bars relative to max value
        for (int i = 0; i < countBars.length; i++)
        {
            ProgressBar bar = countBars[i];
            bar.setMax(maxNum);
            bar.setProgress(session.getCountTable()[i]);
        }
    }

    private void updateSessionData()
    {
        TextView snv = findViewById(R.id.SessionNo);
        int num = session.getSessionNumber();
        String nums = Integer.toString(num);
        if (num < 10) nums = "0" + nums;
        snv.setText("Session: " + nums);

        TextView dv = findViewById(R.id.currentDate);
        dv.setText(Integer.toString(session.getDate()));
    }

    public void onClick_endSession(View view)
    {


        if (dbh.writeToDataBase(session))
        {
            dbh.readDataBase();
            Intent intent = new Intent(this, Activity_Main.class);
            finish();
        } else {
            Toast.makeText(this, "Why would you log a session with no problems?", Toast.LENGTH_LONG).show();
        }

    }

    private void setupSeekBar(SeekBar gradeSeekBar, int max, int p)
    {
        gradeSeekBar.setMax(max + 1);
        gradeSeekBar.setProgress(p);
        gradeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            int progressval = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                progressval = progress;
                TextView gradeDisplay = (TextView) pw.getContentView().findViewById(R.id.gradeInputDisplay);
                gradeDisplay.setText("V" + Integer.toString(progressval));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                selectedGrade = progressval;
            }
        });
    }


    private void setupSpinner(Spinner spinner)
    {

        ArrayAdapter<Enum> adapter_style = new ArrayAdapter<Enum>(Activity_Session.this, android.R.layout.simple_spinner_item, Style.values());
        adapter_style.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter_style);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selectedStyle = Style.values()[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }


}
