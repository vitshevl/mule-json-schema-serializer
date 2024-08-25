package org.example.mule.jsonschema.serializer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Serializes JSON strings by adding a Magic byte and a schema ID, which is necessary
 * for integration with systems using the Confluent Schema Registry, such as Kafka.
 * Custom-designed for MuleSoft applications to handle messages
 * that need specific serialization formats for Kafka.
 */
public class MuleKafkaJsonSchemaSerializer {

  /**
   * Encodes a JSON string by adding a Magic byte and a schema ID at the beginning.
   * This method is particularly useful in MuleSoft applications where there is a need to
   * serialize messages to be sent to Kafka with schema information.
   *
   * @param json The JSON string to be serialized.
   * @param schemaId The schema identifier used in the Schema Registry.
   * @return A string that contains the Magic byte, schema ID, and the original JSON data.
   *         This string is encoded in ISO-8859-1 to preserve the byte array structure.
   */
  public static String encodeJsonWithSchemaId(String json, int schemaId) {

    byte[] jsonDataBytes = json.getBytes(StandardCharsets.UTF_8);

    byte magicByte = 0x0;

    ByteBuffer buffer = ByteBuffer.allocate(1 + 4 + jsonDataBytes.length);
    buffer.put(magicByte);
    buffer.putInt(schemaId);
    buffer.put(jsonDataBytes);

    byte[] messageBytes = buffer.array();

    return new String(messageBytes, StandardCharsets.ISO_8859_1);
  }
}