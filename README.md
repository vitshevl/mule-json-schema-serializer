# MuleSoft Kafka Schema Utilities

Utilities for serializing and deserializing JSON messages in MuleSoft applications integrated with Kafka and Confluent Schema Registry.

## Features

- **Serialize JSON**: Adds Magic byte and schema ID to JSON strings for Kafka.
- **Deserialize JSON**: Extracts JSON from Kafka messages containing a Magic byte and schema ID.

## Getting Started

### Prerequisites

- Java 8 or higher
- Maven

### Installation

First, clone the repository and install the library into your local Maven repository:

```bash
git clone https://github.com/vitshevl/mule-json-schema-serializer.git
cd mule-json-schema-serializer
mvn install
```

Then, add the following dependency to your project's `pom.xml`:

```xml
<dependency>
    <groupId>org.example</groupId>
    <artifactId>mule-json-schema-serializer</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Usage

**Serialization:**

```java
import org.example.mule.jsonschema.serializer.MuleKafkaJsonSchemaSerializer;

String json = "{\"name\":\"John Doe\", \"age\":30}";
int schemaId = 1;
String encoded = MuleKafkaJsonSchemaSerializer.encodeJsonWithSchemaId(json, schemaId);
```

**Deserialization:**

```java
import org.example.mule.jsonschema.deserializer.MuleKafkaJsonSchemaDeserializer;

String decodedJson = MuleKafkaJsonSchemaDeserializer.decodeJsonWithSchemaId(encoded);
```