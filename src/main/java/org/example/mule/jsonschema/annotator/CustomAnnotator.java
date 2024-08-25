package org.example.mule.jsonschema.annotator;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JDefinedClass;
import io.confluent.kafka.schemaregistry.annotations.Schema;
import java.util.Optional;
import org.jsonschema2pojo.AbstractAnnotator;
import org.jsonschema2pojo.GenerationConfig;

/**
 * The CustomAnnotator class extends the AbstractAnnotator class from the jsonschema2pojo library.
 * It is used to add custom annotations to the generated Java classes.
 */
public class CustomAnnotator extends AbstractAnnotator {

  private static final String SCHEMA_VERSION = "http://json-schema.org/draft-04/schema#";
  private static final String VALUE = "value";
  private static final String REFS = "refs";

  /**
   * Constructs a new CustomAnnotator.
   *
   * @param generationConfig the generation configuration
   * @throws Exception if an error occurs while reading the configuration
   */
  public CustomAnnotator(GenerationConfig generationConfig) throws Exception {
    super(generationConfig);
  }

  /**
   * Adds custom annotations to the generated Java class.
   *
   * @param clazz the generated Java class
   * @param schema the JSON schema
   */
  @Override
  public void typeInfo(JDefinedClass clazz, JsonNode schema) {

    Optional.ofNullable(schema)
        .map(JsonNode::toString)
        .filter(stringJson -> stringJson.contains(SCHEMA_VERSION))
        .ifPresent(stringJson -> processSchema(clazz, schema));
  }

  /**
   * Processes the JSON schema and adds custom annotations to the generated Java class.
   *
   * @param clazz the generated Java class
   * @param schema the JSON schema
   */
  private void processSchema(JDefinedClass clazz, JsonNode schema) {
    JAnnotationUse annotation = clazz.annotate(Schema.class);
    annotation.param(VALUE, schema.toString());
    annotation.paramArray(REFS);
  }
}
