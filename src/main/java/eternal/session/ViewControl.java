package eternal.session;

import java.io.Serializable;
import java.util.Stack;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import eternal.game.control.UniverseHandler;
import eternal.game.environment.Sector;
import eternal.game.environment.Universe;
import eternal.user.UserRight;

/**
 * Helper class to navigate inside the admin page.
 */
@Named
@SessionScoped
public class ViewControl implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Inject
    private UniverseEditHolder universeEditHolder;
    
    @Inject
    SessionContext sessionContext;
    
    @Inject
    UniverseHandler universeHandler;
    
    private Stack<String> pageStack;
    
    @PostConstruct
    public void init() {
        pageStack = new Stack<>();
    }
    
    public String pushPage(String page) {
        pageStack.add(page);
        return page;
    }
    
    public String showUniverse(Universe universe) {
        universeEditHolder.setSelectedUniverse(universe);
        return pushPage("/admin-universe-focus.xhtml");
    }
    
    public String showAdminPage() {
        return pushPage("/admin.xhtml");
    }
    
    public String showSector(Sector sector) {
        universeHandler.initUniverse(universeEditHolder.getSelectedUniverse());
        universeEditHolder.setSelectedSector(sector);
        return pushPage("/admin-universe-sector-focus.xhtml");
    }
    
    public String goBack() {
        if(pageStack.isEmpty()) {
            return "/login.xhtml";
        }
        pageStack.pop();
        
        if(pageStack.isEmpty()) {
            return "/login.xhtml";
        }
        
        return pageStack.peek();
    }
    
    public boolean hasRightByName(String rightName) {
        try {
            UserRight right =  UserRight.valueOf(rightName);
            return sessionContext.getUser().hasRight(right);
        } catch(Exception e) {
            return false;
        }
    }
    
    public String getDisplayName() {
        if(sessionContext.getUser().getGameAccount().isPresent()) {
            String name = sessionContext.getUser().getGameAccount().get().getDisplayName(); 
            return (name == null || name.isEmpty()) ? sessionContext.getUser().getUsername() : name;
        }
        return sessionContext.getUser().getUsername();
    }
    
}
