package annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;


/**
 * @author javaok
 * 2023/6/23 14:51
 */
@Target({java.lang.annotation.ElementType.TYPE})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Component {
    Level level();

    String value() default "";
}
