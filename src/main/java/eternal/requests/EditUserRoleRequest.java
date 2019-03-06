package eternal.requests;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIInput;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.session.InteractionHandler;
import eternal.user.UserRight;
import eternal.user.UserRole;

@Named
@RequestScoped
public class EditUserRoleRequest implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String response;
    
    private Map<UserRole, Set<UserRight>> roleTorights;
    
    private List<RoleWrapper> wrappers;
    
    @Inject
    private InteractionHandler handler;
    
    @PostConstruct
    public void init() {
        roleTorights = new HashMap<>();
        wrappers = build();
    }
    
    private Set<UserRight> get(UserRole role) {
        Set<UserRight> res = roleTorights.get(role);
        if(res == null) {
            res = new HashSet<>();
            roleTorights.put(role, res);
        }
        return res;
    }
    
    private List<RoleWrapper> build() {
        List<RoleWrapper> wrappers = new ArrayList<>();
        
        List<UserRole> roles = handler.allAvailableUserRoles();
        roles.sort( (r1, r2) -> r1.getName().compareTo(r2.getName()) );
        
        for(UserRole role : roles) {
            RoleWrapper wrapper = new RoleWrapper();
            
            List<RightWrapper> rights = new ArrayList<>();
            List<UserRight> userrights = handler.allUserRights();
            userrights.sort( (s1,s2 ) -> s1.compareTo(s2));
            for(UserRight right : userrights) {
                RightWrapper rw = new RightWrapper();
                
                rw.setRole(role);
                rw.setSelected(role.hasRight(right));
                rw.setRight(right);
                rights.add(rw);
            }
            
            wrapper.setRights(rights);
            wrapper.setRole(role);
            
            wrappers.add(wrapper);
        }
        return wrappers;
    }
    
    public List<RoleWrapper> getRoles() {
        return wrappers;
    }
    
    public void roleChanged(ValueChangeEvent e) {
        UIInput input = (UIInput)e.getSource();
        UserRole role = (UserRole)input.getAttributes().get("attr_role");
        UserRight right = (UserRight)input.getAttributes().get("attr_right");
        boolean active = (boolean)e.getNewValue();
        
        if(active) {
            get(role).add(right);
        } else {
            get(role).remove(right);
        }
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
    
    public class RoleWrapper {
        
        private UserRole role;
        private List<RightWrapper> rights;
        
        public UserRole getRole() {
            return role;
        }
        
        public void setRole(UserRole role) {
            this.role = role;
        }
        
        public List<RightWrapper> getRights() {
            return rights;
        }
        
        public void setRights(List<RightWrapper> rights) {
            this.rights = rights;
        }
    }
    
    public class RightWrapper {
        
        private boolean selected;
        private UserRight right;
        private UserRole role;
        
        public RightWrapper() {
            
        }

        public UserRole getRole() {
            return role;
        }

        public void setRole(UserRole role) {
            this.role = role;
        }

        public UserRight getRight() {
            return right;
        }

        public void setRight(UserRight right) {
            this.right = right;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
        
        
        
    }
}
