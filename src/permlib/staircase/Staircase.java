package permlib.staircase;

import java.util.ArrayList;
import java.util.Arrays;
import permlib.utilities.IntPair;
import permlib.Permutation;
import permlib.PermUtilities;
import permlib.property.HereditaryProperty;

/**
 * Representation of 321 avoiding permutations by staircase gridding
 * @author Michael Albert
 */
public class Staircase {

    static final HereditaryProperty A321 = PermUtilities.avoidanceTest("321");
    static final int NOT_FOUND = -1;
    static final int DX = 10;
    static final int DY = 10;
    static final int unit = 1;

    int[] blockEncoding;
    
    public Staircase(int[] blockEncoding) {
        this.blockEncoding = blockEncoding;
    }

    public static Staircase genericStaircase(int blocks, int blockSize) {
        int[] blockEncoding = new int[blocks * blockSize];
        int index = 0;
        for (int i = 0; i < blockSize; i++) {
            for (int j = blocks - 1; j >= 0; j--) {
                blockEncoding[index++] = j;
            }
        }
        return new Staircase(blockEncoding);
    }

    public static Staircase randomStaircase(int blocks, int length) {
        int[] blockEncoding = new int[length];
        blockEncoding[0] = (int) (Math.random() * blocks);
        for (int i = 1; i < length; i++) {
            // blockEncoding[i] = (int) (Math.random() * blocks);
            int trialValue = (int) (Math.random() * blocks);
            while (trialValue == blockEncoding[i-1] || trialValue < blockEncoding[i-1]-1) {
                trialValue = (int) (Math.random() * blocks);
            }
            blockEncoding[i] = trialValue;
        }
        return new Staircase(blockEncoding);
    }
    
    public static Staircase randBalStaircase(int blocks, int blockSize) {
        int[] blockEncoding = new int[blocks*blockSize];
        for(int i = 0; i < blocks; i++) {
            for(int j = 0; j < blockSize; j++) {
                blockEncoding[i*blockSize + j] = i;
            }
        }
        shuffle(blockEncoding);
        return new Staircase(blockEncoding);
    }
    

    public Permutation toPermutation() {
        IntPair[] points = new IntPair[blockEncoding.length];
        for (int i = 0; i < blockEncoding.length; i++) {
            points[i] = new IntPair(
                    i + 10 * blockEncoding.length * ((blockEncoding[i] + 1) / 2),
                    i + 10 * blockEncoding.length * ((blockEncoding[i]) / 2));
        }
        Arrays.sort(points);
        int[] elements = new int[points.length];
        for (int i = 0; i < points.length; i++) {
            elements[i] = points[i].getSecond();
        }
        return new Permutation(elements);
    }

    public int[] firstRivets(int fromBlock) {
        int[] result = new int[fromBlock + 1];
        for (int i = 0; i < result.length; i++) {
            result[i] = NOT_FOUND;
        }
        int currentBlock = fromBlock;
        for (int i = 0; currentBlock >= 0 && i < blockEncoding.length; i++) {
            if (blockEncoding[i] == currentBlock) {
                result[currentBlock] = i;
                currentBlock--;
            }
        }
        return result;
    }

    public int[] nextRivets(int[] previousRivets) {
        int[] result = new int[previousRivets.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = NOT_FOUND;
        }
        int currentBlock = previousRivets.length - 1;
        int currentIndex = 0;
        while (currentBlock >= 0 && currentIndex < blockEncoding.length) {
            if (currentBlock > 0) {
                currentIndex = (currentIndex > previousRivets[currentBlock - 1]) ? currentIndex : previousRivets[currentBlock - 1] + 1;
            }
            while (currentIndex < blockEncoding.length && blockEncoding[currentIndex] != currentBlock) {
                currentIndex++;
            }
            if (currentIndex < blockEncoding.length) {
                result[currentBlock] = currentIndex;
                currentBlock--;
            }
        }
        return result;
    }

    public ArrayList<int[]> getRivets(int fromBlock) {
        ArrayList<int[]> result = new ArrayList<int[]>();
        int[] rivets = firstRivets(fromBlock);
        if (rivets[0] == NOT_FOUND) {
            return result;
        }
        result.add(rivets);
        while (true) {
            rivets = nextRivets(rivets);
            if (rivets[0] == NOT_FOUND) {
                return result;
            }
            result.add(rivets);
        }
    }

