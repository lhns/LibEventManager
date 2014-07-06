package com.dafttech.eventmanager;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
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
@Target({ METHOD, CONSTRUCTOR })
@Retention(RUNTIME)
@Documented
public @interface EventListener {
    String[] value();

    String[] filter() default {};

    int priority() default EventType.PRIORITY_STANDARD;

    @Target({ METHOD })
    @Retention(RUNTIME)
    @Documented
    public static @interface Group {
        EventListener[] value();
    }

    @Target({ METHOD, FIELD, CONSTRUCTOR, TYPE })
    @Retention(RUNTIME)
    @Documented
    public @interface Filter {
        String value();
    }
}