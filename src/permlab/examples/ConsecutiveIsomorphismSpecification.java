package permlab.examples;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import permlib.Permutation;
import permlib.Permutations;
import permlib.Symmetry;
import permlib.property.ConsecutiveAvoidanceTest;
import permlib.property.PermProperty;
import permlib.utilities.Combinations;

/**
 *
 * @author Michael Albert
 */
public class ConsecutiveIsomorphismSpecification {

    HashMap<Permutation, Permutation> iso;
    HashSet<Permutation> basis = new HashSet<Permutation>();

    public ConsecutiveIsomorphismSpecification() {
        this(new HashMap<Permutation, Permutation>());
    }

    public ConsecutiveIsomorphismSpecification(HashMap<Permutation, Permutation> iso) {
        this.iso = iso;
    }

    public ConsecutiveIsomorphismSpecification applySymmetryPair(Symmetry left, Symmetry right) {
        HashMap<Permutation, Permutation> newIso = new HashMap<Permutation, Permutation>();
        for (Permutation p : iso.keySet()) {
            if (iso.get(p) == null) {
                System.out.println("Oops " + p);
            }
            newIso.put(left.on(p), right.on(iso.get(p)));
        }
        return new ConsecutiveIsomorphismSpecification(newIso);
    }

    public void add(Permutation p, Permutation q) {
        iso.put(p, q);
    }

    public void remove(Permutation p) {
        iso.remove(p);
    }

    public boolean inDomain(Permutation p) {
        return iso.keySet().contains(p);
    }

    public boolean inRange(Permutation p) {
        return iso.values().contains(p);
    }

    public Permutation apply(Permutation p) {
        return iso.get(p);
    }

    public HashSet<Permutation> apply(HashSet<Permutation> ps) {
        HashSet<Permutation> result = new HashSet<Permutation>();
        for (Permutation p : ps) {
            if (!iso.containsKey(p)) {
                return null;
            }
            result.add(iso.get(p));
        }
        return result;
    }

    public ConsecutiveIsomorphismSpecification copy() {
        ConsecutiveIsomorphismSpecification result = this.applySymmetryPair(Symmetry.ID, Symmetry.ID);
        result.basis.addAll(this.basis);
        return result;
    }

    public static HashSet<ConsecutiveIsomorphismSpecification> symmetryRepresentatives(Collection<ConsecutiveIsomorphismSpecification> isos) {
        HashSet<ConsecutiveIsomorphismSpecification> result = new HashSet<ConsecutiveIsomorphismSpecification>();
        ArrayList<ConsecutiveIsomorphismSpecification> source = new ArrayList<ConsecutiveIsomorphismSpecification>();
        source.addAll(isos);
        while (!source.isEmpty()) {
            ConsecutiveIsomorphismSpecification rep = source.remove(0);
            result.add(rep);
            for (Symmetry l : Symmetry.values()) {
                for (Symmetry r : Symmetry.values()) {
                    source.remove(rep.applySymmetryPair(l, r));
                }
            }
        }
        return result;
    }

