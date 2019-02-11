package eternal.requests;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIInput;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import eternal.user.UserRight;
import eternal.user.UserRole;

@Named
@RequestScoped
public class EditUserRoleRequest implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String response;
    
    private Map<UserRole, Set<UserRight>> roleTorights;
    
    private Set<UserRight> get(UserRole role) {
        Set<UserRight> res = roleTorights.get(role);
        if(res == null) {
            res = new HashSet<>();
            roleTorights.put(role, res);
        }
        return res;
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

}
