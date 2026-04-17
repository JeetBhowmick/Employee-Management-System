package com.company.employee.util;

import java.util.List;

public final class ConsoleTablePrinter {
    private ConsoleTablePrinter() {
    }

    public static void printTable(String title, String[] headers, List<String[]> rows) {
        System.out.println("\n==== " + title + " ====");
        int[] widths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            widths[i] = headers[i].length();
        }
        for (String[] row : rows) {
            for (int i = 0; i < row.length; i++) {
                widths[i] = Math.max(widths[i], row[i].length());
            }
        }

        printSeparator(widths);
        printRow(headers, widths);
        printSeparator(widths);
        for (String[] row : rows) {
            printRow(row, widths);
        }
        printSeparator(widths);
    }

    private static void printSeparator(int[] widths) {
        StringBuilder sb = new StringBuilder("+");
        for (int width : widths) {
            sb.append("-").append("-".repeat(width)).append("-+");
        }
        System.out.println(sb);
    }

    private static void printRow(String[] values, int[] widths) {
        StringBuilder sb = new StringBuilder("|");
        for (int i = 0; i < values.length; i++) {
            sb.append(" ").append(String.format("%-" + widths[i] + "s", values[i])).append(" |");
        }
        System.out.println(sb);
    }
}
