package com.dafttech.eventmanager;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Mark specific Methods for Listening specific Events. The Method has to have
 * the parameters: (EventStream eventStream, Object object)
 * 
 * @param String
 *            []: Namen der Events.
 */
@Target(METHOD)
@Retention(RUNTIME)
@Documented
public @interface EventListener {
    int[] eventTypeId();

    int priority() default EventType.PRIORITY_STANDARD;
}
