package com.framework.api;

import com.atlassian.oai.validator.restassured.OpenApiValidationFilter;
import com.framework.config.ConfigManager;

/**
 * Provides a Rest Assured filter that validates every request/response
 * against the configured OpenAPI spec.  Drop it into any Rest Assured call:
 *
 *   given().filter(OpenApiValidator.filter()).when().get("/pet/1");
 *
 * If the request or response violates the spec the test fails automatically.
 */
public final class OpenApiValidator {

    private static volatile OpenApiValidationFilter instance;

    private OpenApiValidator() {}

    public static OpenApiValidationFilter filter() {
        if (instance == null) {
            synchronized (OpenApiValidator.class) {
                if (instance == null) {
                    String specPath = ConfigManager.get("openapi.spec.path");
                    instance = new OpenApiValidationFilter(specPath);
                }
            }
        }
        return instance;
    }
}
