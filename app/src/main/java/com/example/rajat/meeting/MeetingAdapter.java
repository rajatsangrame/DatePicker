package com.example.rajat.meeting;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rajat.meeting.databinding.ListItemBinding;
import com.example.rajat.meeting.model.Meeting;

import java.util.List;

/**
 * Created by Rajat Sangrame on 31/12/19.
 * http://github.com/rajatsangrame
 */
public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.ViewHolder> {

    private List<Meeting> mList;

    public MeetingAdapter(List<Meeting> list) {
        mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        ListItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.binding.setMeeting(mList.get(position));
        holder.binding.executePendingBindings();

    }

    @Override
    public int getItemCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ListItemBinding binding;

        public ViewHolder(@NonNull ListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
