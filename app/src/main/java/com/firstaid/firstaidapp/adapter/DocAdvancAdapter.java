package com.firstaid.firstaidapp.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firstaid.firstaidapp.R;
import com.firstaid.firstaidapp.doctors.AddAdvancActivity;
import com.firstaid.firstaidapp.model.Advance;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class DocAdvancAdapter extends RecyclerView.Adapter<DocAdvancAdapter.AlarmViewHolder> {

    Context context;
    ArrayList<Advance> listData;
    FirebaseFirestore db;


    public DocAdvancAdapter(Context context, ArrayList<Advance> listData) {
        this.context = context;
        this.listData = listData;
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_adv2, parent, false);
        return new AlarmViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final Advance advance = listData.get(position);
        db = FirebaseFirestore.getInstance();
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

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddAdvancActivity.class);
                intent.putExtra("ad",advance);
                context.startActivity(intent);
            }
        });


        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.collection("Advance").document(advance.getId())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                listData.remove(position);
                                notifyDataSetChanged();

                                Toast.makeText(context, "Remove success", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    class AlarmViewHolder extends RecyclerView.ViewHolder {
        TextView name_tv, text_tv;
        private ImageView imgAdvance,imgEdit,imgDelete;
        private ProgressBar pBarLoad;
        private ImageView imgYoutube;
        LinearLayout lineAlarm;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);

            name_tv = itemView.findViewById(R.id.tvPlantName);
            text_tv = itemView.findViewById(R.id.tvPlantType);
            lineAlarm = itemView.findViewById(R.id.lineAlarm);
            imgAdvance = itemView.findViewById(R.id.imgAdvance);
            imgEdit = itemView.findViewById(R.id.imgEdit);
            imgDelete = itemView.findViewById(R.id.imgDelete);
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
