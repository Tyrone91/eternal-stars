package eternal.game;

public class Resource {
    
    private long amount;
    
    protected Resource(long initial) {
        amount = initial;
    }
    
    protected void add(long val) {
        if(val > 0) {
            if(Long.MAX_VALUE - val < this.amount) {
                this.amount = Long.MAX_VALUE;
            } else {                    
                this.amount += val;
            }
        } else {
            this.amount += val;
        }
        
        if(amount < 0) {
            amount = 0;
        }
    }
    
    protected void sub(long val) {
        add(-val);
    }
    
    public long val() {
        return amount;
    }
    
    protected boolean isLessThen(Resource res) {
        return this.val() < res.val();
    }
    
    protected boolean isMoreThen(Resource res) {
        return res.isLessThen(this);
    }
    
    protected boolean isEqualTo(Resource res) {
        return !isLessThen(res) && !isMoreThen(res);
    }
    
    protected boolean isMoreOrEqualThen(Resource res) {
        return isMoreThen(res) || isEqualTo(res);
    }
    
    protected boolean isLessOrEqualThen(Resource res) {
        return isLessThen(res) || isEqualTo(res);
    }
    
    
}