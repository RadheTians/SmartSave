package com.save.smartsave.ui.insight;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.save.smartsave.R;

import java.util.ArrayList;

import static com.save.smartsave.MainActivity.categoryName;
import static com.save.smartsave.MainActivity.currentMonthCategory;
import static com.save.smartsave.MainActivity.previousMonthCategory;
import static com.save.smartsave.MainActivity.sumOfCategory;

public class InsightCategoryRecyclerAdapter extends RecyclerView.Adapter<InsightCategoryRecyclerAdapter.ViewHolder> {

    Context context;

    public InsightCategoryRecyclerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.insight_category_recycler_singleitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.headingCategory.setText(categoryName.get(position));
        holder.incrementAmountCategory.setText(String.valueOf(currentMonthCategory.get(position).intValue()-previousMonthCategory.get(position).intValue()));
        holder.incrementPercentCategory.setText(String.valueOf(((currentMonthCategory.get(position).intValue()-previousMonthCategory.get(position).intValue())*100)));
        setPieChart1(holder.pieChartCategory,currentMonthCategory.get(position).intValue(),sumOfCategory.intValue());
    }

    @Override
    public int getItemCount() {
        return currentMonthCategory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView headingCategory,incrementAmountCategory,incrementPercentCategory;
        PieChart pieChartCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            headingCategory=itemView.findViewById(R.id.headingCategory);
            incrementAmountCategory=itemView.findViewById(R.id.incrementAmountCategory);
            incrementPercentCategory=itemView.findViewById(R.id.incrementPercentCategory);
            pieChartCategory=itemView.findViewById(R.id.pieChartCategory);
        }
    }

    private void setPieChart1(PieChart pieChart,float categoryValue,float totalCategory){
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
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
}
