package eternal.util;

public class Strings {
    
    public static boolean exists(String str) {
        return str != null && !str.isEmpty();
    }
    
    public static boolean exists(String...strings ) {
        for(String str : strings) {
            if(!exists(str)) {
                return false;
            }
        }
        return true;
    }
}
