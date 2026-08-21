package academy.javaengineering.reflection.annotationprocessing.practices;

import java.lang.annotation.*;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface GenerateToString {}
