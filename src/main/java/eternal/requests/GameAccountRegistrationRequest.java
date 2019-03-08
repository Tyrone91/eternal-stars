package eternal.requests;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class GameAccountRegistrationRequest extends RegistrationRequest {

    private static final long serialVersionUID = 1L;
    
    private String displayName;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}
