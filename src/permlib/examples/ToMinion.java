package permlib.examples;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;
import permlib.property.AvoidanceTest;
import permlib.property.HereditaryProperty;

/**
 * Generate some output for use in minion. Start with the S3-S4 covers.
 *
 * @author Michael Albert
 */
public class ToMinion {

  public static void rand4to6(int samples) {
    System.out.println("MINION 3");
    System.out.println("**VARIABLES**");
    System.out.println("BOOL x[720]");
    System.out.println("**SEARCH**");
    System.out.println("PRINT [x]");
    System.out.print("VARORDER [");
    HashMap<Permutation, String> varNames = new HashMap<>();
    int i = 0;
    for (Permutation p : new Permutations(6)) {
      varNames.put(p, "x[" + i + "]");
      System.out.print(("x[" + i + "],"));
      System.out.println("# " + p);
      i++;
    }
    System.out.print("VALORDER [");
    for (i = 0; i < 720; i++) {
      System.out.print("d,");
    }
    System.out.println("]\n**CONSTRAINTS**");

    HashMap<Permutation, HashSet<Permutation>> c36 = new HashMap<>();

    HashMap<Permutation, HashSet<Permutation>> c46 = new HashMap<>();
    for (Permutation p : new Permutations(4)) {
      c46.put(p, new HashSet<Permutation>());
    }
    for (Permutation p : new Permutations(3)) {
      c36.put(p, new HashSet<Permutation>());
    }
    for (Permutation p : new Permutations(6)) {
      for (Permutation q : PermUtilities.deletions(p)) {
        for (Permutation r : PermUtilities.deletions(q)) {
          c46.get(r).add(p);
          for (Permutation s : PermUtilities.deletions(r)) {
            c36.get(s).add(p);
          }
        }
      }
    }
    for (Permutation p : new Permutations(3)) {
      for (Permutation q : new Permutations(3)) {
        if (p.compareTo(q) >= 0) {
          continue;
        }
        HashSet<Permutation> cp = new HashSet<>(c36.get(p));
        HashSet<Permutation> cq = new HashSet<>(c36.get(q));
        HashSet<Permutation> tp = new HashSet<>(cp);
        cp.removeAll(cq);
        cq.removeAll(tp);
        if (cp.size() + cq.size() > 0) {
          StringBuilder weights = new StringBuilder();
          StringBuilder vars = new StringBuilder();
          weights.append("[");
          vars.append("[");
          for (Permutation cpp : cp) {
            weights.append("1,");
            vars.append(varNames.get(cpp) + ",");
          }
          for (Permutation cqq : cq) {
            weights.append("-1,");
            vars.append(varNames.get(cqq) + ",");
          }
          weights.deleteCharAt(weights.length() - 1);
          vars.deleteCharAt(vars.length() - 1);
          weights.append("]");
          vars.append("]");
          System.out.println("weightedsumgeq(" + weights + "," + vars + ",0)");
          System.out.println("weightedsumleq(" + weights + "," + vars + ",0)");
        }
      }
    }
    HashSet<Permutation> r4 = new HashSet<>();
    for (i = 0; i < samples; i++) {
      r4.add(PermUtilities.randomPermutation(4));
    }
    for (Permutation p : new Permutations(4)) {
      for (Permutation q : new Permutations(4)) {
        if (p.compareTo(q) >= 0 || !r4.contains(q) || !r4.contains(p)) {
          continue;
        }
        HashSet<Permutation> cp = new HashSet<>(c46.get(p));
        HashSet<Permutation> cq = new HashSet<>(c46.get(q));
        HashSet<Permutation> tp = new HashSet<>(cp);
        cp.removeAll(cq);
        cq.removeAll(tp);
        if (cp.size() + cq.size() > 0) {
          StringBuilder weights = new StringBuilder();

          StringBuilder vars = new StringBuilder();
          weights.append("[");
          vars.append("[");
          System.out.println("# " + p + " " + cp.size() + " " + q + " " + cq.size());
          for (Permutation cpp : cp) {
            weights.append("1,");
            vars.append(varNames.get(cpp) + ",");
          }
          for (Permutation cqq : cq) {
            weights.append("-1,");
            vars.append(varNames.get(cqq) + ",");
          }
          weights.deleteCharAt(weights.length() - 1);
          vars.deleteCharAt(vars.length() - 1);
          weights.append("]");
          vars.append("]");
          System.out.println("weightedsumgeq(" + weights + "," + vars + ",0)");
          System.out.println("weightedsumleq(" + weights + "," + vars + ",0)");
        }
      }
    }

  }

