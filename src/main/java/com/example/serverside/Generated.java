package com.example.serverside;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Custom annotation named "Generated" whose retention policy is RUNTIME.
 * Add this annotation (@Generated) above the class or method that is not needed while JaCoCo Reporting.
 * Used when method carries no specific business logic or cannot be tested, useful to exclude them from report to
 * provide better view of the test coverage.
 */

@Documented
@Retention(RUNTIME)
@Target({TYPE, METHOD, CONSTRUCTOR})
public @interface Generated {
}

