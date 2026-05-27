package com.example.ex;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> tipos = new ArrayList<>();
    String r = "";
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        carregarPeixes();
        for(String t : tipos){
            r = r + ", " + t;
        }

        txt = findViewById(R.id.txt);
        txt.setText(r);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void carregarPeixes(){
        new Thread(() -> {
            try {
                String apiURL = "https://www.fisheries.noaa.gov/species-directory";
                URL url = new URL(apiURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                InputStream inputStream = conn.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder jsonBuilder = new StringBuilder();
                String line;

                while((line = reader.readLine()) != null){
                    jsonBuilder.append(line);
                }

                reader.close();
                JSONArray jsonArray = new JSONArray(jsonBuilder.toString());

                // EXTRAIR O ARQUIVO //
                JSONObject arrayPeixes = jsonArray.getJSONObject(0);

                String nomeObj = arrayPeixes.getString("speciesName");
                tipos.add(nomeObj);

                AlertDialog.Builder dialogo = new AlertDialog.Builder(MainActivity.this);
                dialogo.setTitle("MSG");
                dialogo.setMessage("CORRETO");
                dialogo.show();

            } catch (Exception e) {
                AlertDialog.Builder dialogo = new AlertDialog.Builder(MainActivity.this);
                dialogo.setTitle("MSG");
                dialogo.setMessage("ERRADO " + e.getMessage());
                dialogo.show();

            }
        });


    }
}