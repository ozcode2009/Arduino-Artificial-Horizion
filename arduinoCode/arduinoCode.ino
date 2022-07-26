#include "SparkFunLSM6DSO.h"
#include "Wire.h"
LSM6DSO myIMU;
String msg;
void setup() {
  Serial.begin(115200);
  delay(500);

  Wire.begin();
  delay(10);
  if ( myIMU.begin() )
    Serial.println("Ready.");
  else {
    Serial.println("Could not connect to IMU.");
    Serial.println("Freezing");
  }

  if ( myIMU.initialize(BASIC_SETTINGS) )
    Serial.println("Loaded Settings.");


}


void loop()
{
  float accelX = myIMU.readFloatAccelX();
  float accelY = myIMU.readFloatAccelY();
  float accelZ = myIMU.readFloatAccelZ();


  float roll = atan2(accelY, accelZ);
  float pitch = atan2(-accelX, sqrt(accelY * accelY + accelZ * accelZ));
  int rollDegrees = roll * 60;
  int pitchDegrees = pitch * 60;
  Serial.println(String(pitchDegrees) + "," + String(rollDegrees));
}