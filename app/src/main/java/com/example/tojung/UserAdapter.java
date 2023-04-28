package com.example.tojung;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<User> users;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(User user);
    }

    public UserAdapter(List<User> users, OnItemClickListener listener) {
        this.users = users;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView guardianNameTextView;
        public TextView wakeUpTimeTextView;
        public TextView sleepTimeTextView;
        public TextView guardianPhoneNumberTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            guardianNameTextView = itemView.findViewById(R.id.guardian_name_textview);
            wakeUpTimeTextView = itemView.findViewById(R.id.wake_up_time_textview);
            sleepTimeTextView = itemView.findViewById(R.id.sleep_time_textview);
            guardianPhoneNumberTextView = itemView.findViewById(R.id.guardian_phone_number_textview);
        }

        public void bind(final User user, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(user);
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.guardianNameTextView.setText(users.get(position).guardianName);
        holder.wakeUpTimeTextView.setText(users.get(position).wakeUpTime);
        holder.sleepTimeTextView.setText(users.get(position).sleepTime);
        holder.guardianPhoneNumberTextView.setText(users.get(position).guardianPhoneNumber);
        holder.bind(users.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}