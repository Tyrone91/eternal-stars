package eternal.persistence;

import java.util.Optional;
import java.util.Set;

import eternal.user.UserRole;

public interface UserRoleAccessObject {

    public void storeRole(UserRole role);

    public Set<UserRole> getAllRoles();

    public Optional<UserRole> getRole(String roleName);
    
    public boolean updateRole(UserRole role);

    public boolean refreshRole(UserRole role);

}