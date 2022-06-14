package com.example.psi_univ.backend;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressLint("StaticFieldLeak")
public class Background extends AsyncTask<String, Void, Void> {

    Context context;
    public Background(Context context) {
        this.context = context;
    }


    @Override
    protected Void doInBackground(String... voids) {
        if(voids.length == 2) {
            getData(voids[0]);
            getData(voids[1]);
        }
        else if (voids.length == 1) {
            getData(voids[0]);
        }
        Log.i("Fini", "Fini");
        return null;
    }

    // Fetch all event data from the server
    // ~1min30 to fetch all events
    public Void getData(String table) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);

        String connection = "http://ifpop.fr/fetchDatabase.php";

        try{
            URL url = new URL(connection);

            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoInput(true);
            http.setDoOutput(true);


            OutputStream ops = http.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, StandardCharsets.UTF_8));
            String data = URLEncoder.encode("table","UTF-8")+"="+URLEncoder.encode(table,"UTF-8");
            writer.write(data);
            writer.flush();
            writer.close();
            ops.close();


            InputStream ips = http.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(ips, StandardCharsets.ISO_8859_1));
            String line;
            List<String[]> separated = new ArrayList<>();
            if(Objects.equals(table, "rooms")) {

                while ((line = reader.readLine()) != null) {
                    separated.add(line.split("!"));
                }
                dataBaseHelper.addOneBuilding(separated);
            }
            else if(Objects.equals(table, "events")) {

                while ((line = reader.readLine()) != null) {
                    separated.add(line.split("!"));
                }
                dataBaseHelper.addOneEvent(separated);
            }
            reader.close();
            ips.close();
            http.disconnect();
            return null;


        } catch (MalformedURLException e) {
            e.getMessage();
        } catch (IOException e) {
            e.getMessage();
        }
        return null;
    }
}

