package eternal.user;

import java.util.Set;

public interface UserRole {
    
    public String getDescription();
    public String getName();
    public boolean hasRight(UserRight right);
    public Set<UserRight> getRights();
}
