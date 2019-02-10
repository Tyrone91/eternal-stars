package eternal.user;

public interface UserInteraction {
    
    public UserRight getNeededRight();
    public boolean isAllowedToPerform(User user);
    public void performAction(User user);
}
