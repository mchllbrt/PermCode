package permlib.examples;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Objects;
import permlib.Permutation;

/**
 * Mark up a 321-avoiding permutation in the manner required for implementing
 * the involvement/avoidance algorithms of ALLV
 * https://arxiv.org/pdf/1510.06051.pdf
 *
 * @author Michael Albert
 */
public class MarkedAv321 {

  Permutation p;
  Permutation pInverse;

  int[] leftLower;
  int[] leftUpper;
  int[] rightLower;
  int[] rightUpper;
  int[] upLower;
  int[] upUpper;
  int[] downLower;
  int[] downUpper;
  boolean[] lowerChain;
  boolean[] upperChain;
  int[] upperIndices;
  int[] lowerIndices;

  private static final int UNDEFINED = -1;

  public MarkedAv321(Permutation p) {
    this.p = p;
    this.pInverse = p.inverse();
    buildArrays();
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 97 * hash + Objects.hashCode(this.p);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final MarkedAv321 other = (MarkedAv321) obj;
    if (!Objects.equals(this.p, other.p)) {
      return false;
    }
    return true;
  }

  private void buildArrays() {
    leftLower = new int[p.elements.length];
    leftUpper = new int[p.elements.length];
    rightLower = new int[p.elements.length];
    rightUpper = new int[p.elements.length];
    upLower = new int[p.elements.length];
    upUpper = new int[p.elements.length];
    downLower = new int[p.elements.length];
    downUpper = new int[p.elements.length];
    lowerChain = new boolean[p.elements.length];
    upperChain = new boolean[p.elements.length];

    int lrMax = Integer.MIN_VALUE;
    int rlMin = Integer.MAX_VALUE;
    for (int i = 0; i < p.elements.length; i++) {
      lrMax = (p.elements[i] > lrMax) ? p.elements[i] : lrMax;
      lowerChain[i] = p.elements[i] < lrMax;
    }
    for (int i = p.elements.length - 1; i >= 0; i--) {
      rlMin = (p.elements[i] < rlMin) ? p.elements[i] : rlMin;
      upperChain[i] = p.elements[i] > rlMin;
    }

    Arrays.fill(leftLower, UNDEFINED);
    Arrays.fill(leftUpper, UNDEFINED);
    int lastLower = UNDEFINED;
    int lastUpper = UNDEFINED;
    for (int i = 0; i < p.elements.length; i++) {
      leftLower[i] = lastLower;
      leftUpper[i] = lastUpper;
      if (lowerChain[i]) {
        lastLower = i;
      }
      if (upperChain[i]) {
        lastUpper = i;
      }
    }

    Arrays.fill(rightLower, UNDEFINED);
    Arrays.fill(rightUpper, UNDEFINED);
    int nextLower = UNDEFINED;
    int nextUpper = UNDEFINED;
    for (int i = p.elements.length - 1; i >= 0; i--) {
      rightLower[i] = nextLower;
      rightUpper[i] = nextUpper;
      if (lowerChain[i]) {
        nextLower = i;
      }
      if (upperChain[i]) {
        nextUpper = i;
      }
    }

    Arrays.fill(downLower, UNDEFINED);
    Arrays.fill(downUpper, UNDEFINED);
    int maxLowerIndex = UNDEFINED;
    int maxUpperIndex = UNDEFINED;
    for (int i = 0; i < pInverse.elements.length; i++) {
      int j = pInverse.elements[i];
      downLower[j] = maxLowerIndex;
      downUpper[j] = maxUpperIndex;
      if (lowerChain[j]) {
        maxLowerIndex = j;
      }
      if (upperChain[j]) {
        maxUpperIndex = j;
      }

    }

    Arrays.fill(upLower, UNDEFINED);

    Arrays.fill(upUpper, UNDEFINED);
    int minLowerIndex = UNDEFINED;
    int minUpperIndex = UNDEFINED;
    for (int i = pInverse.elements.length - 1;
            i >= 0; i--) {
      upLower[pInverse.elements[i]] = minLowerIndex;
      upUpper[pInverse.elements[i]] = minUpperIndex;
      if (lowerChain[pInverse.elements[i]]) {
        minLowerIndex = pInverse.elements[i];
      }
      if (upperChain[pInverse.elements[i]]) {
        minUpperIndex = pInverse.elements[i];
      }
    }

  }

  Type type(int index) {
    if (lowerChain[index]) {
      return Type.L;
    }
    if (upperChain[index]) {
      return Type.U;
    }
    return Type.F;
  }

