package com.example.tojung;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//RecyclerView에 메시지 표시하는 역할
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    List<Message> messageList;

    public MessageAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messageList.get(position);

        // 유저 입력시
        if(message.getSentBy().equals(Message.SENT_BY_ME)){
            holder.left_chat_view.setVisibility(View.GONE);
            holder.right_chat_view.setVisibility(View.VISIBLE);
            holder.right_chat_tv.setText(message.getMessage());
        } else { //시스템 입력 시
            holder.right_chat_view.setVisibility(View.GONE);
            holder.left_chat_view.setVisibility(View.VISIBLE);
            holder.left_chat_tv.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout left_chat_view, right_chat_view;
        TextView left_chat_tv, right_chat_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            left_chat_view = itemView.findViewById(R.id.chatscroll);    //챗대답
            right_chat_view = itemView.findViewById(R.id.quesview);     //질문
            left_chat_tv = itemView.findViewById(R.id.chatting);        //챗대답 텍스트뷰
            right_chat_tv = itemView.findViewById(R.id.ques);           //질문 텍스트뷰
        }
    }
}