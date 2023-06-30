package util;

/**
 * @author javaok
 * 2023/6/21 9:01
 */
public class StringUtil {
    public static boolean isAnyEmpty(String... str) {
        for (String s : str) {
            if (s == null || s.isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
