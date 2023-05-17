#include "WiFi.h"

const char * ssid = "Vodafone-Africano";
const char * password = "Africano240311!";
WiFiClient client;
const char * server = "rubenpassarinho.pt";
const char * resource = "/ip.txt";
String targetServerIP = ""; // Variable to store the IP

void setup() {
    Serial.begin(115200);
    WiFi.mode(WIFI_STA);
    WiFi.begin(ssid, password);
    if (WiFi.waitForConnectResult() != WL_CONNECTED) {
        Serial.println("WiFi Failed");
        while(1) {
            delay(1000);
        }
    }
    printWifiStatus();
}

void loop() {
    if (client.connect(server, 80)) {
        Serial.println("connected to server");
        // Make a HTTP request:
        String request = "GET " + String(resource) + " HTTP/1.1\r\nHost: " + String(server) + "\r\nConnection: close\r\n\r\n";
        client.print(request);
        delay(1000); // wait for server to respond
        // Read all the lines of the reply from server and print them to Serial
        while(client.available()){
            String line = client.readStringUntil('\r');
            targetServerIP = line; // Store the IP in the variable
            Serial.print(line);
        }
        client.stop();
        // Now, connect to the new server
        if (client.connect(targetServerIP.c_str(), 80)) {
            Serial.println("connected to target server");
            // Add your code here to interact with the target server
        } else {
            Serial.println("connection to target server failed");
        }
    } else {
        Serial.println("connection failed");
    }
    delay(10000); // execute once every 10 seconds, don't flood remote server
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
