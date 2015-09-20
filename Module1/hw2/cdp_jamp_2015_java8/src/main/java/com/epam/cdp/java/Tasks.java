package com.epam.cdp.java;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Tasks {

    private static final Pattern ALPHABETIC_RPL = Pattern.compile("[^a-zA-Z]");
    
    private static final int WORD_LENGTH_MIN = 5;
    
    private static final int WORD_LENGTH_MAX = 25;
    
    public static List<String> task1Java7(List<String> words) {
        List<String> filteredWords = new ArrayList<>();
        for (String word : words) {
        	String wordAlpha = leaveAlphabeticalSymbolsOnly(word);
        	if (checkWordLength(wordAlpha)) {
        		filteredWords.add(wordAlpha.toLowerCase());
        	}
        }
        return filteredWords;
    }
    
    private static boolean checkWordLength(String word) {
    	return word.length() >= WORD_LENGTH_MIN 
    			&& word.length() <= WORD_LENGTH_MAX;
    }
    
    private static String leaveAlphabeticalSymbolsOnly(String source) {
    	return ALPHABETIC_RPL.matcher(source).replaceAll("");
    }


    public static List<String> task1Java8(List<String> words, boolean isParallel) {
    	Stream<String> wordsStream = words.stream();
    	if (isParallel) {
    		wordsStream = wordsStream.parallel();
    	} 

        return wordsStream.map(Tasks::leaveAlphabeticalSymbolsOnly)
            	.filter(Tasks::checkWordLength)
            	.map((word) -> word.toLowerCase())
            	.collect(Collectors.toList());
    }

    public static List<String> task2Java7(List<String> words) {
        //TODO your code here
        return null;
    }

    public static List<String> task2Java8(List<String> words, boolean isParallel) {
        //TODO your code here
        return null;
    }

    public static List<String> task3(Path... paths) {
        //TODO your code here
        return null;
    }
    
}
