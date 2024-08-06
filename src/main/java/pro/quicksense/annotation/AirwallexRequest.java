package pro.quicksense.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The request annotated by this annotation will be intercepted to make sure that it is authenticated by Airwallex,
 * otherwise, a '401 Unauthorized' code would be returned by Airwallex.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AirwallexRequest {
}

