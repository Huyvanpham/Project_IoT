#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include "DHT.h"
#include <ArduinoJson.h>

//Wifi và mqtt
const char* ssid = "OPPO A9 2020";
const char* password = "44444444";
const char* mqtt_server = "192.168.71.104";

//Khai báo cảm biến
const int DHTPin = D5;
#define DHTTYPE DHT11 // DHT 11

DHT dht(DHTPin, DHTTYPE); // Khai báo biến cảm biến dht11

WiFiClient espClient;
PubSubClient client(espClient);
unsigned long lastMsg = 0;
#define MSG_BUFFER_SIZE  (50)
char msg[MSG_BUFFER_SIZE];
int value = 0;

//khai báo chân đèn led
int led1Pin = D1;
int led1Status = 1;

int led2Pin = D2;
int led2Status = 1;

int led3Pin = D0;
int led3Status = 1;

int led4Pin = D3;
int led4Status = 1;

int lightPin = A0;

//khai báo biến toàn cục nhiệt độ và độ ẩm
float temperature;
float humidity;
int light;
int doBui;

//Json parse
StaticJsonDocument<500> doc;

void setup_wifi() {
  dht.begin();
  delay(10);
  // bắt đầu kết nối tới wifi
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);
  Serial.print("1");

  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);

  //nếu không thể kết nối thì sẽ thử lại sau 500 mili giây
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  randomSeed(micros());

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}

void callback(char* topic, byte* message, unsigned int length) {
  Serial.print("Message arrived on topic: ");
  Serial.print(topic);
  Serial.print(". Message: ");
  String messageTemp;
  
  for (int i = 0; i < length; i++) {
    Serial.print((char)message[i]);
    messageTemp += (char)message[i];
  }
  Serial.println();

  // Feel free to add more if statements to control more GPIOs with MQTT

  // If a message is received on the topic esp32/output, you check if the message is either "on" or "off". 
  // Changes the output state according to the message
  if (String(topic) == "ledControlInput") 
  {
    controlLed(message);
  }
}

void controlLed(byte * message)
{
    // Deserialize the JSON document
    DeserializationError error = deserializeJson(doc, message);
    const char* led1 = doc["led1"];
    const char* led2 = doc["led2"];
    const char* led3 = doc["led3"];

    if(String(led1) == "on")
    {
      digitalWrite(led1Pin, HIGH);
      led1Status = 1;
    }
    else if(String(led1) == "off")
    {
      digitalWrite(led1Pin, LOW);
      led1Status = 0;
    }

    if(String(led2) == "on")
    {
      digitalWrite(led2Pin, HIGH);
      led2Status = 1;
    }
    else if(String(led2) == "off")
    {
      digitalWrite(led2Pin, LOW);  
      led2Status = 0;
    }

    if(String(led3) == "on")
    {
      digitalWrite(led3Pin, HIGH);
      led3Status = 1;
    }
    else if(String(led3) == "off")
    {
      digitalWrite(led3Pin, LOW);  
      led3Status = 0;
    }

    //Gửi luôn dữ liệu mới nhất này
    publishData();
}

void reconnect() {
  // Lắp đến khi kết nối thành công
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    //Tạo 1 id của client ngẫu nhiên
    String clientId = "ESP8266Client-";
    clientId += String(random(0xffff), HEX);
    // Sẵn sàng kết nối
    if (client.connect(clientId.c_str())) {
      Serial.println("connected");
      //sẽ subcrible đến topic này
      client.subscribe("ledControlInput");
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      // thử lại sau 5 giây
      delay(5000);
    }
  }
}

void setup() {
  pinMode(led1Pin, OUTPUT);
  pinMode(led2Pin, OUTPUT);
  pinMode(led3Pin, OUTPUT);
  pinMode(led4Pin, OUTPUT);
  pinMode(lightPin, INPUT);
  Serial.begin(115200);
  setup_wifi();
  client.setServer(mqtt_server, 1883);
  client.setCallback(callback);

  digitalWrite(led1Pin, HIGH);
  digitalWrite(led2Pin, HIGH);
  digitalWrite(led3Pin, HIGH);
}

void publishData()
{
  //đọc dữ liệu độ ẩm
  humidity = dht.readHumidity();
  //đọc dữ liệu nhiệt độ
  temperature = dht.readTemperature();
  
  light = analogRead(lightPin);
  light = map(light, 5, 1100, 1100, 5);
  doBui = random(0, 101);

  //Nếu dữ liệu của 1 trong 2 cái không hợp lệ thì sẽ không gửi
  if (isnan(humidity) || isnan(temperature)) {
    Serial.println("Failed to read from DHT sensor!");
  }
  else {
    char jsonResult[100];
    sprintf(jsonResult, "{ \"temperature\": %.2f, \"humidity\": %.2f, \"light\": %d, \"doBui\": %d }", temperature, humidity, light, doBui); 
    Serial.println(jsonResult);
    client.publish("dataSensorOutput", jsonResult);

    sprintf(jsonResult, "{ \"led1\": %d, \"led2\": %d, \"led3\": %d }", led1Status, led2Status, led3Status); 
    Serial.println(jsonResult);
    client.publish("ledStatus", jsonResult);
  }
}

void loop() {

  if (!client.connected()) {
    reconnect();
  }
  client.loop();
//  if (doBui > 20) {
//    while (doBui > 20) {
//        digitalWrite(led3Pin, HIGH);
//        digitalWrite(led2Pin, HIGH);
//        digitalWrite(led1Pin, HIGH);
//        delay(200);
//        digitalWrite(led3Pin, LOW);
//        digitalWrite(led2Pin, LOW);
//        digitalWrite(led1Pin, LOW);
//        delay(200);
//    }
//}


  publishData();

  delay(2000);
}
