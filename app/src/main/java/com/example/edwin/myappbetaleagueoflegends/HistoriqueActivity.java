package com.example.edwin.myappbetaleagueoflegends;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class HistoriqueActivity extends AppCompatActivity {

    private String playerName;
    private Long playerId;
    private TextView textViewM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        textViewM = (TextView) findViewById(R.id.textMostrar);

        if(extras.getString("NAME") != null && extras.getLong("ID") > 0){

            playerName = extras.getString("NAME");
            playerId = extras.getLong("ID");
            textViewM.setText(playerName+"/"+ String.valueOf(playerId));
        }
    }
}
