package permlib.examples;

import java.util.HashMap;
import java.util.Random;
import permlib.utilities.BTree;

/**
 * Build random binary trees (mainly to build random Dyck paths!)
 *
 * @author MichaelAlbert
 */
public class RandomBTree {

    /**
     * @param args the command line arguments
     */
    static Random R = new Random();
    static boolean LEFT = true;
    static boolean RIGHT = false;

    public static void main(String[] args) {
        HashMap<BTree, Integer> m = new HashMap<>();
        for(int i = 0; i < 100; i++) {
            BTree t = buildTree(4000);
            System.out.println(i);
            if (!m.containsKey(t)) {
                m.put(t, 0);
            }
            m.put(t, m.get(t)+1);
        }
        for(BTree t : m.keySet()) {
            System.out.println(BTree.dyckStringBuffer(t) + " " + m.get(t));
        }
        

    }
    
    static BTree buildTree(int size) {
        BTree result = null;
        for(int i = 0; i < size; i++) result = extendOnce(result);
        return result;
    }

    static BTree extendOnce(BTree t) {
        return extendOnce(t, R.nextInt(2 * BTree.getSize(t) + 1), R.nextBoolean());
    }

    private static BTree extendOnce(BTree t, int e, boolean side) {
        BTree result = new BTree();
        if (t == null) {
            return result;
        }
        if (e == 0) {
            if (side == LEFT) {
                result.setRight(t);
            } else {
                result.setLeft(t);
            }
            return result;
        }
        e--;
        if (e < 2 * BTree.getSize(t.getLeft()) + 1) {
            result.setRight(t.getRight());
            result.setLeft(extendOnce(t.getLeft(), e, side));
            return result;
        }
        e -= 2 * BTree.getSize(t.getLeft()) + 1;
        result.setLeft(t.getLeft());
        result.setRight(extendOnce(t.getRight(), e, side));
        return result;
    }

}
