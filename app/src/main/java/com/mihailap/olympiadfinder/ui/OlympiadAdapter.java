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
        this.olympiadList = new ArrayList<>(olympiadList);
        notifyDataSetChanged();
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
        if (olympiad.getSubject().split(",").length > 4) {
            String[] subjectsArray = olympiad.getSubject().split(",");
            holder.binding.tvSubject.setText(String.format("%s, %s, %s, %s + еще %s", subjectsArray[0],
                    subjectsArray[1], subjectsArray[2], subjectsArray[3], subjectsArray.length - 4));
        } else {
            holder.binding.tvSubject.setText(olympiad.getSubject());
        }
        holder.binding.tvGrade.setText(olympiad.getGradeRange());
        if (!olympiad.getTech()) {
            holder.binding.techIcon.setVisibility(View.GONE);
        } else {
            holder.binding.techIcon.setVisibility(View.VISIBLE);
        }
        if (!olympiad.getNature()) {
            holder.binding.natureIcon.setVisibility(View.GONE);
        } else {
            holder.binding.natureIcon.setVisibility(View.VISIBLE);
        }
        if (!olympiad.getHuman()) {
            holder.binding.humanIcon.setVisibility(View.GONE);
        } else {
            holder.binding.humanIcon.setVisibility(View.VISIBLE);
        }
        if (!olympiad.getSports()) {
            holder.binding.sportsIcon.setVisibility(View.GONE);
        } else {
            holder.binding.sportsIcon.setVisibility(View.VISIBLE);
        }

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
