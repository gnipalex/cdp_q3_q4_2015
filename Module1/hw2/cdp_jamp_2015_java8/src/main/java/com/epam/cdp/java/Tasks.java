package com.epam.cdp.java;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Tasks {

    private static final Pattern ALPHABETIC_RPL = Pattern.compile("[^a-zA-Z]");
    
    private static final int WORD_LENGTH_MIN = 5;
    
    private static final int WORD_LENGTH_MAX = 25;
    
    private static final int FILTERED_WORDS_MAX_SIZE = 70_000;
    
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
        return getStream(words, isParallel)
                .map(Tasks::leaveAlphabeticalSymbolsOnly)
                .filter(Tasks::checkWordLength)
                .map((word) -> word.toLowerCase())
                .collect(Collectors.toList());
    }
    
    private static <T> Stream<T> getStream(List<T> source, boolean isParallel) {
        if (isParallel) {
            return source.stream().parallel();
        }
        return source.stream();
    }

    public static List<String> task2Java7(List<String> words) {
        List<String> filteredWords = new ArrayList<>();
        for (String word : words) {
            String wordAlpha = leaveAlphabeticalSymbolsOnly(word);
            if (checkWordLength(wordAlpha)) {
                filteredWords.add(wordAlpha.toLowerCase());
            }
        }
        Collections.sort(filteredWords, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.length() - o1.length();
            }
        });
        Collections.sort(filteredWords, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.length() - o2.length();
            }
        });
        if (filteredWords.size() > FILTERED_WORDS_MAX_SIZE) {
            filteredWords.subList(0, FILTERED_WORDS_MAX_SIZE - 1);
        }
        return filteredWords;
    }

    public static List<String> task2Java8(List<String> words, boolean isParallel) {
        return getStream(words, isParallel)
                .map(Tasks::leaveAlphabeticalSymbolsOnly)
                .filter(Tasks::checkWordLength)
                .map((word) -> word.toLowerCase())
                .sorted((w1, w2) -> w2.length() - w1.length())
                .sorted((w1, w2) -> w1.length() - w2.length())
                .limit(FILTERED_WORDS_MAX_SIZE)
                .collect(Collectors.toList());
    }

    public static List<String> task3(Path... paths) {
        //TODO your code here
        return null;
    }
    
}
