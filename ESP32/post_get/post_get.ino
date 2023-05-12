#include "WiFi.h"

int status;
const char * ssid = "Visitors";
const char * password = "";
WiFiClient client;
char server[] = "10.72.66.195";

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
    IPAddress ip = WiFi.localIP();
    String request = "GET rubenpassarinho.pt/ip.txt";
    request += "IP=";
    request += ip;
    request += " HTTP/1.1";
    Serial.println(request);
    client.println(request);
    String host = "Host: ";
    host += server;
    client.println(host);
    client.println("Connection: close");
    client.println();
  }
  delay(1000);
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
