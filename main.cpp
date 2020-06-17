#include <Arduino.h>
#include "ArduinoJson.h"
#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <SoftwareSerial.h>
#include <Servo.h>
//#include <ESP8266Ping.h>



//VARIABLES
const int numReadings = 5;

int readings[numReadings];      // the readings from the analog input
int readIndex = 0;              // the index of the current reading
int total = 0;                  // the running total
int average = 0;                // the average
int val;
int inputPin = A0;
int outputPin= D4;
int base;
int aux=0;
int idSensor=0;
 int timeStamp=0;
Servo myservo;


//VARIABLES

char responseBuffer[300];
WiFiClient client;

String SSID = "ONOFA66";
String PASS = "QeVstTXAHhNw";

String SERVER_IP = "192.168.1.8";
int SERVER_PORT = 8081;

void sendGetRequest(String);
void sendPostRequest(int,long int,int,int);
void bucleSensor();


void setup() {
  Serial.begin(9600);

  WiFi.begin(SSID, PASS);

  Serial.print("Connecting...");
  while (WiFi.status() != WL_CONNECTED){
    delay(500);
    Serial.print(".");
  }
  Serial.print("Connected, IP address: ");
  Serial.print(WiFi.localIP());

  //SETUP Sensor
Serial.println("1");
  myservo.attach(2);
  Serial.println("3");
  for (int thisReading = 0; thisReading < numReadings; thisReading++) {
    readings[thisReading] = 0;
  }
Serial.println("2");


  delay(5000);
  for(int i=0;i<numReadings;i++){
  total = total - readings[readIndex];
  // read from the sensor:
  readings[readIndex] = analogRead(inputPin);
  // add the reading to the total:
  total = total + readings[readIndex];
  // advance to the next position in the array:
  readIndex = readIndex + 1;
  delay(1000);
  }
  // if we're at the end of the array...
  if (readIndex >= numReadings) {
    // ...wrap around to the beginning:
    readIndex = 0;
  }

    base=total / numReadings;
}

void loop() {

  sendGetRequest("/api/sensor/values/7");
  delay(100000);
/*  if(Ping.ping("192.168.1.8")) {
    Serial.println("Success!!");
  } else {
    Serial.println("Error :(");
  }*/

/*sendPostRequest();
Serial.println("acaba");
while(1){
    delay(10000000);
}
*/
//bucleSensor();
}

void sendGetRequest(String url){
  if (WiFi.status() == WL_CONNECTED){
    Serial.println("1");
    HTTPClient http;
    http.begin(client, SERVER_IP, SERVER_PORT, url, true);
    Serial.println("2");
    int httpCode = http.GET();
    Serial.println("3");
    Serial.println("Response code: " + httpCode);
    Serial.setTimeout(10000);

    String payload = http.getString();
    Serial.println(payload);
    const size_t capacity = JSON_OBJECT_SIZE(4) + JSON_ARRAY_SIZE(2) + 60;
    DynamicJsonDocument doc(capacity);

   DeserializationError error = deserializeJson(doc, payload);
    if (error){
      Serial.print("deserializeJson() failed: ");
      Serial.println(error.c_str());
      return;
    }

    Serial.println(F("Response:"));
    int sensor = doc[0]["idSensor"].as<int>();
    int time = doc[0]["timeStamp"].as<int>();
    int Base = doc[0]["base"].as<int>();
    int valor = doc[0]["valor"].as<int>();

    Serial.println("Sensor name: " + String(sensor));

    Serial.println("Time: " + String(time));
    Serial.println("Data: " + String(Base));
    Serial.println("Valor: " + String(valor));

    val = map(valor, Base-30, Base+50, 105, 10);     // scale it to use it with the servo (value between 0 and 180)
    myservo.write(val);

  }
}


  void sendPostRequest(int idSensor,int TimeStamp,int base,int valor){
  if (WiFi.status() == WL_CONNECTED){
    HTTPClient http;
    http.begin(client, SERVER_IP, SERVER_PORT, "/api/sensor/values", true);
    http.addHeader("Content-Type", "application/json");

    const size_t capacity = JSON_OBJECT_SIZE(3) + JSON_ARRAY_SIZE(2) + 60;
    DynamicJsonDocument doc(capacity);
    doc["idSensor"] = idSensor;
    doc["timeStamp"] = TimeStamp;
    doc["base"] = base;
    doc["valor"] = valor;

    String output;
    serializeJson(doc, output);
    int httpCode = http.PUT(output);

    Serial.println("Response code: " + httpCode);

    String payload = http.getString();

    Serial.println("Resultado: " + payload);
  }else{
    Serial.println("No furula");
  }
}
void bucleSensor(){
  timeStamp=timeStamp+1;
  aux++;
 total = total - readings[readIndex];
 // read from the sensor:
 readings[readIndex] = analogRead(inputPin);
 // add the reading to the total:
 total = total + readings[readIndex];
 // advance to the next position in the array:
 readIndex = readIndex + 1;

 // if we're at the end of the array...
 if (readIndex >= numReadings) {
   // ...wrap around to the beginning:


   readIndex = 0;

 }

 // calculate the average:
 average = total / numReadings;
 val=average;
 val = map(val, base-30, base+50, 105, 10);     // scale it to use it with the servo (value between 0 and 180)
 myservo.write(val);
 Serial.println(average);
 if(aux==100){
   sendPostRequest(idSensor++,timeStamp,base,val);
   Serial.println("Se acaba de enviar un post");
   aux=0;
 }
 delay(50);
}
