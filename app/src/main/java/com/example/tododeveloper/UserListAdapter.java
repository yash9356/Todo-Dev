package com.example.tododeveloper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tododeveloper.db.User;

import java.util.List;


public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder> {

    private Context context;
    private List<User> userList;
    public UserListAdapter(Context context) {
        this.context = context;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.recycler_event_item, parent, false);

       return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.MyViewHolder holder, int position) {
        holder.EventTitle.setText(this.userList.get(position).firstName);
        holder.EventDescription.setText(this.userList.get(position).lastName);
//        if (!this.userList.get(position).eventtime.isEmpty()){
//            holder.Time1.setText(this.userList.get(position).eventtime);
//        }
        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               AddNewUserActivity.getInstance().cancelAlarm();
                MainActivity.getInstance().deleteEvent(userList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return  this.userList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView EventTitle;
        TextView EventDescription,Time1;
        ImageView delete_btn;
        public MyViewHolder(View view) {
            super(view);
            Time1 =view.findViewById(R.id.Event_Time7);
             EventTitle = view.findViewById(R.id.Event_name);
             EventDescription = view.findViewById(R.id.Event_discription);
             delete_btn =view.findViewById(R.id.delete_btn);
        }
    }
}
