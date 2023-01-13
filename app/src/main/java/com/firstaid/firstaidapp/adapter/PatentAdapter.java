package com.firstaid.firstaidapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firstaid.firstaidapp.MapsActivityDoctor;
import com.firstaid.firstaidapp.MyInformation;
import com.firstaid.firstaidapp.R;
import com.firstaid.firstaidapp.doctors.DetlPatientActivity;
import com.firstaid.firstaidapp.model.AlarmDataSend;
import com.firstaid.firstaidapp.model.Patient;

import java.util.ArrayList;


public class PatentAdapter extends RecyclerView.Adapter<PatentAdapter.AlarmViewHolder> {

    Context context ;
    ArrayList<Patient> listData ;

    public PatentAdapter(Context context, ArrayList<Patient> listData) {
        this.context = context;
        this.listData = listData;
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_alarm , parent , false);
        return new AlarmViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {

        final Patient patient = listData.get(position);

        holder.name_tv.setText(patient.getName());
        holder.text_tv.setText(patient.getGender());

        holder.lineAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, DetlPatientActivity.class);
                intent.putExtra("pa",patient);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class AlarmViewHolder extends RecyclerView.ViewHolder {
        TextView name_tv , text_tv ;
        LinearLayout lineAlarm;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);

            name_tv = itemView.findViewById(R.id.tvPlantName);
            text_tv = itemView.findViewById(R.id.tvPlantType);
            lineAlarm = itemView.findViewById(R.id.lineAlarm);




        }
    }
}
