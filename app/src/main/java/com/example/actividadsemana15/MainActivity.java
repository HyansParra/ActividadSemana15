package com.example.actividadsemana15;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// Imports de la librería corregida
import info.mqtt.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private EditText etMessage;
    private Button btnPublish;
    private TextView tvStatus;

    private MqttAndroidClient client;
    private static final String SERVER_URI = "tcp://broker.hivemq.com:1883";
    private static final String TOPIC = "semana15/mqtt/test";
    private static final String TAG = "MQTT_CLIENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etMessage = findViewById(R.id.etMessage);
        btnPublish = findViewById(R.id.btnPublish);
        tvStatus = findViewById(R.id.tvStatus);

        connectToMqttBroker();

        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = etMessage.getText().toString();
                if (!message.isEmpty()) {
                    publishMessage(message);
                } else {
                    Toast.makeText(MainActivity.this, "Por favor escribe un mensaje", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void connectToMqttBroker() {
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), SERVER_URI, clientId);

        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);

            client.connect(options, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG, "Conexión Exitosa");
                    updateStatus("Conectado a HiveMQ");
                    subscribeToTopic();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e(TAG, "Fallo al conectar", exception);
                    updateStatus("Error de conexión");
                }
            });
        } catch (Exception e) { // <--- CAMBIADO A Exception
            e.printStackTrace();
        }
    }

    private void subscribeToTopic() {
        try {
            client.subscribe(TOPIC, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG, "Suscrito a: " + TOPIC);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e(TAG, "Error al suscribirse", exception);
                }
            });

            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    updateStatus("Conexión perdida");
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String payload = new String(message.getPayload());
                    Log.d(TAG, "Mensaje Recibido: " + payload);
                    updateStatus("Recibido: " + payload);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.d(TAG, "Entrega completada");
                }
            });

        } catch (Exception e) { // <--- CAMBIADO A Exception
            e.printStackTrace();
        }
    }

    private void publishMessage(String msg) {
        try {
            byte[] encodedPayload = msg.getBytes(StandardCharsets.UTF_8);
            MqttMessage message = new MqttMessage(encodedPayload);
            message.setQos(0);
            client.publish(TOPIC, message);
            Log.d(TAG, "Mensaje enviado: " + msg);
            etMessage.setText("");
        } catch (Exception e) { // <--- CAMBIADO A Exception (Esto arregla tu error rojo)
            e.printStackTrace();
            Toast.makeText(this, "Error al enviar", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateStatus(String text) {
        runOnUiThread(() -> tvStatus.setText(text));
    }
}