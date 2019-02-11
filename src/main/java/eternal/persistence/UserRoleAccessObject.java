package eternal.persistence;

import java.util.Optional;
import java.util.Set;

import eternal.user.UserRole;

public interface UserRoleAccessObject {

    void storeRole(UserRole role);

    Set<UserRole> getAllRoles();

    Optional<UserRole> getRole(String roleName);

}