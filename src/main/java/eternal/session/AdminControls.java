package eternal.session;

import java.io.Serializable;
import java.util.Date;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.user.User;
import eternal.user.UserRole;
import eternal.util.UITable;

@Named
@SessionScoped
public class AdminControls implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Inject
    private InteractionHandler handler;
    
    private UITable<User> userTable;
    
    @PostConstruct
    private void setup() {
        final UITable.Builder<User> builder = UITable.builder();
        this.userTable = builder
                .add("Name(ID)", (column, u1, u2) -> u1.getUsername().compareTo(u2.getUsername()))
                .add("Email", (column, u1, u2) -> u1.getEmail().compareTo(u2.getEmail()))
                .add("Last Login")
                .add("Role", this::rolecomparator)
                .build(handler::allRegisteredUser, this::usermapper);
        this.userTable.setEntriesPerPage(10);
    }
    
    private String getSortedUserRole(User user) {
        return user.getRolesAsList().stream()
                .map(UserRole::getName)
                .sorted(String::compareTo)
                .collect(Collectors.joining(","));
    }
    
    private String lastLoginToString(User user) {
        return user.getLastLogin().map(Date::toString).orElse("");
    }
    
    private int rolecomparator(String column, User u1, User u2) {
        final String r1 = getSortedUserRole(u1);
        final String r2 = getSortedUserRole(u2);
        return r1.compareTo(r2);
    }
    
    private UITable.Row<User> usermapper(User user, UITable.Row<User> row) {
        return row
                .set("Name(ID)", user, User::getUsername)
                .set("Email", user, User::getEmail)
                .set("Last Login",user, this::lastLoginToString)
                .set("Role", user, this::getSortedUserRole)
                
                .action("fas fa-times red", "delete", u -> handler.deleteAnyUser(u))
                .action("far fa-eye", "view", u -> {
                    
                });
    }
    
    
    
    public UITable<User> getUsersAsTable() {
        return this.userTable.updateData();
    }
    
}
