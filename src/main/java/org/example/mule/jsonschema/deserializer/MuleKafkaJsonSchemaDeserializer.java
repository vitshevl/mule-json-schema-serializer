package org.example.mule.jsonschema.deserializer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Deserializes encoded strings from Kafka that contain a Magic byte, schema ID, and JSON data.
 * Custom-designed for MuleSoft applications to handle messages with schema information from
 * Confluent Schema Registry.
 */
public class MuleKafkaJsonSchemaDeserializer {

  /**
   * Decodes an ISO-8859-1 encoded string by removing the Magic byte and schema ID, returning the
   * JSON data as a UTF-8 string.
   *
   * @param encodedString The string containing the Magic byte, schema ID, and JSON data.
   * @return The extracted JSON string.
   */
  public static String decodeJsonWithSchemaId(String encodedString) {
    byte[] messageBytes = encodedString.getBytes(StandardCharsets.ISO_8859_1);
    ByteBuffer buffer = ByteBuffer.wrap(messageBytes);

    buffer.get();
    buffer.getInt();

    byte[] jsonBytes = new byte[buffer.remaining()];
    buffer.get(jsonBytes);

    return new String(jsonBytes, StandardCharsets.UTF_8);
  }
}
