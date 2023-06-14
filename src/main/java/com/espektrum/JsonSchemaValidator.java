package com.espektrum;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;

/**
 * Simple program to validate JSON files against a JSON Schema
 */
public class JsonSchemaValidator {

    // Schema files should be in resources/schema
    private static final String SCHEMA_FILE_NAME = "schema.json";

    // Test files should be in resources/json
    private static final String TEST_FILE_NAME = "test.json";

    public static void main(String[] args) {
        try (InputStream schemaInputStream = JsonSchemaValidator.class.getResourceAsStream(
                String.format("/schema/%s", SCHEMA_FILE_NAME));
             InputStream payloadInputStream = JsonSchemaValidator.class.getResourceAsStream(
                     String.format("/json/%s", TEST_FILE_NAME))) {

            assert schemaInputStream != null : "schemaInputStream is null";
            assert payloadInputStream != null : "payloadInputStream is null";

            // Get schema
            final Schema schema = SchemaLoader.load(new JSONObject((new JSONTokener(schemaInputStream))));

            // Get payload
            final JSONObject rawPayload = new JSONObject(new JSONTokener(payloadInputStream));

            // Validate
            schema.validate(rawPayload);

            System.out.println("Validation successful!");
        } catch (ValidationException ex) {
            // List validation errors
            System.out.println(ex.toJSON().toString(4));
        } catch (AssertionError | Exception ae) {
            ae.printStackTrace();
        }
    }
}
