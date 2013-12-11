/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package permlib.property;

import java.util.Collection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import permlib.Permutation;
import permlib.Permutations;

/**
 *
 * @author MichaelAlbert
 */
public class AvoidsIncreasingTest {
    
    public AvoidsIncreasingTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

  
    /**
     * Test of isSatisfiedBy method, of class AvoidsIncreasing.
     */
    @Test
    public void testIsSatisfiedBy_Permutation() {
        System.out.println("isSatisfiedBy");
        AvoidsIncreasing instance = new AvoidsIncreasing(3);
        assertEquals(true, instance.isSatisfiedBy(new Permutation("321")));
        assertEquals(false, instance.isSatisfiedBy(new Permutation("123")));
        assertEquals(true, instance.isSatisfiedBy(new Permutation("2413")));
        int c = 0;
        for(Permutation p : new Permutations(5)) {
            if (instance.isSatisfiedBy(p)) c++;
        }
        assertEquals(42, c);
        
        
    }

  
    
}
