package permlib.arches;

/**
 *
 * @author Michael Albert
 */
public class Arch {
    
    static int nextID = 0;
    int id;
    
    End left;
    End right;
    
    public Arch() {
        this.id = nextID;
        nextID++;
        this.left = new End(this.id);
        this.right = new End(this.id);
    }
    
}
