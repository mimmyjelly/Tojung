package com.example.tojung;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<User> userList;
    private Activity context;
    private UserDB database;

    public UserAdapter(Activity context, List<User> userList){
        this.context = context;
        this.userList = userList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        final User user = userList.get(position);
        database = UserDB.getInstance(context);
        holder.textView.setText(user.getText());
        holder.btEdit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                User user = userList.get(holder.getAdapterPosition());

                final int sID = user.getId();
                String sText = user.getText();

                /**  팝업창  **/
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_update);

                int width = WindowManager.LayoutParams.MATCH_PARENT;
                int height = WindowManager.LayoutParams.WRAP_CONTENT;

                dialog.getWindow().setLayout(width, height);

                dialog.show();

                final EditText editText = dialog.findViewById(R.id.dialog_edit_text);
                Button bt_update= dialog.findViewById(R.id.bt_update);

                editText.setText(sText);

                bt_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        String uText = editText.getText().toString().trim();

                        database.userDao().update(sID, uText);

                        userList.clear();
                        userList.addAll(database.userDao().getAll());
                        notifyDataSetChanged();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        Button btEdit;

        public ViewHolder(@NonNull View view)
        {
            super(view);
            textView = view.findViewById(R.id.text_view);
            btEdit = view.findViewById(R.id.bt_edit);
        }
    }
}