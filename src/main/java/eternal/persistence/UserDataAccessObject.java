package eternal.persistence;

import java.util.Optional;
import java.util.Set;

import eternal.user.User;

public interface UserDataAccessObject {

    public Set<User> fetchAllUsers();
    public boolean storeUser(User user);
    public boolean updateUser(User user);
    public Optional<User> loadUser(String username);
    public boolean deleteUser(String username);

}