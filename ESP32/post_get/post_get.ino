#include "WiFi.h"

const char* ssid = "Vodafone-Africano";
const char* password = "240311#Africano";
WiFiClient client;
char server[] = "rubenpassarinho.pt";
char path[] = "/ip.txt";

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
    String ipguardado = "";
    if (client.connect(server, 80)) {
        // Make a HTTP GET request:
        client.print("GET ");
        client.print(path);
        client.println(" HTTP/1.1");
        client.print("Host: ");
        client.println(server);
        client.println("Connection: close");
        client.println();

        while (client.available()) {
            String line = client.readStringUntil('\r');
            ipguardado += line; // Salva a resposta na variável ipguardado
        }

        // Agora que temos o IP, podemos fazer a solicitação POST
        String valor_a_enviar = "5"; // Substitua "algum valor" pelo valor real que deseja enviar
        if (client.connect(ipguardado.c_str(), 80)) {
            client.println("POST / HTTP/1.1");
            client.print("Host: ");
            client.println(ipguardado);
            client.println("Content-Type: application/x-www-form-urlencoded");
            client.print("Content-Length: ");
            client.println(valor_a_enviar.length());
            client.println();
            client.println(valor_a_enviar);
        } else {
            Serial.println("POST request failed");
        }
    } else {
        Serial.println("GET request failed");
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
