package com.example.avhaseisimba.simbaapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.example.avhaseisimba.simbaapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    //Declare Private variables to user withing the class
    private Button btnShowList,btnShowUser;
    private ProgressDialog progress;
    private JSONArray userArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Link class variables with xml items
        btnShowList = (Button) findViewById(R.id.btnShowList);
        btnShowUser = (Button) findViewById(R.id.btnShowUser);

//        Run New thread to create an API call using OkHTTP then store the results into userArray variable
        new Thread(new Runnable(){
            @Override
            public void run() {
                ApiClientClass apiClientClass = new ApiClientClass();
                String response = null;
                try {
                    response = apiClientClass.run(ApiClientClass.ApiUrl);
                    userArray = new JSONArray(response);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

//        Show Samanth email address when pressed
        btnShowUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailAdd = "";

                if(userArray == null){
                    Toast.makeText(MainActivity.this, "Application Not Connected", Toast.LENGTH_SHORT).show();
                    return;
                }
                for(int i = 0; i<userArray.length() ; i++){
                    try {
                        String userName = "";
                        userName = userArray.getJSONObject(i).getString("username");
                        if(userName.equals("Samantha")){
                            emailAdd = userArray.getJSONObject(i).getString("email");
                        }else {
                            Log.d("State","Not Found");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Show Samantha's email");
                builder.setMessage(emailAdd);
                builder.setCancelable(true);
                builder.setNegativeButton(
                        "Close",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

//        Show list of all users when pressed
        btnShowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final List<String> users = new ArrayList<>();

                if(userArray == null){
                    Toast.makeText(MainActivity.this, "Application Not Connected", Toast.LENGTH_SHORT).show();
                    return;
                }
                for(int i = 0; i<userArray.length() ; i++){
                    try {
                        users.add(userArray.getJSONObject(i).getString("name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                final CharSequence[] charSequenceItems = users.toArray(new CharSequence[users.size()]);
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("List all users");
                builder.setCancelable(true);
                builder.setItems(charSequenceItems, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}
