package eternal.user;

public class AnonymousUser extends User {

    private static final long serialVersionUID = 1L;
    
    public AnonymousUser() {
        setEmail("");
        setUserName("Anonymous");
        setPassword("");
        addRole(UserRole.ANONYMOUS);
    }

}
