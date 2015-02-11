package com.jmpesp.halgnu.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandHelper {

    public static String removeCommandFromString(String commandString) {
        String result = new String();
        String[] split = commandString.split(" ");
        List<String> wordList = new ArrayList(Arrays.asList(split));
        wordList.remove(0);

        for(String word: wordList) {
            result += word + " ";
        }
        
        return result;
    }
    
    public static boolean checkForAmountOfArgs(String commandString, int amountOfArgs) {
        String[] split = commandString.split(" ");
        
        if((split.length-1) >= amountOfArgs) {
            return true;
        }
        
        return false;
    }
}
