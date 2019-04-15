package android.taole.boulderprogression;

import android.taole.boulderprogression.enums.SessionType;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Session {

    private int date;
    private int sessionDuration;
    private int sessionNumber;
    private List<Problem> problems;
    private int vSum;
    private double vAvg;
    private double vDen;
    private int totalClimbs;
    private int[] countTable;
    private static final int MAXGRADE = 15;
    private SessionType sessionType;




    public Session(int sessionNumber) {
        problems = new ArrayList<Problem>();
        countTable = new int[MAXGRADE + 1];
        sessionDuration = 0;
        this.sessionNumber = sessionNumber;
        vSum = 0;
        vAvg = 0;
        vDen = 0;

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyMMdd");
        date = Integer.parseInt(df.format(c));
    }


    public void addProblem(Problem p) {

        problems.add(p);
        countTable[p.getGrade()] += 1;

    }


    public String convertSessionDuration()
    {
        int min = Math.round(sessionDuration / 60);
        int sec = sessionDuration % 60;

        String mins = Integer.toString(min);
        if (min < 10) mins = "0" + mins;

        String secs = Integer.toString(sec);
        if (sec < 10) secs = "0" + secs;

        return mins + ":" + secs;
    }

    public void refreshCountTableDisplay(android.app.Activity activity, android.content.res.Resources res, String packageName)
    {
        for (int i = 0; i < countTable.length; i++) {
            //build id string
            String idName = "V" + Integer.toString(i) + "CountDisplay";
            //get id from name
            int id = res.getIdentifier(idName, "id", packageName);
            TextView tv = (TextView) activity.findViewById(id);
            //update count display
            tv.setText(Integer.toString(countTable[i]));
        }
    }

    public void updateMetrics()
    {
        calculateSum();
        calculateAvg();
        calculateDen();
    }


    private void calculateSum()
    {
        int sum =0;
        for (Problem p : problems)
        {
            int grade = p.getGrade();
            if (grade == 0) grade = 1;
            sum += grade;
        }
        vSum = sum;
    }

    private void calculateAvg()
    {
        if (vSum != 0){
            double n = problems.size();
            vAvg = (double) vSum/n;
        } else {
            vAvg = 0;
        }

    }

    private void calculateDen()
    {
        if (sessionDuration != 0)
        vDen = (double) vSum/ (double) sessionDuration;
        else vDen = 0;
    }

    private void endSession(){

        //append all data to json

    }


    //GETTERS SETTERS
    public List<Problem> getProblems() {return problems;}
    public Problem getProblem(int i) {return problems.get(i);}
    public int getProblemCount() { return problems.size(); }

    public int[] getCountTable() { return countTable;}
    public void setCountTable(int[] countTable) { this.countTable = countTable;}

    public int getMaxGrade() {return MAXGRADE +1;}

    public void setSessionDuration(int sessionDuration) { this.sessionDuration = sessionDuration; }
    public int getSessionDuration() {return sessionDuration; }

    public int getvSum()
    {
        return vSum;
    }

    public double getvAvg()
    {
        return vAvg;
    }

    public double getvDen()
    {
        return vDen;
    }

    public SessionType getSessionType() {return sessionType; }

    public void setSessionType(SessionType sessionType)
    {
        this.sessionType = sessionType;
    }

    public int getDate()
    {
        return date;
    }

    public int getSessionNumber()
    {
        return sessionNumber;
    }
}
