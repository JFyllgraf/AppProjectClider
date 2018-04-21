package com.example.nicolai.clider.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.nicolai.clider.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class TestStorageActivity extends AppCompatActivity {
    private StorageReference storageReference;
    final String imgURL = "https://firebasestorage.googleapis.com/v0/b/cliderappproject.appspot.com/o/testImages%2Fcat.jpg?alt=media&token=5de5dd9f-3c80-4caa-9485-bf877fb8107c";
    File localFile;
    ImageView imageTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_storage);
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference pathRef = storageReference.child("testImages/cat.jpg");
        imageTest = findViewById(R.id.imageViewtest);

        try {
            localFile = File.createTempFile("images", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }


        pathRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.d("Dowload", "onSuccess: File downloaded");
                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                imageTest.setImageBitmap(bitmap);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Download", "onFailure: File not downloaded");
            }
        });

    }
}
