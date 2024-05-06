package com.mihailap.olympiadfinder.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mihailap.olympiadfinder.R;
import com.mihailap.olympiadfinder.data.Olympiad;
import com.mihailap.olympiadfinder.databinding.ListItemBinding;

import java.util.ArrayList;
import java.util.List;

public class OlympiadAdapter extends RecyclerView.Adapter<OlympiadAdapter.OlympiadViewHolder> {
    private List<Olympiad> olympiadList = new ArrayList<>();

    public void setOlympiadList(List<Olympiad> olympiadList) {
        this.olympiadList = olympiadList;
        notifyDataSetChanged();
    }

    public List<Olympiad> getOlympiadListList() {
        return olympiadList;
    }

    @NonNull
    @Override
    public OlympiadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new OlympiadViewHolder(ListItemBinding.bind(view));
    }

    @Override
    public void onBindViewHolder(@NonNull OlympiadViewHolder holder, int position) {
        Olympiad olympiad = olympiadList.get(position);
        holder.binding.tvName.setText(olympiad.getName());
        holder.binding.tvSubject.setText(olympiad.getSubject());
        holder.binding.tvGrade.setText(olympiad.getGradeRange());

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onOlympiadClickListener != null) {
                    onOlympiadClickListener.onOlympiadClick(olympiad);
                }
            }
        });
    }

    private OnOlympiadClickListener onOlympiadClickListener;

    public void setOnOlympiadClickListener(OnOlympiadClickListener onOlympiadClickListener) {
        this.onOlympiadClickListener = onOlympiadClickListener;
    }

    interface OnOlympiadClickListener {
        void onOlympiadClick(Olympiad olympiad);
    }


    @Override
    public int getItemCount() {
        return olympiadList.size();
    }

    static class OlympiadViewHolder extends RecyclerView.ViewHolder {
        private final ListItemBinding binding;

        public OlympiadViewHolder(ListItemBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }
}
