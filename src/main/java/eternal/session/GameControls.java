package eternal.session;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class GameControls implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private static List<Control> ALL_CONTROLS = Arrays.asList(
            new Control("Overview", "game-overview.xhtml"),
            new Control("Buildings", "game-building.xhtml"),
            new Control("Research", "game-research.xhtml"),
            new Control("Fleet", "game-fleet.xhtml")
            );
    
    private Control currentFocus;
    
    @PostConstruct
    public void init() {
        currentFocus = ALL_CONTROLS.get(0);
    }
    
    public Control getCurrentFocus() {
        return currentFocus;
    }

    public void setCurrentFocus(Control currentFocus) {
        this.currentFocus = currentFocus;
    }
    
    public List<Control> getControls() {
        return ALL_CONTROLS;
    }

    public static class Control {
        
        private String label;
        private String outcome;
        
        public Control(String label, String outcome) {
            this.setLabel(label);
            this.setOutcome(outcome);
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getOutcome() {
            return outcome;
        }

        public void setOutcome(String outcome) {
            this.outcome = outcome;
        }
    }
}
