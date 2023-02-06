package com.androidproject.iottechhrm.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidproject.iottechhrm.R;
import com.androidproject.iottechhrm.activity.DashBoardActivity;
import com.androidproject.iottechhrm.inter.OnRecyler;
import com.androidproject.iottechhrm.model.Dash_List;

import java.util.ArrayList;
import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.MyViewHolder> {

    Context context;
    List<Dash_List> dash_lists;
    OnRecyler onRecyler;

    public DashboardAdapter(Context context, List<Dash_List> dash_lists, OnRecyler onRecyler) {
        this.context=context;
        this.dash_lists=dash_lists;
        this.onRecyler=onRecyler;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_list,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Dash_List myList=dash_lists.get(position);
        holder.imageView.setImageDrawable(context.getResources().getDrawable(myList.getImage()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=myList.getName();
                onRecyler.onClick(name,position);
            }
        });


    }


    @Override
    public int getItemCount() {
        return dash_lists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.myimage);
        }
    }
}