    public HashMap<Permutation, Permutation> getIso() {
        return iso;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + (this.iso != null ? this.iso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ConsecutiveIsomorphismSpecification other = (ConsecutiveIsomorphismSpecification) obj;
        if (this.iso != other.iso && (this.iso == null || !this.iso.equals(other.iso))) {
            return false;
        }
        return true;
    }

    public HashMap<HashSet<Permutation>, HashSet<Permutation>> possibleExtensions(int n) {
        ConsecutiveOrder.buildMaps(n);
        HashMap<HashSet<Permutation>, HashSet<Permutation>> result = new HashMap<HashSet<Permutation>, HashSet<Permutation>>();
        PermProperty inClass = new ConsecutiveAvoidanceTest(basis);
        for (Permutation p : new Permutations(n)) {
            if (!inClass.isSatisfiedBy(p)) {
                continue;
            }
            HashSet<Permutation> sp = ConsecutiveOrder.parents.get(p);
            HashSet<Permutation> isp = apply(sp);
            if (isp != null && ConsecutiveOrder.children.containsKey(isp)) {
                HashSet<Permutation> possibleImages = ConsecutiveOrder.children.get(isp);
//                System.out.println(p + " " + possibleImages.size());
                if (possibleImages.size() == 1) {
                    for (Permutation q : possibleImages) {
//                        System.out.println(p + "->" + q);
                        this.add(p, q);
                    }
                } else {
                    result.put(ConsecutiveOrder.siblings(p), possibleImages);
                }
            } else {
//                System.out.println("Basis: " + p);
                basis.add(p);
            }
        }
        return result;
    }

    public HashSet<ConsecutiveIsomorphismSpecification> extensions(int n) {
        HashSet<ConsecutiveIsomorphismSpecification> result = new HashSet<ConsecutiveIsomorphismSpecification>();
        HashMap<HashSet<Permutation>, HashSet<Permutation>> matchings = possibleExtensions(n);
//        for(HashSet<Permutation> dom : matchings.keySet()) {
//            System.out.println(dom + " " + matchings.get(dom));
//        }
        result.add(this.copy());
        for (HashSet<Permutation> domain : matchings.keySet()) {
            HashSet<HashMap<Permutation, Permutation>> ms = matchings(domain, matchings.get(domain));
//            System.out.println(domain + " " + matchings.get(domain) + " " + ms.size());
            HashSet<ConsecutiveIsomorphismSpecification> newResult = new HashSet<ConsecutiveIsomorphismSpecification>();
            for (HashMap<Permutation, Permutation> m : ms) {
                for (ConsecutiveIsomorphismSpecification r : result) {
                    ConsecutiveIsomorphismSpecification cr = r.copy();
                    for (Permutation p : m.keySet()) {
                        cr.add(p, m.get(p));
                    }
                    newResult.add(cr);
                }
            }
            result = newResult;
        }

        // result = isoReps(result);
        for (ConsecutiveIsomorphismSpecification a : result) {
            a.fixBasis(n);
        }

        return result;
    }

    public ConsecutiveIsomorphismSpecification oneExtension(int n) {
        ConsecutiveOrder.buildMaps(n);
        ConsecutiveIsomorphismSpecification result = this.copy();
        PermProperty inClass = new ConsecutiveAvoidanceTest(basis);
        for (Permutation p : new Permutations(n)) {
            if (!inClass.isSatisfiedBy(p) || inDomain(p)) {
                continue;
            }
            HashSet<Permutation> sp = ConsecutiveOrder.parents.get(p);
            HashSet<Permutation> isp = apply(sp);
            if (isp != null && ConsecutiveOrder.children.containsKey(isp)) {
                HashSet<Permutation> possibleImages = ConsecutiveOrder.children.get(isp);
                ArrayList<Permutation> pi = new ArrayList<Permutation>();
                for (Permutation q : possibleImages) {
                    if (!result.inRange(q)) {
                        pi.add(q);
                        break;
                    }
                }
                if (!pi.isEmpty()) {
                    result.add(p, pi.get(0));
                }
            }

        }
        result.fixBasis(n);
        return result;
    }

    public ConsecutiveIsomorphismSpecification randomExtension(int n) {
        ConsecutiveOrder.buildMaps(n);
        ConsecutiveIsomorphismSpecification result = this.copy();
        PermProperty inClass = new ConsecutiveAvoidanceTest(basis);
        for (Permutation p : new Permutations(n)) {
            if (!inClass.isSatisfiedBy(p)) {
                continue;
            }
            HashSet<Permutation> sp = ConsecutiveOrder.parents.get(p);
            HashSet<Permutation> isp = apply(sp);
            if (isp != null && ConsecutiveOrder.children.containsKey(isp)) {
                HashSet<Permutation> possibleImages = ConsecutiveOrder.children.get(isp);
                ArrayList<Permutation> pi = new ArrayList<Permutation>();
                for (Permutation q : possibleImages) {
                    if (!result.inRange(q)) {
                        pi.add(q);
                    }
                }
                if (!pi.isEmpty()) {
                    result.add(p, pi.get((int) (Math.random() * pi.size())));
                }
            }

        }
        result.fixBasis(n);
        return result;
    }

    private HashSet<HashMap<Permutation, Permutation>> matchings(HashSet<Permutation> a, HashSet<Permutation> b) {
        HashSet<HashMap<Permutation, Permutation>> result = new HashSet<HashMap<Permutation, Permutation>>();
        Permutation[] domain = new Permutation[a.size()];
        a.toArray(domain);
        Permutation[] codomain = new Permutation[b.size()];
        b.toArray(codomain);
        if (domain.length <= codomain.length) {
            for (int[] c : new Combinations(codomain.length, domain.length)) {
                for (Permutation p : new Permutations(domain.length)) {
                    HashMap<Permutation, Permutation> f = new HashMap<Permutation, Permutation>();
                    for (int i = 0; i < domain.length; i++) {
//                        System.out.println("Putting: " + domain[i] + " " + codomain[c[p.at(i)]]);
                        f.put(domain[i], codomain[c[p.at(i)]]);
                    }
                    result.add(f);
                }
            }
            return result;
        } else {
            for (int[] c : new Combinations(domain.length, codomain.length)) {
                for (Permutation p : new Permutations(codomain.length)) {
                    HashMap<Permutation, Permutation> f = new HashMap<Permutation, Permutation>();

                    for (int i = 0; i < codomain.length; i++) {
                        f.put(domain[c[p.at(i)]], codomain[i]);
                    }
                    result.add(f);
                }
            }
            return result;
        }
    }

    private void fixBasis(int n) {
        PermProperty avTest = new ConsecutiveAvoidanceTest(basis);
        for (Permutation p : new Permutations(n)) {
            if (avTest.isSatisfiedBy(p) && !inDomain(p)) {
                basis.add(p);
            }
        }
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Basis: ");
        for (Permutation p : basis) {
            result.append(p + " ");
        }
        result.append('\n');
        for (Permutation p : iso.keySet()) {
            result.append(p + " --> " + iso.get(p) + "\n");
        }
        return result.toString();
    }

    public static HashSet<ConsecutiveIsomorphismSpecification> isoReps(HashSet<ConsecutiveIsomorphismSpecification> cs) {
        HashSet<ConsecutiveIsomorphismSpecification> result = new HashSet<ConsecutiveIsomorphismSpecification>();
        HashSet<ConsecutiveIsomorphismSpecification> considered = new HashSet<ConsecutiveIsomorphismSpecification>();
        for (ConsecutiveIsomorphismSpecification c : cs) {
            if (!considered.contains(c)) {
                result.add(c);
                for (Symmetry l : Symmetry.values()) {
                    for (Symmetry r : Symmetry.values()) {
                        considered.add(c.applySymmetryPair(l, r));
                    }
                }
            }
        }
        return result;
    }

    public boolean allExtended(int n) {
        PermProperty avTest = new ConsecutiveAvoidanceTest(basis);
        HashSet<Permutation> domainN = new HashSet<Permutation>();
        for (Permutation p : new Permutations(n)) {
            if (inDomain(p)) {
                domainN.add(p);
            }
        }
        for (Permutation p : new Permutations(n + 1)) {
            if (inDomain(p)) {
                for (Permutation pp : ConsecutiveOrder.parents(p)) {
                    domainN.remove(pp);
                }
            }
        }

        for (Permutation p : domainN) {
            System.out.print(p + " ");
        }

        return domainN.isEmpty();
    }
//        ArrayList<ConsecutiveIsomorphismSpecification> source = new ArrayList<ConsecutiveIsomorphismSpecification>();
//        source.addAll(cs);
//        while (!source.isEmpty()) {
//            ConsecutiveIsomorphismSpecification c = source.remove(0);
//            result.add(c);
//            for (Symmetry l : Symmetry.values()) {
//                for (Symmetry r : Symmetry.values()) {
//                    source.remove(c.applySymmetryPair(l, r));
//                }
//            }
//        }
//        return result;
//    }

    void addBasis(Permutation... ps) {
        for (Permutation p : ps) {
            basis.add(p);
        }
    }
}
