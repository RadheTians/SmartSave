package com.save.smartsave.ui.insight;

import android.graphics.Color;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.save.smartsave.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.save.smartsave.MainActivity.balance;
import static com.save.smartsave.MainActivity.categoryName;
import static com.save.smartsave.MainActivity.credit;
import static com.save.smartsave.MainActivity.currentMonthCategory;
import static com.save.smartsave.MainActivity.offlineTransaction;
import static com.save.smartsave.MainActivity.onlineTransaction;
import static com.save.smartsave.MainActivity.previousMonthCategory;
import static com.save.smartsave.MainActivity.sumOfCategory;

public class InsightFragment extends Fragment {

    RecyclerView recyclerViewInsightsCategory;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_insight, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LineChart mLineGraph = view.findViewById(R.id.lineChart1);
        setLineGraph1(mLineGraph);
        //first pie
        PieChart pieChart2 = view.findViewById(R.id.pie_chart_2);
        setPieChart1(pieChart2,currentMonthCategory.get(0).intValue(),sumOfCategory.intValue());
        TextView insightsAmountIncreasePie1,insightsPercentageIncreasePie1,insightsNameOfMaxExpensePie1;
        insightsAmountIncreasePie1=view.findViewById(R.id.insightsAmountIncreasePie1);
        insightsPercentageIncreasePie1=view.findViewById(R.id.insightsPercentageIncreasePie1);
        insightsNameOfMaxExpensePie1=view.findViewById(R.id.insightsNameOfMaxExpensePie1);
        insightsAmountIncreasePie1.setText(String.valueOf(currentMonthCategory.get(1).intValue()-previousMonthCategory.get(1).intValue()));
        insightsPercentageIncreasePie1.setText(String.valueOf(((currentMonthCategory.get(1).intValue()-previousMonthCategory.get(1).intValue())*100)));
        insightsNameOfMaxExpensePie1.setText("Your spend on "+ String.valueOf(categoryName.get(1))+" is Highest");

