package android.taole.boulderprogression;

import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Grades extends Fragment
{

    private  static final String TAG  = "fragment_grades";
    private Button btnTest;
    private int[] gradeTable;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_grades, container, false);
        btnTest = view.findViewById(R.id.btnTest1);



        Bundle bundle = getArguments();
        try
        {
            gradeTable = bundle.getIntArray("gradeTable");
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        btnTest.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                Toast.makeText(getActivity(), Integer.toString(gradeTable.length), Toast.LENGTH_SHORT).show();
            }
        });


        List<BarEntry> grades = new ArrayList<>();
        for (int i =0; i < gradeTable.length; i++)
        {
            grades.add(new BarEntry(i, gradeTable[i]));
        }

        // programmatically create a LineChart
        BarChart chart = new BarChart(getActivity());
        ViewGroup.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);

        Resources r = this.getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                20,
                r.getDisplayMetrics()
        );

        ((ConstraintLayout.LayoutParams) params).setMargins(px,px,px,px);
        chart.setLayoutParams(params);

        // get a layout defined in xml
        ConstraintLayout chartWrapper = view.findViewById(R.id.gradesWrapper);

        chart.animateY(1000);

        chartWrapper.addView(chart); // add the programmatically created chart

        ValueFormatter xAxisFormatter = new VGradeFormatter(chart);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(16);
        xAxis.setValueFormatter(xAxisFormatter);

        chart.getAxisRight().setEnabled(false);
        YAxis yAxis = chart.getAxisLeft();
        yAxis.setAxisMinimum(0);
        yAxis.setValueFormatter(new IntegerValueFormatter());
        yAxis.setDrawZeroLine(false);
        yAxis.setAxisMinimum(0f);

        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);


        BarDataSet barDataSet = new BarDataSet(grades, "Grades");
        barDataSet.setColors(ColorTemplate.rgb("FFB81E"));
        BarData barData = new BarData(barDataSet);
        chart.setData(barData);
        chart.invalidate();




        return view;


    }


    private class VGradeFormatter extends ValueFormatter
    {
        private final String[] mValues = new String[]
                {
                        "V0", "V1", "V2", "V3", "V4", "V5", "V6", "V7", "V8", "V9", "V10", "V11", "V12", "V13", "V14", "V15"
                };

        private final BarLineChartBase<?> chart;

        public VGradeFormatter(BarLineChartBase<?> chart)
        {
            this.chart = chart;
        }

        @Override
        public String getFormattedValue(float value)
        {

            int grade = (int) value;
            return mValues[grade];
        }
    }

    private class IntegerValueFormatter extends ValueFormatter
    {

        private int[] mValues;

        public IntegerValueFormatter()
        {
        }


        @Override
        public String getFormattedValue(float value)
        {
            int val = (int) value;
            return Integer.toString(val);
        }


    }

}
