package edu.campus.ccrm.util;

import edu.campus.ccrm.domain.*;
import edu.campus.ccrm.domain.interfaces.Displayable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Utility class for generating various reports.
 * Demonstrates functional programming with Streams and lambda expressions.
 */
public class ReportGenerator {

    /**
     * Generates a formatted report for any collection of displayable objects
     */
    public static <T extends Displayable> String generateReport(
            Collection<T> items,
            String title,
            Function<T, String> formatter) {

        StringBuilder report = new StringBuilder();
        report.append("=".repeat(60)).append("\n");
        report.append(title).append("\n");
        report.append("=".repeat(60)).append("\n");

        if (items.isEmpty()) {
            report.append("No items found.\n");
        } else {
            report.append(String.format("Total items: %d\n", items.size()));
            report.append("-".repeat(60)).append("\n");

            items.forEach(item -> {
                report.append(formatter.apply(item)).append("\n");
            });
        }

        report.append("=".repeat(60)).append("\n");
        return report.toString();
    }

    /**
     * Generates a summary report with statistics
     */
    public static <T> String generateSummaryReport(
            Collection<T> items,
            String title,
            Map<String, Function<T, ?>> statistics) {

        StringBuilder report = new StringBuilder();
        report.append("=".repeat(60)).append("\n");
        report.append(title + " - Summary Report").append("\n");
        report.append("=".repeat(60)).append("\n");

        report.append(String.format("Total Items: %d\n", items.size()));

        for (Map.Entry<String, Function<T, ?>> entry : statistics.entrySet()) {
            String statName = entry.getKey();
            Function<T, ?> statFunction = entry.getValue();

            if (statFunction.apply(items.iterator().next()) instanceof Number) {
                double avg = items.stream()
                        .map(statFunction)
                        .mapToDouble(v -> ((Number) v).doubleValue())
                        .average()
                        .orElse(0.0);
                report.append(String.format("%s: %.2f\n", statName, avg));
            } else {
                long count = items.stream()
                        .map(statFunction)
                        .distinct()
                        .count();
                report.append(String.format("Unique %s: %d\n", statName.toLowerCase(), count));
            }
        }

        report.append("=".repeat(60)).append("\n");
        return report.toString();
    }

    /**
     * Generates a filtered report
     */
    public static <T extends Displayable> String generateFilteredReport(
            Collection<T> items,
            Predicate<T> filter,
            String title,
            Function<T, String> formatter) {

        List<T> filteredItems = items.stream()
                .filter(filter)
                .collect(Collectors.toList());

        return generateReport(filteredItems, title + " (Filtered)", formatter);
    }

    /**
     * Generates a grouped report
     */
    public static <T, K> String generateGroupedReport(
            Collection<T> items,
            Function<T, K> groupBy,
            String title,
            Function<T, String> formatter) {

        Map<K, List<T>> grouped = items.stream()
                .collect(Collectors.groupingBy(groupBy));

        StringBuilder report = new StringBuilder();
        report.append("=".repeat(60)).append("\n");
        report.append(title + " - Grouped Report").append("\n");
        report.append("=".repeat(60)).append("\n");

        grouped.forEach((key, groupItems) -> {
            report.append(String.format("\n%s (%d items):\n", key, groupItems.size()));
            report.append("-".repeat(40)).append("\n");
            groupItems.forEach(item -> {
                report.append(formatter.apply(item)).append("\n");
            });
        });

        report.append("=".repeat(60)).append("\n");
        return report.toString();
    }

    /**
     * Generates a comparative report between two collections
     */
    public static <T> String generateComparativeReport(
            Collection<T> collection1,
            Collection<T> collection2,
            String title1,
            String title2,
            Function<T, String> formatter) {

        StringBuilder report = new StringBuilder();
        report.append("=".repeat(80)).append("\n");
        report.append("Comparative Report").append("\n");
        report.append("=".repeat(80)).append("\n");

        report.append(String.format("%-40s | %-40s\n", title1, title2));
        report.append("-".repeat(80)).append("\n");

        int maxSize = Math.max(collection1.size(), collection2.size());
        Iterator<T> iter1 = collection1.iterator();
        Iterator<T> iter2 = collection2.iterator();

        for (int i = 0; i < maxSize; i++) {
            String item1 = iter1.hasNext() ? formatter.apply(iter1.next()) : "";
            String item2 = iter2.hasNext() ? formatter.apply(iter2.next()) : "";

            report.append(String.format("%-40s | %-40s\n",
                    item1.length() > 40 ? item1.substring(0, 37) + "..." : item1,
                    item2.length() > 40 ? item2.substring(0, 37) + "..." : item2));
        }

        report.append("=".repeat(80)).append("\n");
        return report.toString();
    }

    /**
     * Generates a trend report showing changes over time
     */
    public static <T> String generateTrendReport(
            Map<String, Collection<T>> timeSeries,
            String title,
            Function<Collection<T>, String> summaryFunction) {

        StringBuilder report = new StringBuilder();
        report.append("=".repeat(60)).append("\n");
        report.append(title + " - Trend Report").append("\n");
        report.append("=".repeat(60)).append("\n");

        timeSeries.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    report.append(String.format("\n%s:\n", entry.getKey()));
                    report.append("-".repeat(40)).append("\n");
                    report.append(summaryFunction.apply(entry.getValue())).append("\n");
                });

        report.append("=".repeat(60)).append("\n");
        return report.toString();
    }

    /**
     * Utility method to format numbers with appropriate precision
     */
    public static String formatNumber(double number, int decimalPlaces) {
        return String.format("%." + decimalPlaces + "f", number);
    }

    /**
     * Utility method to truncate strings to specified length
     */
    public static String truncateString(String str, int maxLength) {
        if (str == null)
            return "";
        if (str.length() <= maxLength)
            return str;
        return str.substring(0, maxLength - 3) + "...";
    }

    /**
     * Utility method to format percentages
     */
    public static String formatPercentage(double value, double total) {
        if (total == 0)
            return "0.00%";
        double percentage = (value / total) * 100;
        return String.format("%.2f%%", percentage);
    }
}
