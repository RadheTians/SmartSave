package com.save.smartsave.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.save.smartsave.R;
import com.save.smartsave.RoundedBarChartRenderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static android.graphics.Color.rgb;
import static com.save.smartsave.MainActivity.balance;
import static com.save.smartsave.MainActivity.credit;

public class HomeFragment extends Fragment {

      final int currentMonth=3;



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView income1HomeFragment=view.findViewById(R.id.income1_home_fragment);
        TextView expense1HomeFragment=view.findViewById(R.id.expense1_home_fragment);
        TextView balance1HomeFragment=view.findViewById(R.id.balance1_home_fragment);
        TextView text1HomeFragment=view.findViewById(R.id.text1_home_fragment);

        int currentMonthExpense =credit.get(currentMonth).intValue()-balance.get(currentMonth).intValue();

        int previousMonthExpense=credit.get(currentMonth-1).intValue()-balance.get(currentMonth-1).intValue();

        income1HomeFragment.setText("Income \n ₹"+credit.get(currentMonth).intValue());
        expense1HomeFragment.setText("Expense \n ₹"+currentMonthExpense);
        balance1HomeFragment.setText("Balance \n ₹"+balance.get(currentMonth).intValue());
        float a;
        try { a=((currentMonthExpense-previousMonthExpense)/previousMonthExpense)*100; }catch (Exception e){ a=0; }
        String text1="Expense have increased by "+a+"% compared to last Month ";
        text1HomeFragment.setText(text1);
        //Pie Chart1
        PieChart pieChart1 = view.findViewById(R.id.pie_chart_1);
        pieChart1.setDrawRoundedSlices(true);
        pieChart1.setUsePercentValues(true);
        pieChart1.getDescription().setEnabled(false);
        pieChart1.setExtraOffsets(0,5,0,0);
        pieChart1.setDragDecelerationFrictionCoef(.80f);
        pieChart1.setDrawHoleEnabled(true);
        pieChart1.setHoleColor(android.R.color.white);
        pieChart1.setTransparentCircleRadius(60);
        ArrayList<PieEntry> pieEntery1 = new ArrayList<>();
        pieEntery1.add(new PieEntry(credit.get(currentMonth).intValue(), "Expense"));
        pieEntery1.add(new PieEntry(balance.get(currentMonth).intValue(), "Saving"));
        PieDataSet dataSet = new PieDataSet(pieEntery1, "Saving");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);
        pieChart1.getLegend().setEnabled(false);
        PieData data = new PieData(dataSet);
        pieChart1.animateY(1000, Easing.EaseInBounce);
        data.setValueTextSize(0);
        String setCenterText1= String.valueOf(0);
        try {
            setCenterText1= String.valueOf((balance.get(currentMonth).intValue()/(credit.get(currentMonth).intValue()+balance.get(currentMonth).intValue()))*100);
        }catch (Exception e){ }
        pieChart1.setCenterText( setCenterText1+"\n Balance");
        pieChart1.setCenterTextSize(14f);
        pieChart1.setCenterTextColor(Color.BLUE);

        final int[] MY_COLORS = {rgb(3, 218, 198),rgb(98, 0, 238)};
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for(int c: MY_COLORS) colors.add(c);
        dataSet.setColors(colors);

        pieChart1.setData(data);

        //BarChar1
        BarChart barChart1 = view.findViewById(R.id.barChart1);
        barChart1.setDrawBarShadow(false);
        barChart1.setDrawValueAboveBar(true);
        barChart1.setPinchZoom(false);
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1, 40));
        barEntries.add(new BarEntry(2, 36));
        barEntries.add(new BarEntry(3, 46));
        BarDataSet barDataSet = new BarDataSet(barEntries, "Month");
        barDataSet.setColors(Color.rgb(98, 0, 238));
        barDataSet.setValueTextSize(14f);
        BarData barData1 = new BarData(barDataSet);
        barData1.setBarWidth(0.5f);
        RoundedBarChartRenderer roundedBarChartRenderer = new RoundedBarChartRenderer(barChart1, barChart1.getAnimator(), barChart1.getViewPortHandler());
        roundedBarChartRenderer.setmRadius(20f);
        barChart1.setRenderer(roundedBarChartRenderer);
        barChart1.getXAxis().setDrawGridLines(false);
        barChart1.getLegend().setEnabled(false);
        barChart1.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart1.getDescription().setEnabled(false);
        barChart1.getAxisRight().setEnabled(false);
        barChart1.getAxisLeft().setEnabled(false);
        String[] labels = new String[4];
        labels[0] = "jan";
        labels[1] = "feb";
        labels[2] = "march";
        labels[3] = "april";
        barChart1.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        barChart1.getXAxis().setTextSize(14f);
        YAxis yAxisRight = barChart1.getAxisRight();
        yAxisRight.setAxisMinValue(25f);
        barChart1.setMinimumHeight(25);
        YAxis yAxisLeft = barChart1.getAxisLeft();
        yAxisLeft.setAxisMinValue(25f);
        barChart1.setData(barData1);

        //BarChart2
        BarChart barChart2 = view.findViewById(R.id.barChart2);
        barChart2.setDrawBarShadow(false);
        barChart2.setDrawValueAboveBar(true);
        barChart2.setPinchZoom(false);
        ArrayList<BarEntry> barEntries2 = new ArrayList<>();
        barEntries2.add(new BarEntry(1, 4000));
        barEntries2.add(new BarEntry(2, 3600));
        BarDataSet barDataSet2 = new BarDataSet(barEntries2, "Month");
        barDataSet2.setColors(Color.rgb(98, 0, 238));
        barDataSet2.setValueTextSize(14f);
        BarData barData2 = new BarData(barDataSet2);
        barData2.setBarWidth(0.3f);
        RoundedBarChartRenderer roundedBarChartRenderer2 = new RoundedBarChartRenderer(barChart2, barChart2.getAnimator(), barChart2.getViewPortHandler());
        roundedBarChartRenderer2.setmRadius(20f);
        barChart2.setRenderer(roundedBarChartRenderer2);
        barChart2.getXAxis().setDrawGridLines(false);
        barChart2.getLegend().setEnabled(false);
        barChart2.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart2.getDescription().setEnabled(false);
        barChart2.getAxisRight().setEnabled(false);
        barChart2.getAxisLeft().setEnabled(false);
        String[] labels2 = new String[3];
        labels2[0] = "jan";
        labels2[1] = "EMI";
        labels2[2] = "Balance";
        barChart2.getXAxis().setSpaceMax(1.5f);

        barChart2.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels2));
        barChart2.getXAxis().setTextSize(14f);
        YAxis yAxisRight2 = barChart2.getAxisRight();
        yAxisRight2.setAxisMinValue(50f);
        barChart2.setMinimumHeight(50);
        YAxis yAxisLeft2 = barChart2.getAxisLeft();
        yAxisLeft2.setAxisMinValue(50f);
        barChart2.setData(barData2);

        //BarChart3
        BarChart barChart3 = view.findViewById(R.id.barChart3);
        barChart3.setDrawBarShadow(false);
        barChart3.setDrawValueAboveBar(true);
        barChart3.setPinchZoom(false);
        ArrayList<BarEntry> barEntries3a = new ArrayList<>();
        ArrayList<BarEntry> barEntries3b = new ArrayList<>();
        barEntries3a.add(new BarEntry(1, credit.get(currentMonth-1).intValue()));
        barEntries3a.add(new BarEntry(2, credit.get(currentMonth).intValue()));
        barEntries3b.add(new BarEntry(1, balance.get(currentMonth-1).intValue()));
        barEntries3b.add(new BarEntry(2, credit.get(currentMonth).intValue()));
        BarDataSet barDataSet3a = new BarDataSet(barEntries3a, "Income");
        BarDataSet barDataSet3b=new BarDataSet(barEntries3b,"Expense");
        barDataSet3a.setValueTextSize(14);
        barDataSet3b.setValueTextSize(14);
        barDataSet3a.setColors(Color.rgb(3, 218, 198));
        barDataSet3b.setColors(Color.rgb(98, 0, 238));
        BarData barData3 = new BarData(barDataSet3a,barDataSet3b);
        barData3.setBarWidth(0.2f);
        RoundedBarChartRenderer roundedBarChartRenderer3 = new RoundedBarChartRenderer(barChart3, barChart3.getAnimator(), barChart3.getViewPortHandler());
        roundedBarChartRenderer3.setmRadius(20f);
        barChart3.setRenderer(roundedBarChartRenderer3);
        barChart3.getXAxis().setDrawGridLines(false);
        barChart3.getLegend().setEnabled(false);
        barChart3.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart3.getDescription().setEnabled(false);
        barChart3.getAxisRight().setEnabled(false);
        barChart3.getAxisLeft().setEnabled(false);
        String[] labels3 = new String[3];
        labels3[0] = "Feb";
        labels3[1] = "March";
        labels3[2]="April";
        barChart3.getXAxis().setTextSize(14f);
        barChart3.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels3));
        YAxis yAxisRight3 = barChart3.getAxisRight();
        yAxisRight3.setAxisMinValue(0f);
        barChart3.setMinimumHeight(0);
        YAxis yAxisLeft3 = barChart3.getAxisLeft();
        yAxisLeft3.setAxisMinValue(0f);
        float groupSpace = 0.5f;
        float barSpace = 0.1f;
        barChart3.setData(barData3);
        barChart3.groupBars(0.6f, groupSpace, barSpace);
        XAxis xAxis = barChart3.getXAxis();
        xAxis.setCenterAxisLabels(true);
        barChart3.invalidate();


    }

}

