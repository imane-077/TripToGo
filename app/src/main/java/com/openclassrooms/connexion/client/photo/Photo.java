package com.openclassrooms.connexion.client.photo;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.openclassrooms.connexion.Accueil;
import com.openclassrooms.connexion.Affichage_images;
import com.openclassrooms.connexion.R;
import com.openclassrooms.connexion.Telechargement;
import com.openclassrooms.connexion.navigation;

import static android.app.Activity.RESULT_OK;

public class Photo extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1; // pour reconnaitre quel intent à été lancé

    Button BtnChoisirImage;
    Button Btntelecharger;
    TextView TextAccesPhoto;
    ImageView mImageView;
    ProgressBar mProgressBar;
    EditText textDescrip;
    EditText textNom;

    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_photo, container, false);

        // lien avec les objets graphique

        BtnChoisirImage = root.findViewById(R.id.btnChoisirImg);
        Btntelecharger = root.findViewById(R.id.btnTelecharger);
        TextAccesPhoto = root.findViewById(R.id.textAccesPhoto);
        mImageView = root.findViewById(R.id.img);
        mProgressBar = root.findViewById(R.id.progressBar);
        textDescrip = root.findViewById(R.id.editTextDescription);
        textNom = root.findViewById(R.id.editTextNamee);

        mStorageRef = FirebaseStorage.getInstance().getReference("Photo"); // mettre dans le dossier photo
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Photo");


        BtnChoisirImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choisirImage();
            }
        });
        Btntelecharger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(getActivity(), "En cours de téléchargement", Toast.LENGTH_SHORT).show();
                } else {
                    teleFichier(mImageUri);
                }
            }
        });
        TextAccesPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Affichage_images.class);
                ((navigation) getActivity()).startActivity(intent);
            }
        });
        return root;
    }

    // je lance l'intention d'accéder à la galerie
    private void choisirImage() {
        // accès à la galerie tel
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //démarre le intent : for result car on attend un résultat du lancement de l'activité
        // lance le intent et on récupère dans pick_image... et si il retourne 1 on sait que c'est cet intent car on peut en lancer plusieurs
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }

    // But : sélection de l'image après appel de startActivity...
    // sélectionner la photo à partir de la galerie
    // requestCode = 1 ==> correspond à pick...
    // resultCode ==> pour le resultat
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // vérifie si une image est récupéré
        // resultCode == RESULT_OK ==> permet de savoir si j'ai bien sélectionner l'image
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            // accès à l'image à partir de data
            // URI c'est comme URL ==> permet de savoir où se trouve l'image
            mImageUri = data.getData();
            // met l'image dans image view
            mImageView.setImageURI(mImageUri);
            mImageView.setVisibility(View.VISIBLE);
        }
    }

    //extension de l'image
    // uri est le fichier uri du résultat du fichier sélectionné par le bouton
    private String getFileExtension(Uri uri) {
        // getContentResolver() ==> obtenir le type de données d'un fichier
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        // renvoie l'extension de uri
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    // télécharger l'image
    private void teleFichier(Uri uri) {

        if (mImageUri != null) { // si on a sélectionné une image
            // ajouter à la base avec les variables définit en haut
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(uri));
            mUploadTask = fileReference.putFile(uri)

                    // si succès
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override

                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            mProgressBar.setProgress(0);
                                        }
                                    }, 500);
                                    Toast.makeText(getActivity(), "Téléchargement réussi !", Toast.LENGTH_LONG).show();
                                    Telechargement tele = new Telechargement(textDescrip.getText().toString().trim(),textNom.getText().toString().trim(),
                                            uri.toString());

                                    String teleId = mDatabaseRef.push().getKey();
                                    mDatabaseRef.child(teleId).setValue(tele);

                                    textDescrip.getText().clear() ;
                                    textNom.getText().clear();
                                    mImageView.setVisibility(View.INVISIBLE);

                                }
                            });

                        }
                    })

                    // Appelé lorsque la tâche a échoué avec une exception
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })

                    // en cours de téléchargement
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            // si aucune image a été sélectionné
            Toast.makeText(getActivity(), "Aucune image ", Toast.LENGTH_SHORT).show();
        }
    }
}