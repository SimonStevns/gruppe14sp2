
package Domain;


public class PassChecker {

    public PassChecker(String password){
       
    }
    
    public boolean checkPassword(String password){
        if (password.length() < 8){
            return false;
        } else {
            boolean upper = false;
            boolean lower = false;
            boolean number = false;
            for (char c: password.toCharArray()){
                if (Character.isUpperCase(c)){
                    upper = true;
                } else if (Character.isLowerCase(c)){
                    lower = true;
                } else if (Character.isDigit(c)){
                    number = true;
                }
            }

            if ( upper == true && lower == true && number == true){
                return true;
            }
        }
        return false;
    }
    
}
