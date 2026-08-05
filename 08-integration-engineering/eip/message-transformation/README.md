# Message Transformation Patterns

## Overview

Message Transformation patterns convert messages from one format to another, ensuring that producers and consumers can communicate effectively even when they use different data formats, protocols, or representations. These patterns are essential for integrating heterogeneous systems.

---

## Table of Contents

1. [Message Translator](#1-message-translator)
2. [Content Enricher](#2-content-enricher)
3. [Content Filter](#3-content-filter)
4. [Content Modifier](#4-content-modifier)
5. [Claim Check](#5-claim-check)
6. [Normalizer](#6-normalizer)
7. [Scatter-Gather](#7-scatter-gather)

---

## 1. Message Translator

### Problem

Two systems use different data formats or protocols. A message from one system cannot be directly consumed by the other system without format conversion.

### Solution

Implement a Message Translator that converts messages from one format to another. The translator acts as a bridge between incompatible systems by transforming message payloads while preserving semantic meaning.

### Implementation

```java
@Component
@Slf4j
public class MessageTranslator {

    @Bean
    public IntegrationFlow xmlToJsonTranslator() {
        return IntegrationFlow.from("xmlInputChannel")
            .transform(String.class, xmlPayload -> {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    XmlMapper xmlMapper = new XmlMapper();

                    // Parse XML
                    JsonNode xmlNode = xmlMapper.readTree(xmlPayload);

                    // Convert to JSON
                    return objectMapper.writeValueAsString(xmlNode);
                } catch (Exception e) {
                    log.error("XML to JSON translation failed", e);
                    throw new RuntimeException("Translation failed", e);
                }
            })
            .channel("jsonOutputChannel")
            .get();
    }
}
```

**Protocol Translator:**

```java
@Component
public class ProtocolTranslator {

    @Bean
    public IntegrationFlow ftpToHttpTranslator() {
        return IntegrationFlow.from(Ftp.inboundAdapter(ftpTemplate())
                .remoteDirectory("/incoming")
                .patternFilter("*.csv"))
            .transform(File.class, file -> {
                try {
                    String content = new String(Files.readAllBytes(file.toPath()));
                    return convertCsvToHttpPayload(content);
                } catch (IOException e) {
                    throw new RuntimeException("File processing failed", e);
                }
            })
            .handle(Http.outboundChannelAdapter("http://api.example.com/data")
                .httpMethod(HttpMethod.POST))
            .get();
    }
}
```

**SOAP to REST Translator:**

```java
@Component
public class SoapToRestTranslator {

    @Bean
    public IntegrationFlow soapToRestFlow() {
        return IntegrationFlow.from("soapInputChannel")
            .transform(String.class, soapXml -> {
                // Parse SOAP XML
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(
                    new InputSource(new StringReader(soapXml)));

                // Extract body content
                NodeList nodeList = document.getElementsByTagName("Body");
                Element body = (Element) nodeList.item(0);

                // Convert to REST-compatible JSON
                Map<String, Object> restPayload = new HashMap<>();
                restPayload.put("operation", body.getFirstChild().getNodeName());
                restPayload.put("data", extractParameters(body));

                return new ObjectMapper().writeValueAsString(restPayload);
            })
            .handle(Http.outboundChannelAdapter("http://rest-api.example.com")
                .httpMethod(HttpMethod.POST))
            .get();
    }
}
```

### When to Use

- Integrating systems with different data formats (XML, JSON, CSV)
- Protocol conversion is required (SOAP to REST, JMS to HTTP)
- Legacy system integration requires format adaptation
- API versioning requires backward compatibility

### Trade-offs

| Pros | Cons |
|------|------|
| Enables system interoperability | Performance overhead for translation |
| Decouples format concerns | Translation logic complexity |
| Supports protocol conversion | Potential data loss during translation |
| Enables gradual system migration | Debugging translation issues |

### Production Use Cases

- **B2B Integration**: Convert EDI messages to internal formats
- **API Gateway**: Transform requests between different API versions
- **Data Migration**: Convert legacy data formats during system upgrades

---

## 2. Content Enricher

### Problem

A message does not contain all the data required for processing. Additional data must be retrieved from external sources and merged with the original message.

### Solution

Implement a Content Enricher that retrieves missing data from external sources and adds it to the message payload, creating a complete message for downstream processing.

### Implementation

```java
@Component
@Slf4j
public class ContentEnricherExample {

    @Bean
    public IntegrationFlow orderEnricherFlow() {
        return IntegrationFlow.from("orderInputChannel")
            .enrich(h -> h
                .requestChannel("enrichmentServiceChannel")
                .propertyExpression("customerDetails", "payload.customerDetails")
                .propertyExpression("productDetails", "payload.productDetails")
                .propertyExpression("shippingOptions", "payload.shippingOptions"))
            .channel("enrichedOrderChannel")
            .get();
    }

    @Bean
    public IntegrationFlow userEnrichmentService() {
        return IntegrationFlow.from("enrichmentServiceChannel")
            .transform(Order.class, order -> {
                // Fetch customer details
                CustomerDetails customer = customerService
                    .getCustomer(order.getCustomerId());

                // Fetch product details
                List<ProductDetails> products = order.getItems().stream()
                    .map(item -> productService.getProduct(item.getProductId()))
                    .collect(Collectors.toList());

                // Fetch shipping options
                List<ShippingOption> shippingOptions = shippingService
                    .getOptions(order.getShippingAddress());

                // Create enriched response
                EnrichmentResult result = new EnrichmentResult();
                result.setCustomerDetails(customer);
                result.setProductDetails(products);
                result.setShippingOptions(shippingOptions);

                return result;
            })
            .get();
    }
}
```

**Header Enricher:**

```java
@Component
public class HeaderEnricherExample {

    @Bean
    public IntegrationFlow headerEnrichmentFlow() {
        return IntegrationFlow.from("inputChannel")
            .enrichHeaders(h -> h
                .header("processingTimestamp", System.currentTimeMillis())
                .header("sourceSystem", "legacy-system")
                .headerExpression("payloadSize", "payload.length()")
                .headerFunction("correlationId",
                    msg -> UUID.randomUUID().toString()))
            .channel("enrichedOutputChannel")
            .get();
    }
}
```

**External Service Enrichment:**

```java
@Component
public class ExternalServiceEnricher {

    @Autowired
    private RestTemplate restTemplate;

    @Bean
    public IntegrationFlow externalEnrichmentFlow() {
        return IntegrationFlow.from("rawDataChannel")
            .enrich(h -> h
                .requestChannel("externalServiceChannel")
                .propertyExpression("geoLocation", "payload.location")
                .propertyExpression("weatherData", "payload.weather"))
            .channel("enrichedDataChannel")
            .get();
    }

    @Bean
    public IntegrationFlow externalServiceChannel() {
        return IntegrationFlow.from("externalServiceChannel")
            .handle((payload, headers) -> {
                String userId = ((Map<?, ?>) payload).get("userId").toString();

                // Call external API
                ResponseEntity<UserProfile> response = restTemplate.exchange(
                    "https://api.external.com/users/{userId}",
                    HttpMethod.GET,
                    null,
                    UserProfile.class,
                    userId);

                return response.getBody();
            })
            .get();
    }
}
```

### When to Use

- Messages need additional context from external sources
- Data from multiple sources must be combined
- Enrichment data changes independently of message producers
- You need to implement data aggregation patterns

### Trade-offs

| Pros | Cons |
|------|------|
| Completes incomplete messages | External service dependencies |
| Decouples data retrieval from producers | Performance impact from external calls |
| Supports flexible data aggregation | Error handling complexity |
| Enables data consolidation | Potential for data inconsistency |

### Production Use Cases

- **Order Processing**: Enrich orders with customer and product details
- **User Profiles**: Combine user data from multiple sources
- **Financial Analysis**: Enrich market data with historical information

---

## 3. Content Filter

### Problem

A message contains data that should not be forwarded to certain consumers. Some fields or sections must be removed or masked before the message can be safely transmitted.

### Solution

Implement a Content Filter that removes, masks, or transforms sensitive or unnecessary data from messages before forwarding them to consumers.

### Implementation

```java
@Component
@Slf4j
public class ContentFilterExample {

    @Bean
    public IntegrationFlow sensitiveDataFilter() {
        return IntegrationFlow.from("inputChannel")
            .transform(String.class, payload -> {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(payload);

                // Remove sensitive fields
                ObjectNode objectNode = (ObjectNode) node;
                objectNode.remove("creditCardNumber");
                objectNode.remove("ssn");
                objectNode.remove("password");

                // Mask partial data
                if (objectNode.has("email")) {
                    String email = objectNode.get("email").asText();
                    objectNode.put("email", maskEmail(email));
                }

                return mapper.writeValueAsString(objectNode);
            })
            .channel("filteredOutputChannel")
            .get();
    }

    private String maskEmail(String email) {
        int atIndex = email.indexOf("@");
        if (atIndex > 2) {
            return email.substring(0, 2) + "***" + email.substring(atIndex);
        }
        return "***" + email.substring(atIndex);
    }
}
```

**Field-Level Filtering:**

```java
@Component
public class FieldLevelFilter {

    @Bean
    public IntegrationFlow fieldFilterFlow() {
        return IntegrationFlow.from("userDataChannel")
            .transform(Map.class, userData -> {
                Map<String, Object> filteredData = new HashMap<>(userData);

                // Apply different filters based on user role
                String userRole = (String) userData.get("userRole");

                if ("EXTERNAL".equals(userRole)) {
                    filteredData.remove("internalNotes");
                    filteredData.remove("salary");
                    filteredData.remove("performanceReviews");
                } else if ("MANAGER".equals(userRole)) {
                    filteredData.remove("internalNotes");
                }

                return filteredData;
            })
            .channel("filteredUserDataChannel")
            .get();
    }
}
```

**GDPR Compliance Filter:**

```java
@Component
public class GdprComplianceFilter {

    @Bean
    public IntegrationFlow gdprFilterFlow() {
        return IntegrationFlow.from("personalDataChannel")
            .transform(PersonalData.class, data -> {
                // Apply GDPR rules
                if (data.getConsentStatus() == ConsentStatus.WITHDRAWN) {
                    return anonymizePersonalData(data);
                }

                // Remove data beyond retention period
                if (isDataExpired(data.getRetentionExpiry())) {
                    return null; // Message will be filtered out
                }

                // Apply data minimization
                return minimizeData(data);
            })
            .filter(Objects::nonNull)
            .channel("gdprCompliantChannel")
            .get();
    }
}
```

### When to Use

- PII data must be removed or masked before external transmission
- Compliance requirements (GDPR, HIPAA) mandate data filtering
- Different consumers require different levels of data detail
- Debugging requires removing sensitive information from logs

### Trade-offs

| Pros | Cons |
|------|------|
| Protects sensitive data | Data loss if filtering is too aggressive |
| Supports compliance requirements | Performance overhead for filtering |
| Enables role-based data access | Complex filtering rules |
| Simplifies downstream processing | Debugging filtered content |

### Production Use Cases

- **Healthcare Systems**: Filter patient data for HIPAA compliance
- **Financial Services**: Mask account numbers in transaction logs
- **Marketing Systems**: Remove PII before analytics processing

---

## 4. Content Modifier

### Problem

Message content needs to be modified or augmented without completely replacing the payload. Specific fields or values must be updated while preserving the overall structure.

### Solution

Implement a Content Modifier that selectively modifies message content by adding, updating, or removing specific fields or values.

### Implementation

```java
@Component
@Slf4j
public class ContentModifierExample {

    @Bean
    public IntegrationFlow orderModifierFlow() {
        return IntegrationFlow.from("orderInputChannel")
            .transform(Order.class, order -> {
                // Update order status
                order.setStatus(OrderStatus.PROCESSING);

                // Add processing metadata
                order.setProcessingTimestamp(System.currentTimeMillis());
                order.setProcessingNode(getNodeIdentifier());

                // Calculate derived fields
                order.setTotalAmount(calculateTotal(order.getItems()));
                order.setTaxAmount(calculateTax(order.getTotalAmount()));

                return order;
            })
            .channel("modifiedOrderChannel")
            .get();
    }
}
```

**JSON Content Modifier:**

```java
@Component
public class JsonContentModifier {

    @Bean
    public IntegrationFlow jsonModifierFlow() {
        return IntegrationFlow.from("jsonInputChannel")
            .transform(String.class, jsonPayload -> {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(jsonPayload);

                ObjectNode modifiedNode = (ObjectNode) node;

                // Add new field
                modifiedNode.put("processedAt",
                    Instant.now().toString());

                // Update existing field
                if (modifiedNode.has("status")) {
                    modifiedNode.put("status", "PROCESSED");
                }

                // Add nested field
                ObjectNode metadata = mapper.createObjectNode();
                metadata.put("version", "1.0");
                metadata.put("environment", "production");
                modifiedNode.set("metadata", metadata);

                return mapper.writeValueAsString(modifiedNode);
            })
            .channel("modifiedJsonChannel")
            .get();
    }
}
```

**Header Modifier:**

```java
@Component
public class HeaderModifierExample {

    @Bean
    public IntegrationFlow headerModifierFlow() {
        return IntegrationFlow.from("inputChannel")
            .enrichHeaders(h -> h
                .header("processedBy", "content-modifier-service")
                .header("processingTimestamp", System.currentTimeMillis())
                .removeHeader("internalTraceId")
                .headerExpression("uppercasePayload",
                    "payload.toUpperCase()"))
            .channel("modifiedOutputChannel")
            .get();
    }
}
```

### When to Use

- Message metadata needs to be updated during routing
- Derived values must be calculated and added to messages
- Message status or state needs to be tracked
- Audit information must be injected into messages

### Trade-offs

| Pros | Cons |
|------|------|
| Preserves original message structure | Modification logic complexity |
| Supports selective updates | Potential for unintended side effects |
| Enables metadata enrichment | Debugging modified messages |
| Simple to implement | May violate immutability principles |

### Production Use Cases

- **Workflow Systems**: Update message status as it flows through stages
- **Audit Trails**: Inject processing metadata into messages
- **Data Processing**: Add calculated fields to data streams

---

## 5. Claim Check

### Problem

Large message payloads consume excessive bandwidth and storage. The message system needs a way to handle large payloads without transmitting the entire payload through the messaging infrastructure.

### Solution

Implement a Claim Check pattern where large payloads are stored in an external storage system, and a reference (claim check) is placed in the message. Consumers retrieve the full payload using the claim check when needed.

### Implementation

```java
@Component
@Slf4j
public class ClaimCheckExample {

    @Autowired
    private BlobStorageService blobStorageService;

    @Bean
    public IntegrationFlow claimCheckInFlow() {
        return IntegrationFlow.from("inputChannel")
            .handle((payload, headers) -> {
                if (payload instanceof byte[] && ((byte[]) payload).length > 1024) {
                    // Store large payload
                    String claimCheckId = UUID.randomUUID().toString();
                    blobStorageService.store(claimCheckId, (byte[]) payload);

                    // Return claim check message
                    return MessageBuilder.withPayload("claim-check:" + claimCheckId)
                        .setHeader("claimCheckId", claimCheckId)
                        .setHeader("originalSize", ((byte[]) payload).length)
                        .build();
                }
                return payload;
            })
            .channel("claimCheckedChannel")
            .get();
    }

    @Bean
    public IntegrationFlow claimCheckOutFlow() {
        return IntegrationFlow.from("claimCheckOutChannel")
            .handle((payload, headers) -> {
                String claimCheckId = (String) headers.get("claimCheckId");

                if (claimCheckId != null) {
                    // Retrieve stored payload
                    byte[] originalPayload = blobStorageService.retrieve(claimCheckId);

                    // Clean up claim check (optional)
                    blobStorageService.delete(claimCheckId);

                    return originalPayload;
                }
                return payload;
            })
            .channel("restoredPayloadChannel")
            .get();
    }
}
```

**Spring Cloud Stream with Claim Check:**

```java
@Component
public class SpringCloudClaimCheck {

    @Bean
    public Function<Message<?>, Message<?>> claimCheckFunction() {
        return message -> {
            Object payload = message.getPayload();

            if (payload instanceof LargeObject) {
                LargeObject largeObj = (LargeObject) payload;

                // Store in external storage
                String reference = storeInExternalStorage(largeObj);

                // Return claim check
                return MessageBuilder.withPayload("reference:" + reference)
                    .copyHeaders(message.getHeaders())
                    .setHeader("claimCheckReference", reference)
                    .build();
            }

            return message;
        };
    }
}
```

### When to Use

- Large payloads (>1MB) must be transmitted through messaging
- Storage costs need to be reduced for message brokers
- Bandwidth optimization is critical
- Payloads contain binary data or large attachments

### Trade-offs

| Pros | Cons |
|------|------|
| Reduces message broker storage | Added complexity of external storage |
| Decreases network bandwidth usage | Additional latency for retrieval |
| Supports large payload handling | Storage system availability dependency |
| Enables payload reuse | Cleanup and lifecycle management |

### Production Use Cases

- **Document Processing**: Handle large document attachments
- **Media Processing**: Process large image or video files
- **Data Archival**: Archive large data payloads with metadata references

---

## 6. Normalizer

### Problem

Multiple systems send messages in different formats for the same business concept. The consumer needs a unified format to process these messages consistently.

### Solution

Implement a Normalizer that converts messages from various formats into a single canonical format, enabling consistent processing regardless of the original format.

### Implementation

```java
@Component
@Slf4j
public class OrderNormalizer {

    @Bean
    public IntegrationFlow orderNormalizationFlow() {
        return IntegrationFlow.from("multiFormatInputChannel")
            .<Message<?>, String>route(
                msg -> detectFormat(msg.getPayload()),
                mapping -> mapping
                    .subFlowMapping("xml", sf -> sf
                        .transform(XmlOrder.class, this::normalizeXmlOrder))
                    .subFlowMapping("json", sf -> sf
                        .transform(JsonOrder.class, this::normalizeJsonOrder))
                    .subFlowMapping("csv", sf -> sf
                        .transform(CsvOrder.class, this::normalizeCsvOrder))
                    .subFlowMapping("legacy", sf -> sf
                        .transform(LegacyOrder.class, this::normalizeLegacyOrder))
            )
            .channel("normalizedOrderChannel")
            .get();
    }

    private String detectFormat(Object payload) {
        if (payload instanceof String) {
            String content = (String) payload;
            if (content.trim().startsWith("<")) return "xml";
            if (content.trim().startsWith("{")) return "json";
            if (content.contains(",")) return "csv";
        }
        if (payload instanceof XmlOrder) return "xml";
        if (payload instanceof JsonOrder) return "json";
        if (payload instanceof CsvOrder) return "csv";
        if (payload instanceof LegacyOrder) return "legacy";
        return "unknown";
    }

    private NormalizedOrder normalizeXmlOrder(XmlOrder xmlOrder) {
        NormalizedOrder order = new NormalizedOrder();
        order.setOrderId(xmlOrder.getOrderNumber());
        order.setCustomerEmail(xmlOrder.getCustomer().getEmail());
        order.setItems(xmlOrder.getOrderItems().stream()
            .map(item -> new OrderItem(
                item.getSku(),
                item.getDescription(),
                item.getQty(),
                item.getPrice()))
            .collect(Collectors.toList()));
        order.setTotal(xmlOrder.getOrderTotal());
        order.setTimestamp(xmlOrder.getOrderDate().toInstant());
        return order;
    }

    private NormalizedOrder normalizeJsonOrder(JsonOrder jsonOrder) {
        NormalizedOrder order = new NormalizedOrder();
        order.setOrderId(jsonOrder.getId());
        order.setCustomerEmail(jsonOrder.getContact().getEmail());
        order.setItems(jsonOrder.getProducts().stream()
            .map(product -> new OrderItem(
                product.getCode(),
                product.getName(),
                product.getQuantity(),
                product.getUnitPrice()))
            .collect(Collectors.toList()));
        order.setTotal(jsonOrder.getGrandTotal());
        order.setTimestamp(Instant.parse(jsonOrder.getCreated()));
        return order;
    }
}
```

**Multi-Source Data Normalizer:**

```java
@Component
public class MultiSourceDataNormalizer {

    @Bean
    public IntegrationFlow sensorDataNormalization() {
        return IntegrationFlow.from("multiSensorInputChannel")
            .<Message<?>, String>route(
                msg -> {
                    String source = msg.getHeaders().get("sensorType", String.class);
                    return source;
                },
                mapping -> mapping
                    .subFlowMapping("temperature", sf -> sf
                        .transform(TemperatureReading.class,
                            this::normalizeTemperature))
                    .subFlowMapping("humidity", sf -> sf
                        .transform(HumidityReading.class,
                            this::normalizeHumidity))
                    .subFlowMapping("pressure", sf -> sf
                        .transform(PressureReading.class,
                            this::normalizePressure))
            )
            .channel("normalizedSensorChannel")
            .get();
    }

    private NormalizedSensorReading normalizeTemperature(TemperatureReading reading) {
        NormalizedSensorReading normalized = new NormalizedSensorReading();
        normalized.setSensorId(reading.getDeviceId());
        normalized.setReadingType(ReadingType.TEMPERATURE);
        normalized.setValue(reading.getCelsius());
        normalized.setUnit("CELSIUS");
        normalized.setTimestamp(reading.getTimestamp());
        normalized.setLocation(reading.getLocation());
        return normalized;
    }
}
```

### When to Use

- Multiple systems send data in different formats
- Legacy system integration requires format unification
- Data consolidation from heterogeneous sources
- API versioning requires backward compatibility

### Trade-offs

| Pros | Cons |
|------|------|
| Enables consistent processing | Multiple normalization rules |
| Supports heterogeneous sources | Performance overhead for detection |
| Simplifies consumer logic | Maintenance of format parsers |
| Enables gradual system migration | Potential data loss in normalization |

### Production Use Cases

- **Data Warehousing**: Normalize data from multiple source systems
- **IoT Platforms**: Unify sensor data from different manufacturers
- **Financial Systems**: Consolidate transaction data from various channels

---

## 7. Scatter-Gather (Transformation)

### Problem

A message transformation requires data from multiple sources. The transformation cannot be completed without aggregating information from different systems.

### Solution

Implement Scatter-Gather for transformation that sends requests to multiple data sources, gathers the responses, and combines them to create the transformed message.

### Implementation

```java
@Component
@Slf4j
public class TransformationScatterGather {

    @Bean
    public IntegrationFlow enrichAndTransformFlow() {
        return IntegrationFlow.from("rawDataChannel")
            .scatterGather(
                scatterer -> scatterer
                    .recipientFlow("customerServiceChannel")
                    .recipientFlow("productServiceChannel")
                    .recipientFlow("inventoryServiceChannel"),
                gatherer -> gatherer
                    .outputProcessor(group -> {
                        Map<String, Object> aggregatedData = new HashMap<>();

                        for (Message<?> message : group.getMessages()) {
                            String source = message.getHeaders()
                                .get("dataSource", String.class);
                            aggregatedData.put(source, message.getPayload());
                        }

                        return transformAggregatedData(aggregatedData);
                    })
                    .groupTimeout(10000L))
            .channel("transformedDataChannel")
            .get();
    }

    private TransformedData transformAggregatedData(
            Map<String, Object> aggregatedData) {
        CustomerData customer = (CustomerData) aggregatedData.get("customer");
        ProductData product = (ProductData) aggregatedData.get("product");
        InventoryData inventory = (InventoryData) aggregatedData.get("inventory");

        TransformedData result = new TransformedData();
        result.setCustomerName(customer.getName());
        result.setProductName(product.getName());
        result.setPrice(product.getPrice());
        result.setInStock(inventory.isAvailable());
        result.setDeliveryEstimate(calculateDelivery(customer, inventory));

        return result;
    }
}
```

### When to Use

- Transformation requires data aggregation from multiple sources
- Enrichment and normalization are combined
- Real-time data consolidation is needed
- Cross-system data validation is required

### Trade-offs

| Pros | Cons |
|------|------|
| Enables comprehensive transformation | Complex coordination logic |
| Supports parallel data retrieval | Increased latency |
| Provides complete data context | Error handling complexity |
| Enables real-time aggregation | Resource consumption |

### Production Use Cases

- **E-commerce**: Enrich product listings with inventory and pricing data
- **Healthcare**: Combine patient data from multiple medical systems
- **Financial Services**: Aggregate market data for trading decisions

---

## Pattern Comparison Matrix

| Pattern | Primary Use | Complexity | Performance | Use When |
|---------|-------------|------------|-------------|----------|
| Message Translator | Format conversion | Medium | Medium | Different formats between systems |
| Content Enricher | Data augmentation | Medium | Medium | Messages need additional context |
| Content Filter | Data removal | Low | Low | Sensitive data must be removed |
| Content Modifier | Selective updates | Low | Low | Metadata needs updating |
| Claim Check | Large payload handling | High | High | Large payloads (>1MB) |
| Normalizer | Format unification | High | Medium | Multiple input formats |
| Scatter-Gather | Data aggregation | High | Medium | Multiple data sources needed |

---

## References

- Enterprise Integration Patterns - Gregor Hohpe, Bobby Woolf
- Spring Integration Reference Guide
- Apache Camel Documentation
- Integration Patterns and Practices
