package com.firstaid.firstaidapp.adapter;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.firstaid.firstaidapp.R;
import com.firstaid.firstaidapp.model.Advance;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class AdvancAdapter extends RecyclerView.Adapter<AdvancAdapter.AlarmViewHolder> {

    Context context;
    ArrayList<Advance> listData;



    public AdvancAdapter(Context context, ArrayList<Advance> listData) {
        this.context = context;
        this.listData = listData;
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_adv, parent, false);
        return new AlarmViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {

        final Advance advance = listData.get(position);

        holder.name_tv.setText(advance.getTitle());
        holder.text_tv.setText(advance.getDes());

        Picasso.get()
                .load(advance.image)
                .into(holder.imgAdvance, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        holder.pBarLoad.setVisibility(View.GONE);
                        holder.imgAdvance.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onError(Exception e) {

                    }

                });

        holder.imgAdvance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog(advance.image);
            }
        });

        holder.imgYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(advance.urlYoutube));
                appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                appIntent.setPackage("com.google.android.youtube");
                context.startActivity(appIntent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    class AlarmViewHolder extends RecyclerView.ViewHolder {
        TextView name_tv, text_tv;
        private ImageView imgAdvance;
        private ProgressBar pBarLoad;
        private ImageView imgYoutube;
        LinearLayout lineAlarm;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);

            name_tv = itemView.findViewById(R.id.tvPlantName);
            text_tv = itemView.findViewById(R.id.tvPlantType);
            lineAlarm = itemView.findViewById(R.id.lineAlarm);
            imgAdvance = itemView.findViewById(R.id.imgAdvance);
            pBarLoad = itemView.findViewById(R.id.pBarLoad);
            imgYoutube = itemView.findViewById(R.id.imgYoutube);


        }
    }

    public void showCustomDialog(String imageUrl) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.img_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


      //  final ImageView imgAdvance = dialog.findViewById(R.id.imgAdvance);
        PhotoView imgAdvance = (PhotoView) dialog.findViewById(R.id.imgAdvance);
        final ProgressBar pBarLoad = dialog.findViewById(R.id.pBarLoad);




        Picasso.get()
                .load(imageUrl)
                .into(imgAdvance, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        pBarLoad.setVisibility(View.GONE);
                        imgAdvance.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onError(Exception e) {

                    }

                });



        dialog.show();
    }

}