  public static void s34to6() {
    System.out.println("MINION 3");
    System.out.println("**VARIABLES");
    System.out.println("BOOL x[720]");
    System.out.println("**SEARCH**");
    System.out.println("PRINT [x]");
    System.out.print("VARORDER [");
    HashMap<Permutation, String> varNames = new HashMap<>();
    int i = 0;
    for (Permutation p : new Permutations(6)) {
      varNames.put(p, "x[" + i + "]");
//      System.out.print("BOOL ");
      System.out.print(("x[" + i + "],"));
      System.out.println("# " + p);
      i++;
    }
    System.out.print("VALORDER [");
    for (i = 0; i < 720; i++) {
      System.out.print("d,");
    }
    System.out.println("]\n**CONSTRAINTS**");

    HashMap<Permutation, HashSet<Permutation>> c36 = new HashMap<>();

    HashMap<Permutation, HashSet<Permutation>> c46 = new HashMap<>();
    for (Permutation p : new Permutations(4)) {
      c46.put(p, new HashSet<Permutation>());
    }
    for (Permutation p : new Permutations(3)) {
      c36.put(p, new HashSet<Permutation>());
    }
    for (Permutation p : new Permutations(6)) {
      for (Permutation q : PermUtilities.deletions(p)) {
        for (Permutation r : PermUtilities.deletions(q)) {
          c46.get(r).add(p);
          for (Permutation s : PermUtilities.deletions(r)) {
            c36.get(s).add(p);
          }
        }
      }
    }
    for (Permutation p : new Permutations(3)) {
      for (Permutation q : new Permutations(3)) {
        if (p.compareTo(q) >= 0) {
          continue;
        }
        HashSet<Permutation> cp = new HashSet<>(c36.get(p));
        HashSet<Permutation> cq = new HashSet<>(c36.get(q));
        HashSet<Permutation> tp = new HashSet<>(cp);
        cp.removeAll(cq);
        cq.removeAll(tp);
        if (cp.size() + cq.size() > 0) {
          StringBuilder weights = new StringBuilder();
          StringBuilder vars = new StringBuilder();
          weights.append("[");
          vars.append("[");
          for (Permutation cpp : cp) {
            weights.append("1,");
            vars.append(varNames.get(cpp) + ",");
          }
          for (Permutation cqq : cq) {
            weights.append("-1,");
            vars.append(varNames.get(cqq) + ",");
          }
          weights.deleteCharAt(weights.length() - 1);
          vars.deleteCharAt(vars.length() - 1);
          weights.append("]");
          vars.append("]");
          System.out.println("weightedsumgeq(" + weights + "," + vars + ",0)");
          System.out.println("weightedsumleq(" + weights + "," + vars + ",0)");
        }
      }
    }
    for (Permutation p : new Permutations(4)) {
      for (Permutation q : new Permutations(4)) {
        if (p.compareTo(q) >= 0) {
          continue;
        }
        HashSet<Permutation> cp = new HashSet<>(c46.get(p));
        HashSet<Permutation> cq = new HashSet<>(c46.get(q));
        HashSet<Permutation> tp = new HashSet<>(cp);
        cp.removeAll(cq);
        cq.removeAll(tp);
        if (cp.size() + cq.size() > 0) {
          StringBuilder weights = new StringBuilder();

          StringBuilder vars = new StringBuilder();
          weights.append("[");
          vars.append("[");
          System.out.println("# " + p + " " + cp.size() + " " + q + " " + cq.size());
          for (Permutation cpp : cp) {
            weights.append("1,");
            vars.append(varNames.get(cpp) + ",");
          }
          for (Permutation cqq : cq) {
            weights.append("-1,");
            vars.append(varNames.get(cqq) + ",");
          }
          weights.deleteCharAt(weights.length() - 1);
          vars.deleteCharAt(vars.length() - 1);
          weights.append("]");
          vars.append("]");
          System.out.println("weightedsumgeq(" + weights + "," + vars + ",0)");
          System.out.println("weightedsumleq(" + weights + "," + vars + ",0)");
        }
      }
    }
    System.out.println("**EOF**");
  }

