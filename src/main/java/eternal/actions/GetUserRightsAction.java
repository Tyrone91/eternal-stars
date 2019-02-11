package eternal.actions;

import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import eternal.user.User;
import eternal.user.UserRight;

@Named
@SessionScoped
public class GetUserRightsAction extends AbstractAction<List<UserRight>, Void>{

    private static final long serialVersionUID = 1L;

    @Override
    public UserRight getNeededRight() {
        return UserRight.USER_MANAGMENT_VIEW_ALL_RIGHTS;
    }

    @Override
    protected List<UserRight> action(User user, Void... args) {
        return Arrays.asList(UserRight.values());
    }

}
