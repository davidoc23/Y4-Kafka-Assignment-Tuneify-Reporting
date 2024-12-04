package atu.distributed.systems;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.LongSerializer;

import java.util.Properties;

public class TuneifyKafkaProducer {

    private static final String BOOTSTRAP_SERVERS = "localhost:9092"; // Kafka broker address
    private static final String TOPIC_NAME = "plays";  // Topic to send play events to

    private Producer<Long, PlayEvent> producer;

    public TuneifyKafkaProducer() {
        // Set up the Kafka producer configuration
        Properties properties = new Properties();
        properties.put("bootstrap.servers", BOOTSTRAP_SERVERS);  // Kafka brokers
        properties.put("key.serializer", LongSerializer.class.getName());  // Key serializer (userId)
        properties.put("value.serializer", PlayEvent.PlayEventSerializer.class.getName());  // Value serializer (PlayEvent)
        properties.put("acks", "all");  // Ensure all replicas acknowledge the message
        properties.put("retries", 0);  // No retries (default)
        properties.put("linger.ms", 1);  // Produce messages as quickly as possible

        // Initialize the producer with the properties
        producer = new KafkaProducer<>(properties);
    }

    // Method to publish a play event to Kafka
    public void publishEvent(long userId, PlayEvent event) {
        // Create a ProducerRecord with the key (userId), value (PlayEvent), and topic
        ProducerRecord<Long, PlayEvent> record = new ProducerRecord<>(TOPIC_NAME, userId, event);

        // Send the record asynchronously to Kafka
        producer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (exception != null) {
                    exception.printStackTrace();
                } else {
                    // Log metadata for the event (e.g., topic, partition, offset)
                    System.out.println("Published event to Kafka: " +
                            "Topic=" + metadata.topic() +
                            ", Partition=" + metadata.partition() +
                            ", Offset=" + metadata.offset());
                }
            }
        });
    }

    // Close the producer
    public void close() {
        if (producer != null) {
            producer.close();
        }
    }
}
