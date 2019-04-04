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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import eternal.game.control.GameAccount;
import eternal.mangement.GameAccountHandler;

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
    @JoinTable
    private Set<UserRole> roles;
    
    private transient GameAccount gameAccount;
    
    public User() {
        this.username = "";
        this.rights = new HashSet<>();
        this.roles = new HashSet<>();
        this.lastLogin = null;
    }
    
    /**
     * This field is only set, if this User has logged in with this account. Otherwise use the {@link GameAccountHandler}.
     * @return
     */
    public Optional<GameAccount> getGameAccount() {
        return Optional.ofNullable(gameAccount);
    }
    
    public void setGameAccount(GameAccount account) {
        gameAccount = account;
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
        this.roles.add(role);
    }
    
    public void removeRole(UserRole role) {
        this.roles.remove(role);
    }
    
    public List<UserRole> getRolesAsList() {
        return new ArrayList<>(getRoles());
    }
}
