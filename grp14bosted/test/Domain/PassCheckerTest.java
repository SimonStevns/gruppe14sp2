
package Domain;

import static Domain.PassChecker.checkPassword;
import org.junit.Test;
import static org.junit.Assert.*;


public class PassCheckerTest {
    

    @Test
    public void testCheckPassword() {
        String passwordTrue = "ThisPassw0rdIsLegit";
        String passwordFalse = "ThisPasswordShouldBeFalse";
        assertTrue(checkPassword(passwordTrue));
        assertFalse(checkPassword(passwordFalse));
        
        
        
    }
    
}
