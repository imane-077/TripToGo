package com.openclassrooms.connexion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class resultat extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.openclassrooms.connexion.MESSAGE";
    public static final String EXTRA_MSGBudget = "com.openclassrooms.connexion.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultat);

        // recupérer la date de fin
        Intent intent = getIntent();
        String message = intent.getStringExtra(resultat.EXTRA_MESSAGE);
        TextView textView = findViewById(R.id.textView8);
        textView.setText(message);
        // budget
        String msgBudget = intent.getStringExtra(resultat.EXTRA_MSGBudget);
        TextView textView9 = findViewById(R.id.textView9);
        textView9.setText(msgBudget);

    }
}