  int[] findEmbedding(MarkedAv321 text) {
    int[] f = initialMap(text);
    System.out.println(Arrays.toString(f));
    boolean[] problems = new boolean[p.elements.length];
    ArrayDeque<Integer> problemQ = new ArrayDeque<>();
    for (int i = 0; i < p.elements.length; i++) {
      problems[i] = isProblem(i, f, text);
      if (problems[i]) {
        System.out.println(i);
        problemQ.add(i);
      }
    }
    while (!problemQ.isEmpty()) {
      int x = problemQ.poll();
      problems[x] = false;
      System.out.println("Fixing " + x);
      boolean fixed = fixProblem(x, f, text);
      if (!fixed) {
        System.out.println("Not fixed!");
        Arrays.fill(f, UNDEFINED);
        return f;
      }
      System.out.println(Arrays.toString(f));
      int xr = navigate(x, Direction.RIGHT);
      System.out.println("Checking xr=" + xr);
      if (xr != UNDEFINED) System.out.println("Known problem " + problems[xr]);
      if (xr != UNDEFINED && !problems[xr] && isProblem(xr, f, text)) {
        System.out.println("Problem at: xr=" + xr);
        problemQ.add(xr);
        problems[xr] = true;
      }
      int xu = navigate(x, Direction.UP);
      if (xu != UNDEFINED && !problems[xu] && isProblem(xu, f, text)) {
        System.out.println("Problem at: xu=" + xu);
        problemQ.add(xu);
        problems[xu] = true;
      }
      System.out.println(Arrays.toString(f));
    }
    return f;
  }

  private boolean isProblem(int x, int[] f, MarkedAv321 text) {

    int lhs = text.value(f[x]);
    int xl = navigate(x, Direction.LEFT);
    // System.out.println("xl " + x + " " + xl);
    if (xl != UNDEFINED) {
      int rhs = text.value(text.navigate(f[xl], Direction.RIGHT, type(x)));
      if (lhs < rhs) {
        return true;
      }
    }
    int xd = navigate(x, Direction.DOWN);
    // System.out.println("xd " + x + " " + xd);
    if (xd != UNDEFINED) {
      int rhs = text.value(text.navigate(f[xd], Direction.UP, type(x)));
      if (lhs < rhs) {
        return true;
      }
    }
    return false;
  }

  private int[] initialMap(MarkedAv321 text) {
    int[] f = new int[p.elements.length];
    int textIndex = 0;
    for (int i = 0; i < f.length; i++) {
      switch (type(i)) {
        case L:
          while (textIndex < text.p.elements.length && text.type(textIndex) != Type.L) {
            textIndex++;
          }
          f[i] = textIndex;
          textIndex++;
          break;
        case U:
          while (textIndex < text.p.elements.length && text.type(textIndex) != Type.U) {
            textIndex++;
          }
          f[i] = textIndex;
          textIndex++;
          break;
      }
      if (f[i] >= text.p.elements.length) {
        Arrays.fill(f, UNDEFINED);
        break;
      }
    }
    System.out.println("Initial f: " + Arrays.toString(f));
    return f;
  }

  public static void main(String[] args) {
    Permutation p = new Permutation("24153");
    System.out.println(p);
    MarkedAv321 pm = new MarkedAv321(p);
    Permutation t = new Permutation("2153746");
    System.out.println(t);
    MarkedAv321 tm = new MarkedAv321(t);
    pm.findEmbedding(tm);

  }

  private boolean fixProblem(int x, int[] f, MarkedAv321 text) {
    int value = UNDEFINED;
    int xl = navigate(x, Direction.LEFT);
    if (xl != UNDEFINED) {
      value = text.value(text.navigate(f[xl], Direction.RIGHT, type(x)));
      if (value == UNDEFINED) return false;
    }
    int xd = navigate(x, Direction.DOWN);
    if (xd != UNDEFINED) {
      int v = text.value(text.navigate(f[xd], Direction.UP, type(x)));
      if (v == UNDEFINED) return false;
      value = Math.max(value, v);
    }
    if (value != UNDEFINED) {
      f[x] = text.pInverse.elements[value];
      return true;
    } else {
      f[x] = UNDEFINED;
      return false;
    }

  }

  private static enum Type {
    U, L, F;
  }

  private static enum Direction {
    LEFT, RIGHT, UP, DOWN;
  }

  int value(int index) {
    if (0 < index && index < p.elements.length) {
      return p.elements[index];
    }
    return UNDEFINED;
  }

  int navigate(int index, Direction d) {
    switch (d) {
      case LEFT:
        if (index == 0) {
          return UNDEFINED;
        }
        return index - 1;
      case RIGHT:
        if (index == p.elements.length - 1) {
          return UNDEFINED;
        }
        return index + 1;
      case UP:
        if (p.elements[index] == p.elements.length - 1) {
          return UNDEFINED;
        }
        return pInverse.elements[p.elements[index] + 1];
      case DOWN:
        if (p.elements[index] == 0) {
          return UNDEFINED;
        }
        return pInverse.elements[p.elements[index] - 1];
      default:
        return UNDEFINED;
    }
  }

  int navigate(int index, Direction d, Type t) {
    if (index == UNDEFINED) return UNDEFINED;
    switch (d) {
      case RIGHT:
        switch (t) {
          case L:
            return rightLower[index];
          case U:
            return rightUpper[index];
          default:
            return UNDEFINED;
        }
      case UP:
        switch (t) {
          case L:
            return upLower[index];
          case U:
            return upUpper[index];
          default:
            return UNDEFINED;
        }
      default:
        return UNDEFINED;
    }
  }

}
