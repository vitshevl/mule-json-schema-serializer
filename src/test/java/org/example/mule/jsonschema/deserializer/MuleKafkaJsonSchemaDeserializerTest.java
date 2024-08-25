package org.example.mule.jsonschema.deserializer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class MuleKafkaJsonSchemaDeserializerTest {

  @Test
  void testDecodeJsonWithSchemaId() {
    String json = "{\"name\":\"John\", \"age\":30}";
    int schemaId = 123;
    byte magicByte = 0x0;
    ByteBuffer buffer = ByteBuffer.allocate(1 + 4 + json.getBytes(StandardCharsets.UTF_8).length);
    buffer.put(magicByte);
    buffer.putInt(schemaId);
    buffer.put(json.getBytes(StandardCharsets.UTF_8));
    String encodedString = new String(buffer.array(), StandardCharsets.ISO_8859_1);

    String decodedJson = MuleKafkaJsonSchemaDeserializer.decodeJsonWithSchemaId(encodedString);

    assertEquals(json, decodedJson, "The decoded JSON does not match the original.");
  }
}
