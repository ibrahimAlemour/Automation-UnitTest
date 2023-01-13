package com.firstaid.firstaidapp.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firstaid.firstaidapp.MapsActivity;
import com.firstaid.firstaidapp.MapsActivityDoctor;
import com.firstaid.firstaidapp.MyInformation;
import com.firstaid.firstaidapp.R;
import com.firstaid.firstaidapp.model.AlarmDataSend;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.collect.Maps;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {

    Context context ;
    ArrayList<AlarmDataSend> listData ;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    public AlarmAdapter(Context context, ArrayList<AlarmDataSend> listData) {
        this.context = context;
        this.listData = listData;
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_alarm2 , parent , false);
        return new AlarmViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {

        final AlarmDataSend alarmData = listData.get(position);

        holder.name_tv.setText(alarmData.getName());
        holder.text_tv.setText(alarmData.getDes());
        holder.tvlevel.setText(alarmData.getLevel());

        if (alarmData.isStatus() == false){

            holder.imgLocations.setVisibility(View.GONE);
            holder.tvFinished.setVisibility(View.VISIBLE);

            holder.lineAlarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Finished", Toast.LENGTH_SHORT).show();
                }
            });


        }else if (alarmData.isStatus() == true){

            holder.imgLocations.setVisibility(View.VISIBLE);
            holder.tvFinished.setVisibility(View.GONE);

            holder.lineAlarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, MapsActivityDoctor.class);
                    intent.putExtra("lat",alarmData.getLocation());
                    intent.putExtra("call",alarmData.getPhone());
                    intent.putExtra("des",alarmData.getDes());
                    context.startActivity(intent);
                }
            });
        }



    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class AlarmViewHolder extends RecyclerView.ViewHolder {
        TextView name_tv , text_tv ,tvlevel,tvFinished;
        LinearLayout lineAlarm;
        ImageView imgLocations;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);

            name_tv = itemView.findViewById(R.id.tvPlantName);
            text_tv = itemView.findViewById(R.id.tvPlantType);
            tvlevel = itemView.findViewById(R.id.tvlevel);
            tvFinished = itemView.findViewById(R.id.tvFinished);
            lineAlarm = itemView.findViewById(R.id.lineAlarm);
            imgLocations = itemView.findViewById(R.id.imgLocations);




        }
    }



}
