import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class TuneifyReportingService {

    public static void main(String[] args) {
        // Kafka Consumer configuration
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "reporting-group");
        props.put("key.deserializer", LongDeserializer.class.getName());
        props.put("value.deserializer", PlayEvent.PlayEventDeserializer.class.getName());
        props.put("auto.offset.reset", "earliest");

        KafkaConsumer<Long, PlayEvent> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("plays"));  // Subscribe to the "plays" topic

        try {
            while (true) {
                ConsumerRecords<Long, PlayEvent> records = consumer.poll(Duration.ofMillis(100));

                for (ConsumerRecord<Long, PlayEvent> record : records) {
                    PlayEvent playEvent = record.value();

                    if (playEvent != null) {  // Check if deserialization was successful
                        System.out.printf(
                                "Received PlayEvent: UserID=%d, Artist=%s, Track=%s, Album=%s, Partition=%d, Offset=%d%n",
                                playEvent.getUserId(), playEvent.getArtistName(),
                                playEvent.getTrackName(), playEvent.getAlbumName(),
                                record.partition(), record.offset()
                        );
                    } else {
                        System.err.println("Received null PlayEvent. Skipping this record.");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
        }
    }
}
