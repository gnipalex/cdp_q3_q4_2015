package com.epam.cdp.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Tasks {

    private static final Pattern ALPHABETIC_RPL = Pattern.compile("[^a-zA-Z]");
    
    private static final int WORD_LENGTH_MIN = 5;
    
    private static final int WORD_LENGTH_MAX = 25;
    
    private static final int FILTERED_WORDS_MAX_SIZE = 70_000;
    
    private static final String WORDS_SPLIT_PATTERN = "[^\\w]+";
    
    private static final int TOP_WORDS_MAX_COUNT = 10;
    
    private static final String TASK3_FILE_1 = "samples/task3.txt";
    
    private static final String TASK3_FILE_2 = "samples/task3_1.txt";
    
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
        Comparator<String> stringLengthDescComparator = (w1, w2) -> w2.length() - w1.length();
        return getStream(words, isParallel)
                .map(Tasks::leaveAlphabeticalSymbolsOnly)
                .filter(Tasks::checkWordLength)
                .map((word) -> word.toLowerCase())
                .sorted(stringLengthDescComparator)
                .sorted(stringLengthDescComparator.reversed())
                .limit(FILTERED_WORDS_MAX_SIZE)
                .collect(Collectors.toList());
    }

    public static List<String> task3(Path... paths)  {
        Map<String, Integer> wordsMap = new HashMap<>();
        //read and populate words and their count to a map
        Arrays.stream(paths)
            .flatMap(Tasks::getStreamOfLines)
            .flatMap(line -> Stream.of(line.split(WORDS_SPLIT_PATTERN)))
            .map(String::toLowerCase)
            .forEach(word -> Tasks.addWordToMap(word, wordsMap));
        //sort map with words by word count (desc) and return top words
        return wordsMap.entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
            .limit(TOP_WORDS_MAX_COUNT)
            .map(entry -> entry.getKey())
            .collect(Collectors.toList());
    }
    
    private static Stream<String> getStreamOfLines(Path path) {
        try {
            return Files.lines(path);
        } catch (IOException e) {
            System.err.println("Failed to read path " + path.toString());
            return Stream.empty();
        }   
    }
    
    private static void addWordToMap(String word, Map<String, Integer> wordsMap) {
        if (wordsMap.containsKey(word)) {
            wordsMap.put(word, wordsMap.get(word) + 1);
        } else {
            wordsMap.put(word, 1);
        }
    }
    
    public static void main(String[] args) {
        List<String> topWords = task3(Paths.get(TASK3_FILE_1),
                Paths.get(TASK3_FILE_2));
        System.out.println("Task3 top words:");
        System.out.println(Arrays.toString(topWords.toArray()));
    }
    
}
