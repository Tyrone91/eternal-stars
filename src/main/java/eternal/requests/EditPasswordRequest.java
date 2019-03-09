package eternal.requests;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class EditPasswordRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String oldPassword;
    private String newPassword;
    private String newPasswordConfirm;
    
    public String getNewPassword() {
        return newPassword;
    }
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    
    public String getNewPasswordConfirm() {
        return newPasswordConfirm;
    }
    public void setNewPasswordConfirm(String newPasswordConfirm) {
        this.newPasswordConfirm = newPasswordConfirm;
    }
    
    public String getOldPassword() {
        return oldPassword;
    }
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

}
