package eternal.user;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
    
    @Id
    private String id;
    private String username;
    private Set<UserRight> rights;
    private Date lastLogin;
    
    public User() {
        this.id = UUID.randomUUID().toString();
        this.username = "";
        this.rights = new HashSet<>();
        this.lastLogin = null;
    }
    
    public boolean hasRight(UserRight right) {
        return this.rights.contains(right);
    }
    
    public String getId() {
        return this.id;
    }
    public String getUsername() {
        return this.username;
    }
    
    public Set<UserRight> getUserRights() {
        return this.rights;
    }
    
    public Optional<Date> getLastLogin() {
        return Optional.ofNullable(lastLogin);
    }
}
