package ro.pub.cs.systems.pdsd.practicaltest02;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PracticalTest02MainActivity extends AppCompatActivity {
    private ServerThread serverThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);
    }
    public void onServerStartClick(View view) {
        String serverPort = ((EditText) findViewById(R.id.serverPortText)).getText().toString();
        Toast.makeText(
                getApplicationContext(),
                serverPort,
                Toast.LENGTH_SHORT
        ).show();
        if (serverPort == null || serverPort.isEmpty()) {
            Toast.makeText(
                    getApplicationContext(),
                    "[MAIN ACTIVITY] Server port should be filled!",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }
        serverThread = new ServerThread(Integer.parseInt(serverPort));
        if (serverThread.getServerSocket() == null) {
            Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
            return;
        }
        serverThread.start();
    }

    public void onSendRequestClick(View view) {
        String clientAddress = ((EditText) findViewById(R.id.clientAddressText)).getText().toString();
        String clientPort = ((EditText) findViewById(R.id.clientPortText)).getText().toString();
        if (clientAddress == null || clientAddress.isEmpty() || clientPort == null || clientPort.isEmpty()) {
            Toast.makeText(
                    getApplicationContext(),
                    "[MAIN ACTIVITY] Client connection parameters should be filled!",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }
        if (serverThread == null || !serverThread.isAlive()) {
            Toast.makeText(
                    getApplicationContext(),
                    "[MAIN ACTIVITY] There is no server to connect to!",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        String key = ((EditText) findViewById(R.id.keyText)).getText().toString();
        if (key == null || key.isEmpty()) {
            Toast.makeText(
                    getApplicationContext(),
                    "[MAIN ACTIVITY] Parameters from client (key / value) should be filled",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }
        TextView respTextView = (TextView) findViewById(R.id.responseTextView);
        respTextView.setText("");
        ClientThread clientThread = new ClientThread(
                clientAddress, Integer.parseInt(clientPort), key,respTextView
        );
        clientThread.start();
    }
    @Override
    protected void onDestroy() {
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}