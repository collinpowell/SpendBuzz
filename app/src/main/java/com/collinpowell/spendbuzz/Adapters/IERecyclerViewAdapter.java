package com.collinpowell.spendbuzz.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.collinpowell.spendbuzz.Models.IEModel;
import com.collinpowell.spendbuzz.R;

import java.util.ArrayList;

public class IERecyclerViewAdapter extends RecyclerView.Adapter<IERecyclerViewAdapter.IEViewHolder> {
    ArrayList<IEModel> items;

    public IERecyclerViewAdapter(ArrayList<IEModel> items) {
        this.items = items;
    }

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    @NonNull
    @Override
    public IERecyclerViewAdapter.IEViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.income_expence_recycler_design, parent, false);
        IEViewHolder ViewHolder = new IEViewHolder(view, mListener);
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IERecyclerViewAdapter.IEViewHolder holder, int position) {
        IEModel model = items.get(position);

        holder.name.setText(model.getName());
         holder.type.setText(model.getType());
         holder.period.setText(model.getPeriod());
         switch (model.getPeriod()){
             case "Monthly":
                 holder.period.setTextColor(holder.itemView.getResources().getColor(R.color.Monthly));
                 break;
             case "Weekly":
                 holder.period.setTextColor(holder.itemView.getResources().getColor(R.color.Weekly));
                 break;
             case "Daily":
                 holder.period.setTextColor(holder.itemView.getResources().getColor(R.color.Daily));
                 break;
             case "Yearly":
                 holder.period.setTextColor(holder.itemView.getResources().getColor(R.color.Yearly));
                 break;
             case "One Time":
                 holder.period.setTextColor(holder.itemView.getResources().getColor(R.color.red));
                 break;
             default:
                 holder.period.setTextColor(holder.itemView.getResources().getColor(R.color.gold));
                 break;

         }
        if(model.getType().equals("Income")){
            holder.amount.setText("Current: " +String.format("%,d",Long.parseLong(String.valueOf(model.getAmount())))+" NGN");
            holder.type.setTextColor(holder.itemView.getResources().getColor(R.color.green));
        }else {
            holder.amount.setText("Budgeted: " +String.format("%,d",Long.parseLong(String.valueOf(model.getAmount())))+" NGN");
            holder.type.setTextColor(holder.itemView.getResources().getColor(R.color.blue));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class IEViewHolder extends RecyclerView.ViewHolder {
        Button editButton;
        TextView name,amount,type,period;
        ImageView delete;
        public IEViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            editButton = itemView.findViewById(R.id.edit_button);
            name = itemView.findViewById(R.id.name);
            amount = itemView.findViewById(R.id.amount);
            type = itemView.findViewById(R.id.type);
            period = itemView.findViewById(R.id.period);
            delete = itemView.findViewById(R.id.delete);

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onEditClick(position);
                        }
                    }
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });


        }
    }
}
