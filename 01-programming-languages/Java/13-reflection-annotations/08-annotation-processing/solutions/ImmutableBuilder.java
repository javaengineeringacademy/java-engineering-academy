package academy.javaengineering.reflection.annotationprocessing.solutions;

import java.lang.annotation.*;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface ImmutableBuilder {}