  public static void whichPerms() {
    PermutationClass theClass = new PermutationClass("2143", "3421", "4123");
    HashSet<Permutation> classElements = new HashSet<>();
    for (Permutation p : new Permutations(theClass, 5)) {
      classElements.add(p);
    }
    Permutation[] ce = classElements.toArray(new Permutation[0]);
    int classSize = ce.length;
    Arrays.sort(ce);
    Scanner s = new Scanner("1 1 1 1 1 0 0 1 1 0 1 0 1 0 0 0 1 1 0 1 0 0 0 0 0 0 0 1 0 0 1 1 1 1 1 0 1 1 1 1 1 1 1 0 1 0 1 0 1 0 1 1 1 0 1 1 1 1 1 1 1 0 1 1 0 0 0 0 1");
    for (Permutation p : ce) {
      if (s.nextInt() == 0) {
        System.out.println(p);
      }
    }
  }

  public static void main(String[] args) {
    // PermutationClass c = new PermutationClass("2143", "3421", "4123");
    //,"12543","13245","13524","14235","14325","14352","14523","15342","15432","21345","23145","23415","23451","23514","23541","24315","24351","31245","34512","35412","41352","42315","43152","53214","54132","54213","54231","54312");
    // doClass(c,4,5);
    doClass(new PermutationClass(new HashSet<Permutation>()), 4, 5);
    // makeFiles();
//whichPerms();
  }

  public static void foo() {
    for (int i = 0; i < 120; i++) {
      System.out.print("x[" + i + "],");
    }
  }

  public static void doClass(PermutationClass theClass, int toLength, int atLength) {
    StringBuilder model = new StringBuilder();
    model.append("MINION 3\n");
    HashSet<Permutation> classElements = new HashSet<>();
    for (Permutation p : new Permutations(theClass, atLength)) {
      classElements.add(p);
    }
    Permutation[] ce = classElements.toArray(new Permutation[0]);
    int classSize = ce.length;
    Arrays.sort(ce);
    model.append("**VARIABLES**\n");
    model.append("BOOL x[" + classSize + "]\n");
    model.append("**SEARCH**\n");
    model.append("PRINT [x]\n");
    model.append("VARORDER [");
    HashMap<Permutation, String> varNames = new HashMap<>();
    int i = 0;
    for (Permutation p : ce) {
      varNames.put(p, "x[" + i + "]");
      model.append(("x[" + i + "]"));
      if (i < classSize - 1) {
        model.append(", # " + p + "\n");
      } else {
        model.append("  # " + p + "\n");
      }
      i++;
    }
    model.append("]\n");
    model.append("**CONSTRAINTS**\n");
    for (int k = 2; k <= toLength; k++) {
      for (Permutation p : new Permutations(theClass, k)) {
        HashSet<Permutation> cp = covers(p, theClass, atLength);
        for (Permutation q : new Permutations(theClass, k)) {
          if (p.compareTo(q) >= 0) {
            continue;
          }
          HashSet<Permutation> cq = covers(q, theClass, atLength);
          HashSet<Permutation> cpc = new HashSet<>(cp);
          HashSet<Permutation> cqc = new HashSet<>(cq);
          cpc.removeAll(cq);
          cqc.removeAll(cp);
          if (cpc.size() + cqc.size() > 0) {
            StringBuilder weights = new StringBuilder();
            StringBuilder vars = new StringBuilder();
            weights.append("[");
            vars.append("[");
            // System.out.println("# " + p + " " + cp.size() + " " + q + " " + cq.size());
            for (Permutation cpp : cpc) {
              weights.append("1,");
              vars.append(varNames.get(cpp) + ",");
            }
            for (Permutation cqq : cqc) {
              weights.append("-1,");
              vars.append(varNames.get(cqq) + ",");
            }
            weights.deleteCharAt(weights.length() - 1);
            vars.deleteCharAt(vars.length() - 1);
            weights.append("]");
            vars.append("]");
            model.append("weightedsumgeq(" + weights + "," + vars + ",0)\n");
            model.append("weightedsumleq(" + weights + "," + vars + ",0)\n");
          }
        }
      }
    }
    model.append("**EOF**");
    System.out.println(model);
  }

  private static HashSet<Permutation> covers(Permutation p, PermutationClass theClass, int length) {
    HashSet<Permutation> result = new HashSet<>();
    HereditaryProperty ap = AvoidanceTest.getTest(p);
    for (Permutation q : new Permutations(theClass, length)) {
      if (!ap.isSatisfiedBy(q)) {
        result.add(q);
      }
    }
    return result;
  }

