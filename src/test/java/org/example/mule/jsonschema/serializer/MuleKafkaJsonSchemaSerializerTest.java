package org.example.mule.jsonschema.serializer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class MuleKafkaJsonSchemaSerializerTest {

  @Test
  void testEncodeJsonWithSchemaId() {
    String json = "{\"name\":\"John\", \"age\":30}";
    int schemaId = 123;
    String expectedOutput =
        new String(
            new byte[] {
              0, 0, 0, 0, 123, 123, 34, 110, 97, 109, 101, 34, 58, 34, 74, 111, 104, 110, 34, 44,
              32, 34, 97, 103, 101, 34, 58, 51, 48, 125
            },
            StandardCharsets.ISO_8859_1);

    String actualOutput = MuleKafkaJsonSchemaSerializer.encodeJsonWithSchemaId(json, schemaId);

    assertEquals(
        expectedOutput, actualOutput, "The encoded string did not match the expected output.");
  }
}
