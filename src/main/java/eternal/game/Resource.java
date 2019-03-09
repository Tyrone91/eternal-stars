package eternal.game;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Resource {
    
    @Id
    @GeneratedValue
    private int id;
    
    private String type;
    private long amount;
    
    public Resource() {}
    
    public Resource(String type, long amount) {
        this.type = type;
        this.amount = amount;
    }
    
    public Resource(Resource source) {
        this(source.getType(), source.getAmount());
    }
    

    public String getType() {
        return type;
    }


    public long getAmount() {
        return amount;
    }
    
    protected Resource amount(long inc) {
        if(inc > 0) {
            if(Long.MAX_VALUE - inc < this.amount) {
                this.amount = Long.MAX_VALUE;
            } else {                    
                this.amount += inc;
            }
        } else {
            this.amount += inc;
        }
        
        if(amount < 0) {
            amount = 0;
        }
        return this;
    }
    
    protected Resource amount(Resource inc) {
        return amount(inc, false);
     }
    
    protected Resource amount(Resource inc, boolean sub) {
        if(sub) {
            return amount(-inc.getAmount());
        }
       return amount(inc.getAmount());
    }
    
}