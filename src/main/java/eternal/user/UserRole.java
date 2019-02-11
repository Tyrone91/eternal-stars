package eternal.user;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
public class UserRole implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public static UserRole ADMIN = new UserRole("ADMIN", "Has access to everything.", UserRight.values());
    public static UserRole NORMAL_USER = new UserRole("NORMAL", "Normal user without privilege", UserRight.LOGIN);
    public static UserRole ANONYMOUS = new UserRole("ANONYMOUS", "Unknown user to the system", UserRight.LOGIN, UserRight.REGISTER);
    
    @Enumerated(EnumType.STRING)
    @ElementCollection
    private Set<UserRight> rights;
    
    @Id
    private String name;
    private String description;
    
    public UserRole() {
        
    }
    
    public UserRole(String name, String description, UserRight...rights ) {
       this.name = name;
       this.description = description;
       this.rights = new HashSet<>( Arrays.asList(rights));
    }
    
    public void setRights(Set<UserRight> rights) {
        this.rights = rights;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
    
    public String getName() {
        return name;
    }
    
    public boolean hasRight(UserRight right) {
        return rights.contains(right);
    }
    
    public Set<UserRight> getRights() {
        return this.rights;
    }
}
