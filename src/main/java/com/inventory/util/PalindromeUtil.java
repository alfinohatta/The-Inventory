package com.inventory.util;

public class PalindromeUtil {
    
    /**
     * Detects if a string is a palindrome
     * Case-insensitive, ignores non-alphanumeric characters and spaces
     * 
     * @param text the text to check
     * @return true if palindrome, false otherwise
     */
    public static boolean isPalindrome(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }
        
        // Remove non-alphanumeric characters and convert to lowercase
        String cleaned = text.replaceAll("[^A-Za-z0-9]", "").toLowerCase();
        
        if (cleaned.isEmpty()) {
            return false;
        }
        
        // Check if palindrome
        int left = 0;
        int right = cleaned.length() - 1;
        
        while (left < right) {
            if (cleaned.charAt(left) != cleaned.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        
        return true;
    }
    
    /**
     * Gets a formatted palindrome status string
     * 
     * @param text the text to check
     * @return "Palindrome: YES" or "Palindrome: NO"
     */
    public static String getPalindromeStatus(String text) {
        return "Palindrome: " + (isPalindrome(text) ? "YES" : "NO");
    }
} 