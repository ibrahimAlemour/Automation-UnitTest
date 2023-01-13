package com.firstaid.firstaidapp.doctors;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.firstaid.firstaidapp.R;
import com.firstaid.firstaidapp.model.Advance;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

public class AddAdvancActivity extends AppCompatActivity {

    ProgressDialog pd;
    private ImageView imgBack;
    private EditText etTitle;
    private EditText etDEs;
    private AppCompatButton btnPost;
    Bitmap bitmap;
    FirebaseStorage storage;
    StorageReference storageRef;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    Uri uri;
    String nameImage;
    private EditText etUrl;
    private ImageView imgAdvance;
    private TextView tvPathImg;
    Advance advance;
    private TextView tvT;
    private LinearLayout linearImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_advanc);
        initView();

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        db = FirebaseFirestore.getInstance();

        advance = (Advance) getIntent().getSerializableExtra("ad");
        if (advance != null) {

            tvT.setText("Edit Advance");
            etTitle.setText(advance.getTitle());
            etDEs.setText(advance.getDes());
            etUrl.setText(advance.getUrlYoutube());
            linearImg.setVisibility(View.GONE);
            Picasso.get()
                    .load(advance.image)
                    .into(imgAdvance, new Callback() {
                        @Override
                        public void onSuccess() {

                            imgAdvance.setVisibility(View.VISIBLE);

                        }

                        @Override
                        public void onError(Exception e) {

                        }

                    });


        }

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (advance != null) {

                    updateAdvance(advance.getId(), etTitle.getText().toString()
                            , etDEs.getText().toString()
                            , etUrl.getText().toString());
                } else {
                    uploadImage(etTitle.getText().toString(), etDEs.getText().toString(), etUrl.getText().toString());
                }


            }
        });


        //ImageProfile

        imgAdvance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent, 100);

            }
        });


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void postAdv(String title, String des) {

        pd = new ProgressDialog(this);
        pd.setMessage("loading...");
        pd.show();

        Advance advance = new Advance("", title, des, "", "");

        db.collection("Advance")
                .add(advance)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Toast.makeText(AddAdvancActivity.this, "Post Success", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", "Error adding Account", e);
                    }
                });
    }

    void updateAdvance(String id, String title, String des, String url) {

        ProgressDialog pd = new ProgressDialog(AddAdvancActivity.this);
        pd.setMessage("loading...");
        pd.show();

        db.collection("Advance").document(id)
                .update("des", des
                        , "title", title
                        , "urlYoutube", url
                )
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        pd.dismiss();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                    }
                });


    }

    void uploadImage(String title, String des, String urlYoutube) {

        Random rand = new Random();
        int n = rand.nextInt(10000);

        pd = new ProgressDialog(this);
        pd.setMessage("loading...");
        pd.show();

        Uri uri1 = Uri.parse(tvPathImg.getText().toString());

        StorageReference storageRef = storage.getReference().child("images/").child(System.currentTimeMillis() + ".jpg");
        UploadTask uploadTask = storageRef.putFile(uri1);


        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (task.isSuccessful()) {

                    //here the upload of the image finish
                }

                // Continue the task to get a download url
                return storageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult(); //this is the download url that you need to pass to your database
                    //Pass the url to your reference

                    Advance advance = new Advance(n + "", title, des, downloadUri.toString(), urlYoutube);

                    db.collection("Advance").document(n + "")
                            .set(advance)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {


                                    Toast.makeText(AddAdvancActivity.this, "Post Success", Toast.LENGTH_SHORT).show();
                                    pd.dismiss();
                                    finish();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });


                }
            }
        });

    }


    private void initView() {
        imgBack = (ImageView) findViewById(R.id.imgBack);
        etTitle = (EditText) findViewById(R.id.etTitle);
        etDEs = (EditText) findViewById(R.id.etDEs);
        btnPost = (AppCompatButton) findViewById(R.id.btnPost);
        etUrl = (EditText) findViewById(R.id.etUrl);
        imgAdvance = (ImageView) findViewById(R.id.imgAdvance);
        tvPathImg = (TextView) findViewById(R.id.tvPathImg);
        tvT = (TextView) findViewById(R.id.tvT);
        linearImg = (LinearLayout) findViewById(R.id.linearImg);
    }


    //EditImageProfile

    void copyImage(Bitmap bitmap) {

        File data = Environment.getDataDirectory();

        // انشاء ملف داخلل داتا التطبيق لصور
        String currentDBPath = "//data//" + this.getPackageName() + "//images//" + "";
        File fileDir = new File(data, currentDBPath);

        if (fileDir.exists()) {
            Log.e(this.getPackageName(), "Found Dir");
        } else { // اذا مش موجود اصنعة
            fileDir.mkdir();
            Log.e(this.getPackageName(), "Make DirDone");

        }


        String fileName = String.format("%d.jpg", System.currentTimeMillis()); // لعدم تكرار الصورة
        File outFile = new File(fileDir, fileName);
        Log.e("ibm", "outFile : " + outFile);
        try {
            // نسخ الصورة في الملف بصيغتها العالية وهي 100
            FileOutputStream outStream = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
            Log.e(this.getPackageName(), "Save image Done !");
            Log.e(this.getPackageName(), " image path : " + outFile.getAbsolutePath());
            Log.e("ibm", "outFilePath : " + outFile.getAbsolutePath());
            String[] parts = outFile.getAbsolutePath().split("images/");
            nameImage = parts[1];
            Log.e("nameImage", "" + nameImage);
            // tvPathImg.setText(outFile.getAbsolutePath());


        } catch (Exception ex) { // اعطاء خطاء اذا لم يتم نسخ الصورة في الفايل الخاص بصور الذي تم انشاءه

            Log.e(this.getPackageName(), "Save image Error");

            Log.e(this.getPackageName(), " " + ex.getMessage());

        }

    }

    // اخذ الصورة من الاستيديو
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // MyPreferences.context = this;
        if (requestCode == 100 && resultCode == RESULT_OK) {
            uri = data.getData();
            tvPathImg.setText(uri + "");
            Log.e("uri", uri + "");
            try {
                //عرض صورة من الاستيديو
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                imgAdvance.setImageBitmap(bitmap);

                Log.e("path", uri.getPath());
                copyImage(bitmap);

            } catch (Exception ex) {

                Log.e(this.getPackageName(), "Get image Error");

                Log.e(this.getPackageName(), " " + ex.getMessage());
            }


            //Toast.makeText(this, "تم عرض الصورة", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
        }


    }
}