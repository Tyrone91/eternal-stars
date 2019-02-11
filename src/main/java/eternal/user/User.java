package eternal.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class User implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    private String username;
    private String password;
    private String email;
    
    @Enumerated(EnumType.STRING)
    @ElementCollection
    private Set<UserRight> rights;
    
    @ManyToMany
    private Set<UserRole> roles;
    
    public User() {
        this.username = "";
        this.rights = new HashSet<>();
        this.roles = new HashSet<>();
        this.lastLogin = null;
    }
    
    public Set<UserRight> getRights() {
        return rights;
    }

    public void setRights(Set<UserRight> rights) {
        this.rights = rights;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private Date lastLogin;
    
    public boolean hasRight(UserRight right) {
        return this.rights.contains(right) || roles.stream().anyMatch(role -> role.hasRight(right));
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public void setUserName(String username) {
        this.username = username;
    }
    
    public Set<UserRight> getUserRights() {
        return this.rights;
    }
    
    public void setUserRights(Set<UserRight> rights) {
        this.rights = rights;
    }
    
    public Optional<Date> getLastLogin() {
        return Optional.ofNullable(lastLogin);
    }
    
    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public void addRole(UserRole role) {
        System.out.println("role incoming" + role);
        this.roles.add(role);
    }
    
    public void removeRole(UserRole role) {
        this.roles.remove(role);
    }
    
    public List<UserRole> getRolesAsList() {
        return new ArrayList<>(getRoles());
    }
}
