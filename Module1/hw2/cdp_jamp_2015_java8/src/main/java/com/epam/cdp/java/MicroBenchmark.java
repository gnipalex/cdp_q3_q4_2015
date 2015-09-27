package com.epam.cdp.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

public class MicroBenchmark {

    private static final int TEST_COUNT = 5;

    public static void main(String[] args) throws IOException {
        List<String> words = loadWords();
        test("Task 1, java 7, serial ", () -> Tasks.task1Java7(words));
        test("Task 1, java 8, serial ", () -> Tasks.task1Java8(words, false));
        test("Task 1, java 8, parallel ", () -> Tasks.task1Java8(words, true));
        test("Task 2, java 7, serial ", () -> Tasks.task2Java7(words));
        test("Task 2, java 8, serial ", () -> Tasks.task2Java8(words, false));
        test("Task 2, java 8, parallel ", () -> Tasks.task2Java8(words, true));
        //NOTE: as this is a microbenchmark, please run tests separately.
    }

    public static <T> void test(String label, Supplier<T> task) {
        for (int i = 0; i < TEST_COUNT; i++) {
            System.out.printf("%s: time - %d\n", label, oneRun(task));
        }
    }

    public static <T> long oneRun(Supplier<T> task) {
        long time = System.nanoTime();
        task.get();
        return System.nanoTime() - time;
    }

    private static List<String> loadWords() throws IOException {
        return Files.lines(Paths.get("samples/wordslist.txt")).collect(toList());
    }
}
