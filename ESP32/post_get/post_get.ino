#include "WiFi.h"
#include <ArduinoJson.h>
#include <NewPing.h>  // Inclua a biblioteca NewPing

#define TRIGGER_PIN  5  // Pino GPIO 5 do ESP32 conectado ao pino SIG do sensor
#define MAX_DISTANCE 200 // Defina a distância máxima que desejamos medir, suponha 200cm

NewPing sonar(TRIGGER_PIN, TRIGGER_PIN, MAX_DISTANCE); // Crie um objeto NewPing.

int status;
const char *ssid = "Vodafone-Africano";
const char *password = "240311#Africano";
WiFiClient client;
char server[] = "rubenpassarinho.pt";
String newIP = "";
int port = 6969;

void setup() {
    Serial.begin(115200);
    WiFi.mode(WIFI_STA);
    WiFi.begin(ssid, password);
    if (WiFi.waitForConnectResult() != WL_CONNECTED) {
        Serial.println("WiFi Failed");
        while (1) {
            delay(1000);
        }
    }
    printWifiStatus();
    randomSeed(analogRead(0));
}

void loop() {
    if (client.connect(server, 80)) {
        Serial.println("connected to server");
        // Make a HTTP request:
        IPAddress ip = WiFi.localIP();
        String request = "GET /ip.txt?";
        request += "IP=";
        request += ip;
        request += " HTTP/1.1";
        client.println(request);
        client.println("Host: " + String(server));
        client.println("Connection: close");
        client.println();

        // Wait until client is available
        while (!client.available()) {
            delay(10);
        }

        // Read the response
        String response;
        while (client.available()) {
            response = client.readStringUntil('\n');
            response.trim(); // trim off trailing whitespace
        }
        client.stop(); // close the connection

        // Parse the JSON response to extract the IP address
        DynamicJsonDocument doc(256);
        deserializeJson(doc, response);
        const char* ipAddr = doc["ip"];
        if (ipAddr != NULL) {
            newIP = ipAddr;
            // Successfully extracted the IP address from JSON
            Serial.println("Received IP: " + newIP);
            // Connect to the new IP on port 6969
            if (client.connect(newIP.c_str(), port)) {
                Serial.println("Successfully connected to: " + newIP);
                sendPostRequest();
            } else {
                Serial.println("Failed to connect to: " + newIP);
            }
        } else {
            Serial.println("Failed to extract IP address from JSON: " + response);
        }
    } else {
        Serial.println("connection failed");
    }
     // Wait for 30 seconds before next connection attempt
}

void sendPostRequest() {
    //unsigned int uS = sonar.ping(); // Enviar ping e obter tempo em microssegundos
    //unsigned int distanceCm = uS / US_ROUNDTRIP_CM; // Converte para distância em cm
    //String postData = String(20); // Converte a distância para string
    int distanceCm = random(1, 201);
    String postData = String(distanceCm); // Converte a distância para string
    Serial.println(distanceCm);
    client.print("POST /?value=" + postData + " HTTP/1.1\r\n");
    client.print("Host: " + String(newIP) + "\r\n");
    client.print("Connection: close\r\n");
    client.print("Content-Type: application/x-www-form-urlencoded\r\n");
    client.print("Content-Length: 0\r\n");
    client.print("\r\n");

    Serial.println("POST request sent");
}

void printWifiStatus() {
    // print the SSID of the network you're attached to:
    Serial.print("SSID: ");
    Serial.println(WiFi.SSID());

    // print your WiFi shield's IP address:
    IPAddress ip = WiFi.localIP();
    Serial.print("IP Address: ");
    Serial.println(ip);

    // print the received signal strength:
    long rssi = WiFi.RSSI();
    Serial.print("signal strength (RSSI):");
    Serial.print(rssi);
    Serial.println(" dBm");
}
