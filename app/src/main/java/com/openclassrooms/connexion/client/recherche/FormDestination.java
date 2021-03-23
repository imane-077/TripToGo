package com.openclassrooms.connexion.client.recherche;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.openclassrooms.connexion.Accueil;
import com.openclassrooms.connexion.R;
import com.openclassrooms.connexion.navigation;
import com.openclassrooms.connexion.resultat;

import java.util.Calendar;
import java.util.Currency;
import java.util.Locale;

public class FormDestination extends Fragment {

    DatePickerDialog picker;
    TextView TextViewDebut;
    TextView TextViewFin;
    TextView affichage;
    EditText textDateDebut;
    EditText textDateFin;
    EditText textBudget;
    RadioButton envMer, envVille, envForet, envDesert, envMont;
    RadioGroup radio_group1;
    public static final String EXTRA_MESSAGE = "com.openclassrooms.connexion.MESSAGE";
    public static final String EXTRA_MSGBudget = "com.openclassrooms.connexion.MESSAGE";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_recherche, container, false);
        TextViewDebut = root.findViewById(R.id.textViewDebut);
        TextViewFin = root.findViewById(R.id.textViewFin);
        textDateDebut = root.findViewById(R.id.editTextDebut);
        textDateFin = root.findViewById(R.id.editTextFin);
        textBudget = root.findViewById(R.id.editTextBudget);
        affichage=root.findViewById(R.id.affichage);
        envMer = root.findViewById(R.id.envMer);
        envVille = root.findViewById(R.id.envVille);
        envForet = root.findViewById(R.id.envForet);
        envDesert = root.findViewById(R.id.envDesert);
        envMont = root.findViewById(R.id.envMont);
        radio_group1 = root.findViewById(R.id.radio_group1);



        textBudget.setTransformationMethod(null); // pour empêcher l'utilisateur de mettre des lettres... pour le budget et le clavier affiche que des chiffres

        textDateDebut.setInputType(InputType.TYPE_NULL);
        textDateDebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                textDateDebut.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                TextViewDebut.setText("Selected Date: "+ textDateDebut.getText()); // récupère la date et l'affiche la date dans le textView
                            }
                        }, year, month, day);
                picker.show();

            }


        });

        textDateFin .setInputType(InputType.TYPE_NULL);
        textDateFin .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                textDateFin .setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                TextViewFin.setText("Selected Date: "+ textDateFin .getText()); // récupère la date et l'affiche la date dans le textView
                            }
                        }, year, month, day);
                picker.show();

            }

        });

        final Button button = root.findViewById(R.id.deconnecter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // passer d'un fragment à une activity
                Intent intent = new Intent(getActivity(), Accueil.class); // quand on est dans un fragment on doit mettre getActivity()
                ((navigation) getActivity()).startActivity(intent); // navigation : point d'entrée des fragments
            }
        });

        final Button buttonConf = root.findViewById(R.id.button_confirm_next);
        buttonConf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // passer d'un fragment à une activity
                if (error()){

                    //envoie info activity resultat

                    Intent intent = new Intent(getActivity(), resultat.class);
                    String message = textDateFin.getText().toString();
                    intent.putExtra(EXTRA_MESSAGE, message);

                    String msgBudget = textBudget.getText().toString();
                    intent.putExtra(EXTRA_MSGBudget, msgBudget);

                    ((navigation) getActivity()).startActivity(intent); // navigation : point d'entrée des fragments
                }
            }

            private boolean error() {
                if (textDateDebut.getText().toString().length()==0){
                    Toast.makeText(getActivity(), "Sélectionnez une date de début", Toast.LENGTH_LONG).show();
                    return false;
                }
                if (textDateFin.getText().toString().length()==0){
                    Toast.makeText(getActivity(), "Sélectionnez une date de fin", Toast.LENGTH_LONG).show();
                    return false;
                }

                else if (!envMer.isChecked()&& !envVille.isChecked() && !envForet.isChecked() && !envDesert.isChecked() && !envMont.isChecked()){
                    Toast.makeText(getActivity(), "Veuillez sélectionner un environnement", Toast.LENGTH_LONG).show();
                    return false;
                }
                else if (textBudget.getText().toString().length()==0){
                    Toast.makeText(getActivity(), "Veuillez renter un budget", Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            }

        });
        return root;

    }

}
