
package Domain;

import static Domain.PassChecker.checkPassword;
import org.junit.Test;
import static org.junit.Assert.*;


public class PassCheckerTest {
    

    @Test
    public void testCheckPassword() {
        String passwordTrue = "ThisPassw0rdIsLegit";
        String passwordNoNum = "ThisShouldBeFalse";
        String passwordNoUpper = "thissh0uldbefalse";
        String passwordNoLower = "THISSH0ULDBEFALSE";
        assertTrue(checkPassword(passwordTrue));
        assertFalse(checkPassword(passwordNoNum));
        assertFalse(checkPassword(passwordNoUpper));
        assertFalse(checkPassword(passwordNoLower));
        
        
        
        
    }
    
}
