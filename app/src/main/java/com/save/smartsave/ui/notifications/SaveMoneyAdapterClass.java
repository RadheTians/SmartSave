package com.save.smartsave.ui.notifications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.save.smartsave.R;

import java.util.List;

public class SaveMoneyAdapterClass extends RecyclerView.Adapter<SaveMoneyAdapterClass.SaveMoneyViewHolder> {
    List<SaveMoneyModelClass> saveMoneyModelClassList;
    Context context;

    public SaveMoneyAdapterClass(List<SaveMoneyModelClass> saveMoneyModelClassList, Context context) {
        this.saveMoneyModelClassList = saveMoneyModelClassList;
        this.context = context;
    }

    @NonNull
    @Override
    public SaveMoneyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SaveMoneyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.save_money_single_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SaveMoneyViewHolder holder, int position) {
        holder.heading.setText(saveMoneyModelClassList.get(position).getHeading());
        holder.body.setText(saveMoneyModelClassList.get(position).getBody());
        holder.amonunt.setText((saveMoneyModelClassList.get(position).getAmount()));
    }

    @Override
    public int getItemCount() {
        return saveMoneyModelClassList.size();
    }

    public class SaveMoneyViewHolder extends RecyclerView.ViewHolder {
        private TextView heading,body,amonunt;
        public SaveMoneyViewHolder(@NonNull View itemView) {
            super(itemView);
            heading = itemView.findViewById(R.id.heading_save_money);
            body = itemView.findViewById(R.id.body_save_money);
            amonunt = itemView.findViewById(R.id.amount_save_money);
        }
    }
}
