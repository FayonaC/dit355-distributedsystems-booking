# Booking

## MQTT Prerequirements
* Install [Java JDK 8 or above](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html)
* Install [Maven 3.5 or above](https://maven.apache.org/download.cgi)
* Install MQTT Broker (e.g. Mosquitto) and run it locally on port 1883
   * For a helpful tutorial refer to [Steve's Internet Guide](http://www.steves-internet-guide.com/install-mosquitto-broker/)

## Installation Guide
1. Clone repository to your machine
2. Open a terminal window (e.g. Command Prompt) and move to the root folder of the repository. Enter command `mvn clean install` This will create a target folder.
3. To ensure that installation was successful, check target folder for booking.jar file.
4. Move to target folder and enter command `java -jar booking.jar`. This will enable the booking component to start listening to the MQTT Broker.
5. Open a new terminal window and move to target folder. Enter command `java -jar booking.jar`. This will enable to booking component to send messages.