package permlib.arches;


public class End {
    
    End prev;
    End next;
    int id;
    
    public End(int id) {
        this.id = id;
    }
    
    public String toString() {
        return "" + id;
    }
    
}
