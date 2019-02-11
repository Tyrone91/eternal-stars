package eternal.requests;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import eternal.user.User;

@Named
@RequestScoped
public class GetUserListRequest implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private List<User> result = Collections.emptyList();

    public List<User> getResult() {
        return result;
    }

    public void setResult(List<User> result) {
        this.result = result;
    }
}
