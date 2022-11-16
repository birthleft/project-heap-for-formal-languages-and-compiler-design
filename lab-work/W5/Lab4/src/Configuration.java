public class Configuration {
    public String state;
    public String sequence;
    public Configuration next;

    public Configuration(String state, String sequence) {
        this.state = state;
        this.sequence = sequence;
        this.next = null;
    }

    public void set(Configuration nextConfiguration){
        this.next = nextConfiguration;
    }

    public Configuration move(){
        return this.next;
    }

    @Override
    public String toString() {
        return "(" + this.state + ", " + (this.sequence.isEmpty() ? "ϵ" : this.sequence) + ")";
    }
}
