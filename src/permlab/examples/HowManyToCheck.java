/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package permlab.examples;

import permlib.PermUtilities;
import permlib.Permutation;
import permlib.property.HereditaryProperty;


/**
 *
 * @author MichaelAlbert
 */
public class HowManyToCheck {

    static String[][] s = {{"2143"},
        {"21543",
            "32154",
            "315264",
            "215364",
            "214365",
            "426153",
            "231564",
            "241365",
            "312645",
            "314265",
            "241635",
            "214635",
            "5472163",
            "4265173",
            "5173264",
            "5276143",
            "5271436",
            "2547163",
            "51763284",
            "25476183",
            "61837254",
            "65821437",
            "54726183",
            "64821537",
            "26587143",
            "65872143",
            "51736284",
            "26581437",
            "26481537",
            "61873254",
            "64872153",
            "61832547",
            "54762183",
            "26487153",
            "65827143"},
        {"316254",
            "214365",
            "326154",
            "426153",
            "231654",
            "432165",
            "325164",
            "312654",
            "241653",
            "321654",
            "421653",
            "421635",
            "321645",
            "216543",
            "321564",
            "5314276",
            "3162475",
            "3416275",
            "2514736",
            "3165274",
            "3514276",
            "3521476",
            "2541376",
            "4253176",
            "2165374",
            "2513746",
            "5472163",
            "2316475",
            "4163275",
            "2415736",
            "2315746",
            "5241376",
            "4153276",
            "2413675",
            "2164735",
            "2164753",
            "3126475",
            "3512746",
            "4251376",
            "4265173",
            "3152746",
            "2157436",
            "5173264",
            "2413756",
            "2157364",
            "5276143",
            "5271436",
            "2416375",
            "2157463",
            "2517436",
            "3146275",
            "3142756",
            "2547163",
            "2163754",
            "4315276",
            "2416735",
            "3524176",
            "3142675",
            "2147635",
            "3162745",
            "3164275",
            "2541736",
            "2175364",
            "3125746",
            "2174635",
            "51763284",
            "21468357",
            "23416785",
            "25476183",
            "61837254",
            "65821437",
            "54726183",
            "64821537",
            "26587143",
            "65872143",
            "51736284",
            "41526387",
            "26581437",
            "34127856",
            "41238567",
            "26481537",
            "61873254",
            "41627385",
            "64872153",
            "24618357",
            "21637485",
            "24613587",
            "61832547",
            "54762183",
            "26487153",
            "65827143"}};

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        HereditaryProperty[][] bases = new HereditaryProperty[s.length][];
        for (int i = 0; i < s.length; i++) {
            bases[i] = new HereditaryProperty[s[i].length];
            for (int j = 0; j < bases[i].length; j++) {
                bases[i][j] = PermUtilities.avoidanceTest(s[i][j]);
            }
        }
        int count = 0;
        int v = 0;
        int trials = 100000;
        for (int trial = 0; trial < trials; trial++) {
            Permutation p = PermUtilities.randomPermutation(16);
            boolean avoids = true;
            for (int i = 0; i < bases.length; i++) {
                for (HereditaryProperty a : bases[i]) {
                    avoids = a.isSatisfiedBy(p);
                    if (!avoids) {
                        break;
                    }
                }
                if (avoids) {
                    break;
                }
            }
            if (avoids) {
                count++;
            } else {
                v += LascouxSchutzenberger.vexBound(p,4) ? 1 : 0;
            }
           // System.out.println(trial);
            
        }
        System.out.println(count);
        System.out.println(v + " " + ((float) v)/(trials-count));
    }
}
