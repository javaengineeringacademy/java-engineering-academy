package academy.javaengineering.kafka;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class KafkaStreamsExample {

    private final Map<String, List<Map<String, Object>>> inputStreams = new ConcurrentHashMap<>();
    private final Map<String, List<Map<String, Object>>> outputStreams = new ConcurrentHashMap<>();

    public void inputStream(String topic, Map<String, Object> record) {
        inputStreams.computeIfAbsent(topic, k -> new ArrayList<>()).add(record);
    }

    public <K, V> void processStream(String inputTopic, String outputTopic,
                                      Function<Map<String, Object>, Map<String, Object>> processor) {
        List<Map<String, Object>> input = inputStreams.getOrDefault(inputTopic, Collections.emptyList());
        List<Map<String, Object>> output = outputStreams.computeIfAbsent(outputTopic, k -> new ArrayList<>());

        for (Map<String, Object> record : input) {
            Map<String, Object> processed = processor.apply(record);
            output.add(processed);
        }

        System.out.println("Processed " + input.size() + " records from " + inputTopic + " to " + outputTopic);
    }

    public void mapValues(String inputTopic, String outputTopic,
                          Function<Object, Object> mapper) {
        processStream(inputTopic, outputTopic, record -> {
            Map<String, Object> newRecord = new HashMap<>(record);
            newRecord.put("value", mapper.apply(record.get("value")));
            return newRecord;
        });
    }

    public void filter(String inputTopic, String outputTopic,
                       java.util.function.Predicate<Map<String, Object>> predicate) {
        List<Map<String, Object>> input = inputStreams.getOrDefault(inputTopic, Collections.emptyList());
        List<Map<String, Object>> output = outputStreams.computeIfAbsent(outputTopic, k -> new ArrayList<>());

        for (Map<String, Object> record : input) {
            if (predicate.test(record)) {
                output.add(record);
            }
        }
    }

    public Map<String, List<Map<String, Object>>> getOutputStreams() {
        return new HashMap<>(outputStreams);
    }

    public static void main(String[] args) {
        KafkaStreamsExample streams = new KafkaStreamsExample();

        System.out.println("=== Kafka Streams Demo ===\n");

        streams.inputStream("orders", Map.of("key", "order-1", "value", "created"));
        streams.inputStream("orders", Map.of("key", "order-2", "value", "updated"));
        streams.inputStream("orders", Map.of("key", "order-3", "value", "created"));

        System.out.println("--- Map Values ---");
        streams.mapValues("orders", "orders-upper",
                value -> value.toString().toUpperCase());

        System.out.println("\n--- Filter ---");
        streams.filter("orders", "created-orders",
                record -> "created".equals(record.get("value")));

        System.out.println("\n--- Output Streams ---");
        streams.getOutputStreams().forEach((topic, records) -> {
            System.out.println(topic + ": " + records.size() + " records");
        });
    }
}
