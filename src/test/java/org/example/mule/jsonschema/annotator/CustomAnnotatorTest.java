package org.example.mule.jsonschema.annotator;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import io.confluent.kafka.schemaregistry.annotations.Schema;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.jsonschema2pojo.DefaultGenerationConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CustomAnnotatorTest {

  private static final String SCHEMAS_DIR = "src/test/resources/json-schemas";
  private static final String SCHEMA_ANNOTATION = Schema.class.getName();
  private static final String TITLE = "title";
  private static final String VALUE = "value";
  private static final String REFS = "refs";

  private CustomAnnotator customAnnotator;
  private JCodeModel codeModel;

  @BeforeEach
  void setUp() throws Exception {
    DefaultGenerationConfig generationConfig = Mockito.mock(DefaultGenerationConfig.class);
    customAnnotator = new CustomAnnotator(generationConfig);
    codeModel = new JCodeModel();
  }

  @Test
  void testAnnotator() {
    try {
      Files.walk(Paths.get(SCHEMAS_DIR))
          .filter(Files::isRegularFile)
          .map(Path::toFile)
          .map(CustomAnnotatorTest::readSchema)
          .forEach(schemaNode -> processSchema(schemaNode, codeModel, customAnnotator));
    } catch (IOException e) {
      fail("Exception should not have been thrown.", e);
    }
  }

  private static JsonNode readSchema(File schemaFile) {
    try {
      return new ObjectMapper().readTree(schemaFile);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static void processSchema(
      JsonNode schemaNode, JCodeModel codeModel, CustomAnnotator customAnnotator) {
    try {
      String className = schemaNode.get(TITLE).asText();
      JDefinedClass jclass = codeModel._class(className);
      customAnnotator.typeInfo(jclass, schemaNode);

      // Check that the class was annotated with @Schema
      boolean isAnnotatedWithSchema =
          jclass.annotations().stream()
              .anyMatch(
                  annotation ->
                      annotation.getAnnotationClass().fullName().equals(SCHEMA_ANNOTATION));
      assertTrue(isAnnotatedWithSchema, "Class should be annotated with @Schema");

      // Check that the annotation has the correct values
      jclass.annotations().stream()
          .filter(
              annotation -> annotation.getAnnotationClass().fullName().equals(SCHEMA_ANNOTATION))
          .forEach(
              annotation -> {
                assertNotNull(
                    annotation.getAnnotationMembers().get(VALUE), "Annotation should have a value");
                assertNotNull(
                    annotation.getAnnotationMembers().get(REFS), "Annotation should have a ref");
              });
    } catch (JClassAlreadyExistsException e) {
      throw new RuntimeException(e);
    }
  }
}
