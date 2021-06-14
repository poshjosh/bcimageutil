package com.bc.imageutil;

/**
 * @author hp
 */
public class Util {
    
    /** 
     * @param path The path whose extension in form 'jpg' will be returned
     * @param resultIfNone The result to return if the path contains no extension
     * @return The extension of the input path in form 'jpg'
     */
    public static String getExtension(String path, String resultIfNone) {
        int n = path.lastIndexOf('.');
        return n == -1 ? resultIfNone : path.substring(n + 1);
    }
}
