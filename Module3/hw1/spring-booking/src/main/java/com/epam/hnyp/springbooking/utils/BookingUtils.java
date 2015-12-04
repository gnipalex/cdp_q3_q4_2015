package com.epam.hnyp.springbooking.utils;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.OptionalLong;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BookingUtils {
	
	public static <T> List<T> getPageFromStream(Stream<T> stream, int pageSize, int pageNum) {
		assertNotNegative(pageNum, MessageFormat.format("pageNum {0}, expected not negative value", pageNum));
		assertNotNegative(pageSize, MessageFormat.format("pageSize {0}, expected not negative value", pageSize));
		return stream.skip(pageSize * (pageNum - 1))
				.limit(pageSize)
				.collect(Collectors.toList());
	}
	
	private static void assertNotNegative(int value, String message) {
		if (value < 0) {
			throw new IllegalArgumentException(message);
		}
	}
	
	/**
	 * Gets max long value from mapped source collection. 
	 * @param source
	 * @param longFieldMapper
	 * @return max value + 1, if max value not found 0L is being returned
	 */
	public static <T> long getNextToMaxLong(Collection<T> source, ToLongFunction<T> longFieldMapper) {
		OptionalLong maxValue = source.stream().mapToLong(longFieldMapper).reduce(Long::max);
		long nextId = 0L;
		if (maxValue.isPresent()) {
			nextId = maxValue.getAsLong() + 1;
		}
		return nextId;
	}
	
	/**
	 * Compares values avoiding NullPointerException
	 * @param first Comparable
	 * @param second Comparable
	 * @return if values are not null, returns  first.compareTo(second)     <br>
	 * if first == null, returns -1        <br>
	 * if second == null, returns 1    <br>
	 * if first == second == null, returns 0
	 */
	public static <T extends Comparable<T>> int compareNullSafe(T first, T second) {
        if (first == null && second == null) {
            return 0;
        } else if (first == null) {
            return -1;
        } else if (second == null) {
            return 1;
        } else {
            return first.compareTo(second);
        }
	}
}
