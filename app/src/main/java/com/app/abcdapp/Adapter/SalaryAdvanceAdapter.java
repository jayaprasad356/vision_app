package com.app.abcdapp.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.abcdapp.databinding.SalaryAdvanceModelBinding;
import com.app.abcdapp.model.AdvanceWithdrawlModel;

import java.util.ArrayList;

    public class SalaryAdvanceAdapter extends RecyclerView.Adapter<SalaryAdvanceAdapter.viewHolder> {

        private SalaryAdvanceModelBinding binding;
        private final ArrayList<AdvanceWithdrawlModel> advanceWithdrawlModels;
        public SalaryAdvanceAdapter(Activity activity,ArrayList<AdvanceWithdrawlModel> advanceWithdrawlModels) {
            this.advanceWithdrawlModels = advanceWithdrawlModels;
        }
        protected  class viewHolder extends RecyclerView.ViewHolder {

            public viewHolder(@NonNull SalaryAdvanceModelBinding itemView) {
                super(itemView.getRoot());
            }
        }

        @NonNull
        @Override
        public SalaryAdvanceAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            binding = SalaryAdvanceModelBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
            return new SalaryAdvanceAdapter.viewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull SalaryAdvanceAdapter.viewHolder holder, int position) {
            final AdvanceWithdrawlModel model = advanceWithdrawlModels.get(position);

            binding.tvAmount.setText(model.getAmount());
            binding.tvTransactionStatus.setText(model.getType());
            binding.tvTime.setText(model.getDate());
        }

        @Override
        public int getItemCount() {
            return advanceWithdrawlModels.size();
        }
    }

