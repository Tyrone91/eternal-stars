package eternal.requests;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import eternal.user.User;
import eternal.user.UserRole;

@Named
@SessionScoped
public class UserListSortRequest implements Serializable {
    
    private static final long serialVersionUID = 1L;

    public enum Sort {
        USERNAME_DESC,
        USERNAME_ASCE,
        
        EMAIL_DESC,
        EMAIL_ASCE,
        
        ROLE_DESC,
        ROLE_ASCE
    }
    
    private String usernameFilter = "";
    
    private Sort currentSort = Sort.USERNAME_ASCE;
    
    private void toggle(Sort a, Sort b) {
        if(currentSort == a) {
            currentSort = b;
        } else { 
            currentSort = a;
        }
    }
    
    public void toggleUsername() {
        toggle(Sort.USERNAME_ASCE, Sort.USERNAME_DESC);
    }
    
    public void toggleEmail() {
        toggle(Sort.EMAIL_ASCE, Sort.EMAIL_DESC);
    }
    
    public void toggleRole() {
        toggle(Sort.ROLE_ASCE, Sort.ROLE_DESC);
    }
    
    public List<User> sort(List<User> list) {
        List<User> sortedList = null;
        if(usernameFilter != null && !usernameFilter.isEmpty()) {
            sortedList = list.stream().filter( u -> u.getUsername().toLowerCase().contains(usernameFilter.toLowerCase())).collect(Collectors.toList());
        } else {
            sortedList = new ArrayList<>(list);
        }
        switch (currentSort) {
        
            case USERNAME_ASCE: sortedList.sort( (u1, u2) -> u1.getUsername().compareTo(u2.getUsername()) ); break;
            case USERNAME_DESC: sortedList.sort( (u1, u2) -> u2.getUsername().compareTo(u1.getUsername()) ); break;
            
            case EMAIL_ASCE: sortedList.sort( (u1, u2) -> u1.getEmail().compareTo(u2.getEmail()) ); break;
            case EMAIL_DESC: sortedList.sort( (u1, u2) -> u2.getEmail().compareTo(u1.getEmail()) ); break;
            
            case ROLE_ASCE: sortedList.sort( (u1, u2) -> this.sortByRole(u1, u2)); break;
            case ROLE_DESC: sortedList.sort( (u1, u2) -> this.sortByRole(u2, u1)); break;

            default: break;
        }
        
        return sortedList;
    }
    
    private int sortByRole(User u1, User u2) {
        if(u1.getRolesAsList().size() > u2.getRolesAsList().size() ) {
            return 1;
        } else if(u2.getRolesAsList().size() > u1.getRolesAsList().size() ){
            return -1;
        }
        
        Optional<UserRole> f = u1.getRolesAsList().stream().findFirst();
        Optional<UserRole> s = u2.getRolesAsList().stream().findFirst();
        
        if(f.isPresent() && s.isPresent()) {
            return f.get().getName().compareTo(s.get().getName());
        } else {
            return 0;
        }
    }

    public String getUsernameFilter() {
        return usernameFilter;
    }

    public void setUsernameFilter(String usernameFilter) {
        this.usernameFilter = usernameFilter;
    }
}
