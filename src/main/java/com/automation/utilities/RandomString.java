package com.automation.utilities;

/**
 * This class provides Utility Helper functions for the Logging Module
 *
 * @author Narasimha Dontula
 * @since 20/06/2020.
 */
public class RandomString { 
	  
    // function to generate a random string of length n 
    static String getAlphaNumericString(int n) 
    { 
  
        // chose a Character random from this String 
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz"; 
  
        // create StringBuffer size of AlphaNumericString 
        StringBuilder sb = new StringBuilder(n); 
  
        for (int i = 0; i < n; i++) { 
  
            // generate a random number between 
            // 0 to AlphaNumericString variable length 
            int index 
                = (int)(AlphaNumericString.length()* Math.random()); 
  
            // add Character one by one in end of sb 
            sb.append(AlphaNumericString 
                          .charAt(index)); 
        } 
  
        return sb.toString(); 
    } 
  
    public static void main(String[] args) 
    { 
  
        // Get the size n 
        int n = 10; 
  
        // Get and display the alphanumeric string 
        System.out.println(RandomString .getAlphaNumericString(n)); 
    } 
} 