        //first Category pie chart
        recyclerViewInsightsCategory=view.findViewById(R.id.insightsRecyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        recyclerViewInsightsCategory.setLayoutManager(layoutManager);
        InsightCategoryRecyclerAdapter insightCategoryRecyclerAdapter=new InsightCategoryRecyclerAdapter(getContext());
        recyclerViewInsightsCategory.setAdapter(insightCategoryRecyclerAdapter);

        final LinearLayout categoryInsights=view.findViewById(R.id.categoryInsights);
        final CardView cardView=view.findViewById(R.id.card_main_category);
        categoryInsights.setVisibility(View.GONE);
        final Button buttonCategoryOpen =view.findViewById(R.id.openCategory);
        buttonCategoryOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { if(categoryInsights.getVisibility()==View.GONE){
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    categoryInsights.setVisibility(View.VISIBLE);
                    buttonCategoryOpen.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_up_black_24dp, 0);
                }else{
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    categoryInsights.setVisibility(View.GONE);
                    buttonCategoryOpen.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down_black_24dp, 0);

                } }});

        PieChart transactionPie=view.findViewById(R.id.transaction_pie_chart);
        setPieChart3(transactionPie,offlineTransaction.get(0).intValue(),onlineTransaction.get(0).intValue());

        PieChart onlinePaymentMode=view.findViewById(R.id.online_payment_mode_pie_chart);
        setPieChart2(onlinePaymentMode);
    }

    private void setPieChart1(PieChart pieChart,float categoryValue,float totalCategory){
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
//        pieChart.setExtraOffsets(5,10,5,10);
        pieChart.setDragDecelerationFrictionCoef(.80f);
        pieChart.setDrawHoleEnabled(true);
        ArrayList<PieEntry> pieEntery1 = new ArrayList<>();
        pieEntery1.add(new PieEntry(totalCategory-categoryValue, ""));
        pieEntery1.add(new PieEntry(categoryValue, ""));
        PieDataSet dataSet = new PieDataSet(pieEntery1, "Saving");
        dataSet.setSliceSpace(5);
        pieChart.getLegend().setEnabled(false);
        PieData data = new PieData(dataSet);
        pieChart.animateY(1000, Easing.EaseInBounce);
        data.setValueTextSize(0);
        final int[] MY_COLORS = {Color.rgb(236,236,244), Color.rgb(98, 0, 238)};
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for(int c: MY_COLORS) colors.add(c);
        dataSet.setColors(colors);
        pieChart.setTransparentCircleRadius(70);
        if(totalCategory!=0){
            pieChart.setCenterText(((categoryValue/totalCategory)*100)+"%");
        }
        pieChart.setCenterTextSize(20f);
        pieChart.setCenterTextColor(Color.rgb(98, 0, 238));
        pieChart.setData(data);
    }

    private void setPieChart2(PieChart pieChart){
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
//        pieChart.setExtraOffsets(5,10,5,10);
        pieChart.setDragDecelerationFrictionCoef(.80f);
        pieChart.setDrawHoleEnabled(true);
        ArrayList<PieEntry> pieEntery1 = new ArrayList<>();
        pieEntery1.add(new PieEntry(80, ""));
        pieEntery1.add(new PieEntry(20, ""));
        PieDataSet dataSet = new PieDataSet(pieEntery1, "Saving");
        dataSet.setSliceSpace(5);
        pieChart.getLegend().setEnabled(false);
        PieData data = new PieData(dataSet);
        pieChart.animateY(1000, Easing.EaseInBounce);
        data.setValueTextSize(0);
        final int[] MY_COLORS = {Color.rgb(236,236,244), Color.rgb(98, 0, 238)};
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for(int c: MY_COLORS) colors.add(c);
        dataSet.setColors(colors);
        pieChart.setTransparentCircleRadius(70);
        pieChart.setCenterText("20%");
        pieChart.setCenterTextSize(20f);
        pieChart.setCenterTextColor(Color.rgb(98, 0, 238));

        pieChart.setData(data);
    }

    private void setPieChart3(PieChart pieChart,float offline,float online){
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
//        pieChart.setExtraOffsets(5,10,5,10);
        pieChart.setDragDecelerationFrictionCoef(.80f);
        pieChart.setDrawHoleEnabled(true);
        ArrayList<PieEntry> pieEntery1 = new ArrayList<>();
        pieEntery1.add(new PieEntry(offline, "Offline"));
        pieEntery1.add(new PieEntry(online, "Online"));
        PieDataSet dataSet = new PieDataSet(pieEntery1, "Saving");
        dataSet.setSliceSpace(5);
        pieChart.getLegend().setEnabled(false);
        PieData data = new PieData(dataSet);
        pieChart.animateY(1000, Easing.EaseInBounce);
        data.setValueTextSize(0);
        final int[] MY_COLORS = {Color.rgb(236,236,244), Color.rgb(98, 0, 238)};
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for(int c: MY_COLORS) colors.add(c);
        dataSet.setColors(colors);
        pieChart.setTransparentCircleRadius(70);
        pieChart.setCenterTextSize(20f);
        pieChart.setCenterTextColor(Color.rgb(98, 0, 238));
        pieChart.setData(data);
    }

    private void setLineGraph1(LineChart mLineGraph) {
        //Line Graph
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        List<String> xAxisValues = new ArrayList<>(Arrays.asList("Jan", "Feb", "March", "April", "May", "June","July", "August", "September", "October", "November", "Decemeber"));
        List<Entry> incomeEntries =getIncomeEntries();
        dataSets = new ArrayList<>();
        LineDataSet set1;
        set1 = new LineDataSet(incomeEntries, "Income");
        set1.setColor(Color.rgb(65, 168, 121));
        set1.setValueTextColor(Color.rgb(55, 70, 73));
        set1.setValueTextSize(10f);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSets.add(set1);
        //customization
        mLineGraph.setTouchEnabled(true);
        mLineGraph.setDragEnabled(true);
        mLineGraph.setScaleEnabled(false);
        mLineGraph.setPinchZoom(false);
        mLineGraph.setDrawGridBackground(false);
        mLineGraph.setExtraLeftOffset(15);
        mLineGraph.setExtraRightOffset(15);
        //to hide background lines
        mLineGraph.getXAxis().setDrawGridLines(false);
        mLineGraph.getAxisLeft().setDrawGridLines(false);
        mLineGraph.getAxisRight().setDrawGridLines(false);
        //to hide right Y and top X border
        mLineGraph.getAxisRight().setEnabled(false);
        mLineGraph.getAxisLeft().setEnabled(true);
        mLineGraph.getXAxis().setEnabled(false);
        XAxis xAxis = mLineGraph.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setEnabled(true);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        set1.setLineWidth(4f);
        set1.setCircleRadius(3f);
        set1.setDrawValues(false);
        set1.setCircleHoleColor(getResources().getColor(R.color.colorPrimary));
        set1.setCircleColor(getResources().getColor(R.color.colorPrimary));
        //String setter in x-Axis
        mLineGraph.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisValues));
        LineData data1 = new LineData(dataSets);
        mLineGraph.setData(data1);
        mLineGraph.animateX(1000);
        mLineGraph.invalidate();
        mLineGraph.getLegend().setEnabled(false);
        mLineGraph.getDescription().setEnabled(false);
    }

    private List<Entry> getIncomeEntries() {
        ArrayList<Entry> incomeEntries = new ArrayList<>();

        incomeEntries.add(new Entry(1, credit.get(0).intValue()-balance.get(0).intValue()));
        incomeEntries.add(new Entry(2, credit.get(1).intValue()-balance.get(1).intValue()));
        incomeEntries.add(new Entry(3, credit.get(2).intValue()-balance.get(2).intValue()));
        incomeEntries.add(new Entry(4, credit.get(3).intValue()-balance.get(3).intValue()));
        incomeEntries.add(new Entry(5, credit.get(4).intValue()-balance.get(4).intValue()));
        incomeEntries.add(new Entry(6, credit.get(5).intValue()-balance.get(5).intValue()));
        incomeEntries.add(new Entry(7, credit.get(6).intValue()-balance.get(6).intValue()));
        incomeEntries.add(new Entry(8, credit.get(7).intValue()-balance.get(7).intValue()));
        incomeEntries.add(new Entry(9, credit.get(8).intValue()-balance.get(8).intValue()));
        incomeEntries.add(new Entry(10, credit.get(9).intValue()-balance.get(9).intValue()));
        incomeEntries.add(new Entry(11, credit.get(10).intValue()-balance.get(10).intValue()));
        incomeEntries.add(new Entry(12, credit.get(11).intValue()-balance.get(11).intValue()));
        return incomeEntries.subList(0, 12);
    }


}