  private static void makeFiles() {
    HashSet<Permutation> b4 = new HashSet<>();
//    b4.add(new Permutation("2143"));
//    b4.add(new Permutation("3421"));
//    b4.add(new Permutation("4123"));
    PermutationClass theClass = new PermutationClass(b4);
    HashSet<Permutation> classElements = new HashSet<>();
    for (Permutation p : new Permutations(theClass, 5)) {
      classElements.add(p);
    }
    Permutation[] ce = classElements.toArray(new Permutation[0]);
    Arrays.sort(ce);
    System.out.println(ce.length);
    try {
      Scanner in = new Scanner(new BufferedReader(new FileReader("/Users/MichaelAlbert/Documents/minion-1.8/perm/c5s.txt")));
      int count = 0;
      System.out.println(count);
      while (in.hasNextLine()) {
        String line = in.nextLine();
        System.out.println(line);
        Scanner nextClass = new Scanner(line);
        HashSet<Permutation> basis = new HashSet<>();
        basis.addAll(b4);
        for (Permutation p : ce) {
          if (nextClass.nextInt() == 0) {
            basis.add(p);
          }
        }
        System.out.println(basis.size());
        String outCount = "000000" + count;
        outCount = outCount.substring(outCount.length()-4);
        doClass(new PermutationClass(basis), 5, 6, "/Users/MichaelAlbert/Documents/minion-1.8/perm/c5/C" + outCount);
        count++;
      }
    } catch (Exception ex) {
      System.out.println(ex);
    }
  }

  private static void doClass(PermutationClass theClass, int toLength, int atLength, String outFile) {
    StringBuilder model = new StringBuilder();
    model.append("MINION 3\n");
    HashSet<Permutation> classElements = new HashSet<>();
    for (Permutation p : new Permutations(theClass, atLength)) {
      classElements.add(p);
    }
    Permutation[] ce = classElements.toArray(new Permutation[0]);
    int classSize = ce.length;
    Arrays.sort(ce);
    model.append("**VARIABLES**\n");
    model.append("BOOL x[" + classSize + "]\n");
    model.append("**SEARCH**\n");
    model.append("PRINT [x]\n");
    model.append("VARORDER [");
    HashMap<Permutation, String> varNames = new HashMap<>();
    int i = 0;
    for (Permutation p : ce) {
      varNames.put(p, "x[" + i + "]");
      model.append(("x[" + i + "]"));
      if (i < classSize - 1) {
        model.append(", # " + p + "\n");
      } else {
        model.append("  # " + p + "\n");
      }
      i++;
    }
    model.append("]\n");
    model.append("**CONSTRAINTS**\n");
    model.append("sumgeq([x[0],x["+(ce.length-1)+"]],2)\n");
    for (int k = 2; k <= toLength; k++) {
      for (Permutation p : new Permutations(theClass, k)) {
        HashSet<Permutation> cp = covers(p, theClass, atLength);
        for (Permutation q : new Permutations(theClass, k)) {
          if (p.compareTo(q) >= 0) {
            continue;
          }
          HashSet<Permutation> cq = covers(q, theClass, atLength);
          HashSet<Permutation> cpc = new HashSet<>(cp);
          HashSet<Permutation> cqc = new HashSet<>(cq);
          cpc.removeAll(cq);
          cqc.removeAll(cp);
          if (cpc.size() + cqc.size() > 0) {
            StringBuilder weights = new StringBuilder();
            StringBuilder vars = new StringBuilder();
            weights.append("[");
            vars.append("[");
            // System.out.println("# " + p + " " + cp.size() + " " + q + " " + cq.size());
            for (Permutation cpp : cpc) {
              weights.append("1,");
              vars.append(varNames.get(cpp) + ",");
            }
            for (Permutation cqq : cqc) {
              weights.append("-1,");
              vars.append(varNames.get(cqq) + ",");
            }
            weights.deleteCharAt(weights.length() - 1);
            vars.deleteCharAt(vars.length() - 1);
            weights.append("]");
            vars.append("]");
            model.append("weightedsumgeq(" + weights + "," + vars + ",0)\n");
            model.append("weightedsumleq(" + weights + "," + vars + ",0)\n");
          }
        }
      }
    }
    model.append("**EOF**\n");
    BufferedWriter writer;
    try {
      writer = new BufferedWriter(new FileWriter(outFile));
      writer.write(model.toString());
      writer.flush();
      writer.close();
    } catch (IOException ex) {
      System.out.println("More oops");
    }

  }

}
