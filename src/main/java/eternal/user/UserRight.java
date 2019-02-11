package eternal.user;

public enum UserRight {
    
    ADMIN_CONTROLS("admin"),
    
    LOGIN("login"),
    REGISTER("register"),
    DELETE_OWN_ACCOUNT("delete-user(self)"),
    EDIT_OWN_ACCOUNT("edit-user(self)"),
    
    RESEARCH("research"),
    BUILDING("build"),
    ATTACK("attack"),
    CHAT("chat"),
    
    USER_MANAGMENT_VIEW_ALL_RIGHTS("view-rights"),
    
    USER_MANAGMENT_VIEW_ALL_USERS("view-users"),
    USER_MANAGMENT_VIEW_ALL_ROLES("view-roles"),
    
    USER_MANAGMENT_EDIT_ALL_USERS("edit-user(all)"),
    USER_MANAGMENT_EDIT_ALL_ROLES("edit-role"),
    
    USER_MANAGMENT_DELETE_ROLE("delete-role"),
    USER_MANAGMENT_DELETE_USER("delete-user(any)");
    
    private String shorttext;
    
    UserRight(String shorttext) {
        this.shorttext = shorttext;
    }
    
    @Override
    public String toString() {
        return this.shorttext;
    }
    
}
