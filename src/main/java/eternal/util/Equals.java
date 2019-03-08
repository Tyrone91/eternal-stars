package eternal.util;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

public class Equals {
        
    public static Uncasted isNotNull(Object obj) {
        return new Uncasted(obj == null, obj);
    }
    
    final public static class Uncasted {
        
        private Object checkedObject;
        private boolean failed;
        
        private Uncasted(boolean failed, Object obj) {
            this.failed = failed;
            this.checkedObject = obj;
        }
        
        @SuppressWarnings("unchecked")
        public <T> Casted<T> isInstance(Class<T> clazz) {
            if(failed || !clazz.isInstance(checkedObject)) {
                return new Casted<>(true, null);
            } else {
                return new Casted<>(false, (T)checkedObject); //This is checked by clazz.isInstance() but the warning check only for instanceof which doesn't work generics
            }
        }
    }
    
    final public static class Casted<T> {
        
        private List<BiFunction<T, T, Boolean>> checkers;
        private boolean failed;
        private T other;
        
        private Casted(boolean failed, T obj) {
            this.failed = failed;
            this.other = obj;
            if(!failed) {
                checkers = new LinkedList<>();
            }
        }
        
        public Casted<T> checkIf(BiFunction<T, T, Boolean> validator) {
            if(!failed) {
                checkers.add(validator);
            }
            return this;
        }
        
        public boolean isEqualTo(T pivot) {
            if(failed) {
                return false;
            }
            
            for(BiFunction<T, T, Boolean> check : checkers) {
                if(!check.apply(pivot, other)) {
                    return false;
                }
            }
            return true;
        }
    }
    
}
