package Leetcode;

import java.util.*;

public class Leetcode950 {

    /**
     * #925
     *
     * @param name name string
     * @param typed input string
     * @return is long press
     */
    public static boolean isLongPressedName(String name, String typed) {
        if(name.charAt(0) != typed.charAt(0)){
            return false;
        }
        char lastChar = name.charAt(0);
        int namePtr = 1, typedPtr = 1;
        for(;typedPtr < typed.length(); typedPtr++){
            if(namePtr < name.length()){
                if(typed.charAt(typedPtr) == name.charAt(namePtr)){
                    lastChar = name.charAt(namePtr);
                    namePtr++;
                }
                else{
                    if(typed.charAt(typedPtr) != lastChar){
                        return false;
                    }
                }
            }
            else{
                if(typed.charAt(typedPtr) != lastChar){
                    return false;
                }
            }
        }
        return namePtr == name.length();
    }
}
