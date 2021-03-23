package com.openclassrooms.connexion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class NewDestination extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    Button Btn_C_Image;
    Button BtnConf;
    EditText T_V_Lieu;
    EditText T_V_Pays;
    EditText T_V_Env;
    ImageView I_V_Des;
    ProgressBar ProgressBar;

    private Uri UriImg;
    private StorageReference StorageRef;
    private DatabaseReference DatabaseRef;
    private StorageTask UploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_destination);

        Btn_C_Image = findViewById(R.id.BtnImg) ;
        BtnConf = findViewById(R.id.Btn_Valider_ND);
        T_V_Lieu = findViewById(R.id.eT_lieu);
        T_V_Pays = findViewById(R.id.eT_pays);
        T_V_Env = findViewById(R.id.eT_env);
        I_V_Des = findViewById(R.id.I_V_Des);
        ProgressBar = findViewById(R.id.progressBar);

        StorageRef = FirebaseStorage.getInstance().getReference("Data");
        DatabaseRef = FirebaseDatabase.getInstance().getReference("Data");

        Btn_C_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choisirImage();
            }
        });
        BtnConf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UploadTask != null && UploadTask.isInProgress()) {
                    Toast.makeText(NewDestination.this, "En cours de téléchargement", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile(UriImg);
                }
            }
        });
    }

    private void choisirImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            UriImg = data.getData();
            I_V_Des.setImageURI(UriImg);
        }
    }


    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(Uri uri) {
        if (UriImg != null) {
            StorageReference fileReference = StorageRef.child(System.currentTimeMillis() + "." + getFileExtension(uri));
            UploadTask = fileReference.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ProgressBar.setProgress(0);
                                }
                            }, 500);
                            Toast.makeText(NewDestination.this, "Téléchargement réussi ", Toast.LENGTH_SHORT).show();
                            //Model model = new Telechargement(textDescrip.getText().toString().trim(),textNom.getText().toString().trim(),
                             //       uri.toString());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(NewDestination.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            ProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(NewDestination.this, "Aucune image", Toast.LENGTH_SHORT).show();
        }
    }
}