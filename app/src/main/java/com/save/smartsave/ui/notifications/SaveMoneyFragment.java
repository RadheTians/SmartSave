package com.save.smartsave.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.save.smartsave.R;

import java.util.ArrayList;
import java.util.List;

public class SaveMoneyFragment extends Fragment {
    List<SaveMoneyModelClass> saveMoneyModelClassList;
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_save_money, container, false);
        recyclerView=root.findViewById(R.id.save_money_recycler_view);
        SaveMoneyAdapterClass saveMoneyAdapterClass=new SaveMoneyAdapterClass(saveMoneyModelClassList,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(saveMoneyAdapterClass);
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saveMoneyModelClassList=new ArrayList<>();
        saveMoneyModelClassList.add(new SaveMoneyModelClass("Cut ₹ 500 in movies expenses","400+ users found this recommendation useful and saved  ₹ 450 on average.","+300"));
        saveMoneyModelClassList.add(new SaveMoneyModelClass("Heads up on Amazon Prime trial","You had  authorized card 6234 XXXX XXXX 4312 for free trial of Amazon Prime. Cancel now if you don't want to continue with trial to avoid auto debit.","+700"));
        saveMoneyModelClassList.add(new SaveMoneyModelClass("Cancel Netflix subscription","Your Netflix subscription is about to expire. Cancel before 25th April if you don't need it anymore to avoid auto renewal and save Rs.999","+800"));
        saveMoneyModelClassList.add(new SaveMoneyModelClass("Save ₹ 400 on Travel","Your spend on Ola / Uber cabs has increased. You travel during peak hours. You can use ‘Ola share’ and cut min. ₹ 200 on travel expenses.","+100"));
        saveMoneyModelClassList.add(new SaveMoneyModelClass("Save ₹ 300 on Grocery","Consider ordering through Grofers app with 10% discount on all orders.","+1000"));
        saveMoneyModelClassList.add(new SaveMoneyModelClass("Save ₹ 100 on Electricity Bill","Consider Paying through Mobikwik app with 10% discount on electricity  bill.","+600"));

    }
}