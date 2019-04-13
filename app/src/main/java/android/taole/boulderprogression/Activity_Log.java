package android.taole.boulderprogression;

import android.database.Cursor;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class Activity_Log extends AppCompatActivity
{

    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        dbHandler = new DBHandler(getApplicationContext());

        dbHandler.readDataBase();

        //build grade pyramid

        Cursor c = dbHandler.getDataBase().rawQuery("SELECT " + dbHandler.KEY_GRADE + " FROM " + dbHandler.DATABASE_NAME, null);
        //sqlite> SELECT Name, Price FROM Cars;


        int gradeInd = c.getColumnIndex(dbHandler.KEY_GRADE);
        Log.i("columnCount", Integer.toString(c.getCount()));
        int[] gradeTable = new int[16];

        c.moveToFirst();
        //int i = 0;
        while (!c.isAfterLast())
        {

            int currentGrade = c.getInt(gradeInd);
            gradeTable[currentGrade]++;

            Log.i("grade", "V" + c.getString(gradeInd));
            c.moveToNext();
        }

        for (int i = 0; i < gradeTable.length; i++)
        {

            Log.i("gradeTable, V" + Integer.toString(i), Integer.toString(gradeTable[i]));
        }


        List<BarEntry> grades = new ArrayList<>();
        for (int i =0; i < gradeTable.length; i++)
        {
            grades.add(new BarEntry(i, gradeTable[i]));
        }

        // programmatically create a LineChart
        BarChart chart = new BarChart(this);
        chart.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));
        // get a layout defined in xml
        ConstraintLayout chartWrapper = (ConstraintLayout) findViewById(R.id.ChartWrapperLayout);

        chart.animateY(1000);

        chartWrapper.addView(chart); // add the programmatically created chart

        ValueFormatter xAxisFormatter = new VGradeFormater(chart);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(16);
        xAxis.setValueFormatter(xAxisFormatter);
        


        BarDataSet barDataSet = new BarDataSet(grades, "Grades");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData barData = new BarData(barDataSet);
        chart.setData(barData);
        chart.invalidate();
        /*
        List<Entry> NoOfEmp = new ArrayList();

        NoOfEmp.add(new Entry(2008, 945f));
        NoOfEmp.add(new Entry(2009, 1040f));
        NoOfEmp.add(new Entry(2010, 1133f));
        NoOfEmp.add(new Entry(2011, 1240f));
        NoOfEmp.add(new Entry(2012, 1369f, 4));
        NoOfEmp.add(new Entry(2013, 1487f, 5));
        NoOfEmp.add(new Entry(2014, 1501f, 6));
        NoOfEmp.add(new Entry(2015, 1645f, 7));
        NoOfEmp.add(new Entry(2016, 1578f, 8));
        NoOfEmp.add(new Entry(2017, 1695f, 9));


        List<String> year = new ArrayList();

        year.add("2008");
        year.add("2009");
        year.add("2010");
        year.add("2011");
        year.add("2012");
        year.add("2013");
        year.add("2014");
        year.add("2015");
        year.add("2016");
        year.add("2017");


        LineChart chart = (LineChart) findViewById(R.id.chart);
        LineDataSet dataSet = new LineDataSet(NoOfEmp, "label");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextColor(1);
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate();

*/

    }



    public class VGradeFormater extends ValueFormatter
    {
        private final String[] mValues = new String[]
                {
                        "V0","V1","V2","V3","V4","V5","V6","V7","V8","V9","V10","V11","V12","V13","V14","V15"
                };

        private final BarLineChartBase<?> chart;
        public VGradeFormater(BarLineChartBase<?> chart) {
            this.chart = chart;
        }
        @Override
        public String getFormattedValue(float value) {

            int grade = (int) value;
            return mValues[grade];
        }


    }
}
