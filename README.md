**Tuneify Assignment**

**Overview**

The Tuneify Project is a music event tracking and reporting system built using Kafka for event streaming and processing. The project includes multiple components: a Producer that sends music play events to a Kafka topic, a Consumer service that processes these events, and a simple API that receives and responds to HTTP POST requests. This system is designed to handle real-time play event data and display the reports efficiently.

**Components**
1. Kafka Producer
  The Kafka Producer sends music play events to the Kafka topic plays. Each event contains details of a music play, including:
  • User ID
  •	Artist Name
  •	Track Name
  •	Album Name
  The producer sends these events as JSON objects to the Kafka topic for consumption by the Kafka Consumer.

2. Kafka Consumer (Tuneify Reporting Service)
  The Kafka Consumer (Tuneify Reporting Service) listens to the Kafka topic plays and processes incoming events. Upon receiving a play event, it deserializes the event and prints the event details to the console.
  •	The consumer reads from the Kafka topic and uses a PlayEventDeserializer to convert the event data into a Java object.

3. Tuneify API
The Tuneify API provides a simple HTTP server running on port 8000. The API exposes the /plays endpoint, which listens for HTTP POST requests containing music play event data. The server processes these requests and stores the events. It can be used to manually send events from external clients or simulate events.

**Project Setup**
Prerequisites
•	Kafka: Kafka is used for event streaming. Ensure that Kafka is properly set up and running.
•	Java 11+: The project is written in Java, so you need to have JDK 11 or higher installed.

**Setting Up Kafka**
1.	Download and install Kafka.

2.	Start the Zookeeper server:
      bin/zookeeper-server-start.sh config/zookeeper.properties

3.	Start the 3 Kafka brokers:
    bin/kafka-server-start.sh config/server.properties
      bin/kafka-server-start.sh config/server-1.properties
      bin/kafka-server-start.sh config/server-2.properties

**Starting the Consumer**
1.	Compile and run the Tuneify Reporting Service (Kafka consumer).

2.	The consumer will automatically start processing events from the plays Kafka topic.

**Starting the Producer**
1.	Compile and run the Tuneify Client (Kafka producer).
2.	It will send events to the plays topic, which will be consumed by the Tuneify Reporting Service.

**Running the API**
1.	Compile and run the Tuneify API.
2.	It will start an HTTP server listening on port 8000 for incoming POST requests to /plays.

**Usage**
**Sending Play Events**

You can send play events to the API by using the Tuneify Client (tuneify-client.jar). It will send POST requests with play event data to the /plays endpoint.
Here’s an example of a play event: 950303,AronChupa,Little Swing,Little Swing

**Consuming Play Events**
The Tuneify Reporting Service will receive the play events from the plays topic and print the details of each event:
Received PlayEvent: UserID=950303, Artist=AronChupa, Track=Little Swing, Album=Little Swing

**Error Handling**
  •	If the consumer receives invalid data or cannot deserialize the event, a NullPointerException or a JsonParseException will be logged.
  •	Timeouts during HTTP requests are handled using HttpTimeoutException.

**Running the Project**
1.	Start Kafka and Zookeeper services.
2.	Start the Tuneify Reporting Service to consume the events.
3.	Run the Tuneify API to accept POST requests.
4.	Run the Tuneify Client from the projects root folder to simulate the sending of events.    ( java -jar tuneify-client.jar )

**Dependencies**
•	Kafka Clients: Kafka client libraries are required to interact with Kafka brokers. These libraries provide the necessary classes for producing and consuming messages.
•	Java Development Kit (JDK): The project is built using Java. Ensure you have JDK 11 or higher installed. 
    Installation: Follow Oracle's JDK installation guide or use OpenJDK from your system's package manager.
•	Serialization/Deserialization Libraries: Kafka messages need to be serialized before being sent to Kafka topics and deserialized when consumed.

**Future Improvements**
•	Add more robust error handling for HTTP requests and Kafka processing.
•	Scale the consumer to handle larger volumes of events in parallel.
•	Implement more complex event processing logic, such as tracking the most-played tracks or generating analytics.
•	Add authentication to the API to secure the /plays endpoint.