    public String rivetsToPSTricks(int fromBlock, int maxBlock) {
        StringBuilder result = new StringBuilder();
        ArrayList<int[]> rivetsList = getRivets(fromBlock);
        for (int[] r : rivetsList) {
            for (int i : r) {
                int lx = ((blockEncoding[i] + 1) / 2) * blockEncoding.length * DX;
                int ly = ((blockEncoding[i]) / 2) * blockEncoding.length * DY;
                result.append("\\pscircle*");
                result.append("(" + (lx + i * DX + DX / 2) + "," + (ly + i * DY + DY / 2) + "){2}\n");
                int vertLineBottom = ly;
                int vertLineTop = ly + blockEncoding.length * DY;
                int horizLineLeft = lx;
                int horizLineRight = lx + blockEncoding.length * DX;
                if (blockEncoding[i] % 2 == 0) {
                    if (blockEncoding[i] > 0) {
                        vertLineBottom -= blockEncoding.length * DY;
                    }
                    if (blockEncoding[i] < maxBlock) {
                        horizLineRight += blockEncoding.length * DX;
                    }
                } else {
                    if (blockEncoding[i] < maxBlock) {
                        vertLineTop += blockEncoding.length * DY;
                    }
                    horizLineLeft -= blockEncoding.length * DX;
                }
                result.append("\\psline[linestyle=dotted]");
                result.append("(" + horizLineLeft + "," + (ly + i * DY + DY / 2) + ")");
                result.append("(" + horizLineRight + "," + (ly + i * DY + DY / 2) + ")\n");
                result.append("\\psline[linestyle=dotted]");
                result.append("(" + (lx + i * DX + DX / 2) + "," + vertLineBottom + ")");
                result.append("(" + (lx + i * DX + DX / 2) + "," + vertLineTop + ")\n");
                

            }
        }
        return result.toString();
    }

    public String toPSTricks(int blocks) {
        StringBuilder result = new StringBuilder();
        result.append("\\psset{" + "xunit=" + unit
                + "pt, " + "yunit=" + unit + "pt, " + "runit=" + unit + "pt" + "}\n"
                + "\\begin{pspicture}(0,0)" + "(" + ((blocks + 2) / 2) * blockEncoding.length * DX + ","
                + ((blocks + 1) / 2) * blockEncoding.length * DY + ")\n");
        for (int i = 0; i < blocks; i++) {
            int lx = ((i + 1) / 2) * blockEncoding.length * DX;
            int ly = ((i) / 2) * blockEncoding.length * DY;
            int ux = lx + blockEncoding.length * DX;
            int uy = ly + blockEncoding.length * DY;
            result.append("\\pspolygon[linewidth=1.5pt]");
            result.append("(" + lx + "," + ly + ")");
            result.append("(" + ux + "," + ly + ")");
            result.append("(" + ux + "," + uy + ")");
            result.append("(" + lx + "," + uy + ")");
            result.append("\n");
        }
        for (int i = 0; i < blockEncoding.length; i++) {
            int lx = ((blockEncoding[i] + 1) / 2) * blockEncoding.length * DX;
            int ly = ((blockEncoding[i]) / 2) * blockEncoding.length * DY;
            result.append("\\pscircle*");
            result.append("(" + (lx + i * DX + DX / 2) + "," + (ly + i * DY + DY / 2) + "){1}\n");
        }
        result.append("\\end{pspicture}");
        return result.toString();
    }

    public static void main(String[] args) {
        Staircase s = new Staircase(new int[]{0, 0, 0});
        System.out.println(s.toPermutation());
        s = new Staircase(new int[]{3, 2, 1, 0, 1, 2, 3});
        System.out.println(s.toPermutation());
        s = genericStaircase(4, 3);
        System.out.println(s.toPermutation());
        int[] rivets = s.firstRivets(2);
        System.out.println(Arrays.toString(rivets));
        rivets = s.nextRivets(rivets);
        System.out.println(Arrays.toString(rivets));
        s = randBalStaircase(4, 10);
        System.out.println(Arrays.toString(s.blockEncoding));
        for (int[] r : s.getRivets(3)) {
            System.out.println(Arrays.toString(r));
        }
        System.out.println(s.toPSTricks(4));
        System.out.println(s.rivetsToPSTricks(2,3));
    }
    
    private static void shuffle(int[] a) {
    
        for(int i = 0; i < a.length; i++) {
            int j = (int) (Math.random()*(i+1));
            int t = a[i];
            a[i] = a[j];
            a[j] = t;
        }
    }
    
}
