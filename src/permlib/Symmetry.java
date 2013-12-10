package permlib;

/**
 * This enum provides the fundamental symmetries of the involvement order on
 * permutations.
 *
 * @author Michael Albert
 */
public enum Symmetry {

    /**
     * The identity
     */
    ID {

        @Override
        public Symmetry inverse() {
            return ID;
        }

        @Override
        public Permutation on(Permutation p) {
            return p.clone();
        }
    },
    /**
     * Reverse
     */
    R {
        
        @Override
        public Symmetry inverse() {
            return R;
        }

        @Override
        public Permutation on(Permutation p) {
            return p.reverse();
        }
    },
    /**
     * Complement
     */
    C {

        @Override
        public Symmetry inverse() {return C;}
        
        @Override
        public Permutation on(Permutation p) {
            return p.complement();
        }
    },
    /**
     * Reverse-complement
     */
    RC {

        @Override
        public Symmetry inverse() {return RC;}
        
        @Override
        public Permutation on(Permutation p) {
            return R.on(C.on(p));
        }
    },
    /**
     * Inverse
     */
    INV {

        @Override
        public Symmetry inverse() {return INV;}
        
        @Override
        public Permutation on(Permutation p) {
            return p.inverse();
        }
    },
    /**
     * Inverse-reverse
     */
    IR {

        @Override
        public Symmetry inverse() {return IC;}
        
        @Override
        public Permutation on(Permutation p) {
            return INV.on(R.on(p));
        }
    },
    /**
     * Inverse-complement
     */
    IC {

        @Override
        public Symmetry inverse() {return IR;}
        
        @Override
        public Permutation on(Permutation p) {
            return INV.on(C.on(p));
        }
    },
    /**
     * Inverse-reverse-complement
     */
    IRC {

        @Override
        public Symmetry inverse() {return IRC;}
        
        @Override
        public Permutation on(Permutation p) {
            return INV.on(RC.on(p));
        }
    };

    /**
     * Applies the given symmetry to a permutation.
     * @param p the permutation
     * @return the image of <code>p</code> under the symmetry
     */
    public abstract Permutation on(Permutation p);

    /**
     * Applies the given symmetry to each permutation of an array.
     * @param ps the array of permutations
     * @return the image of the array under the symmetry
     */
    public Permutation[] onArray(Permutation[] ps) {
        Permutation[] result = new Permutation[ps.length];
        for (int i = 0; i < ps.length; i++) {
            result[i] = on(ps[i]);
        }
        return result;
    }

    /**
     * Returns the inverse of this symmetry.
     * @return the inverse of this symmetry
     */
    public abstract Symmetry inverse();
}
