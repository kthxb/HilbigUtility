package com.ash.input;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <b>Please read the javadoc of {@link ClassInputInterfaceGenerator}</b>
 * @author Aaron Hilbig
 *
 */
@Target({FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface UserParameter { }