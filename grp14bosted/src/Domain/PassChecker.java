/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domain;

/**
 *
 * @author Nikolaj
 */
public class PassChecker {

    public PassChecker(String password){
       
    }
    
    public boolean checkPassword(String password){
        if (password.length() < 8){
            System.out.println("password skal minimum være 8 cifre");
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
