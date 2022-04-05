package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    // declaring required variables
    private Socket client;
    private EditText textField;
    private Button buttonC;
    private Button buttonS;
    private Button buttonD;
    private String message;
    private ImageView Connected;
    private ImageView Disconnected;
    private TextView result;

    BufferedReader inFromServer;
    DataOutputStream outToServer;
    PrintWriter out ;
    String modifiedSentence;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // reference to the text field
        textField = (EditText) findViewById(R.id.EditText);

        // reference to the send button
        buttonC = (Button) findViewById(R.id.Button2);
        buttonS=(Button) findViewById(R.id.Button1);
        buttonD=(Button) findViewById(R.id.Button3);
        Connected=(ImageView) findViewById(R.id.ConnectedImage) ;
        Disconnected=(ImageView) findViewById(R.id.DisconnectedImage) ;
        result=(TextView)findViewById(R.id.textView);
        buttonC.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                new Thread(new ClientThread()).start();
                Connected.setVisibility(View.VISIBLE);
                Disconnected.setVisibility(View.INVISIBLE);
            }
        });
        buttonD.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                new Thread(new ClientDisconnect()).start();
                Connected.setVisibility(View.INVISIBLE);
                Disconnected.setVisibility(View.VISIBLE);
            }
        });
        buttonS.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                new Thread(new ClientSend(textField)).start();
                textField.setText("");
            }
        });
    }

    // the ClientThread class performs
    // the networking operations
    class ClientThread implements Runnable {



        ClientThread() {

        }
        @Override
        public void run() {
            try {
                client = new Socket("192.168.1.18", 12346);  // connect to server
                outToServer = new DataOutputStream(client.getOutputStream());
                inFromServer = new BufferedReader(new InputStreamReader(client.getInputStream()));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    class ClientDisconnect implements Runnable {



        ClientDisconnect() {

        }
        @Override
        public void run() {
            try {
                out = new PrintWriter(client.getOutputStream(),true);
                out.println("");
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }



        }
    }

    class ClientSend implements Runnable {
        EditText textField;


        ClientSend(EditText textField) {
            this.textField=textField;

        }
        @Override
        public void run() {

            try {
                out = new PrintWriter(client.getOutputStream(),true);
                message = textField.getText().toString();
                System.out.println(message);
                out.println(message);
                modifiedSentence = inFromServer.readLine();
                result.setText(modifiedSentence);
                textField.setText("");
            } catch (Exception e) {
                e.printStackTrace();
            }



        }
    }
}

