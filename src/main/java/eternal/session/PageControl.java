package eternal.session;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;


@Named
@SessionScoped
public class PageControl implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private static final String LOGIN_PAGE = "/jsf/user/login.xhtml";
    private static final String REGISTRATION_PAGE = "/jsf/user/registration.xhtml";
    private static final String GAME_PAGE = "/jsf/game/game.xhtml";
    private static final String ADMIN_PAGE = "/jsf/admin/admin.xhtml";
    
    private static final String FALLBACK_PAGE = LOGIN_PAGE;
    
    
    private static final Map<String,String> NAME_TO_FILE = new HashMap<>();
    static {
        page(
                "login", LOGIN_PAGE,
                "registration", REGISTRATION_PAGE,
                "game", GAME_PAGE,
                "admin", ADMIN_PAGE);
    }
    
    @PostConstruct
    private void setup() {
        this.pageStack.push(LOGIN_PAGE);
    }
    
    private Stack<String> pageStack = new Stack<>();
    
    private static void page(String... nameAndValue) {
        assert nameAndValue.length % 2 == 0;
        for(int i = 0; i < nameAndValue.length; i += 2) {
            final String name = nameAndValue[i];
            final String val = nameAndValue[i+1];
            NAME_TO_FILE.put(name, val);
        }
    }
    
    private Optional<String> absolute(String str) {
        return Optional.ofNullable(NAME_TO_FILE.get(str));
    }
    
    public String push(String page) {
        final String res = absolute(page).orElse(FALLBACK_PAGE);
        this.pageStack.push(res);
        return res;
    }
    
    public String pop(String page) {
        if(this.pageStack.isEmpty()) {
            return FALLBACK_PAGE;
        }
        return this.pageStack.pop();
    }
    
    public String getCurrentView() {
        if(this.pageStack.isEmpty()) {
            return FALLBACK_PAGE;
        }
        return this.pageStack.peek();
    }
    
    public void view(String page) {
        this.pageStack.clear();
        push(page);
    }
}
