package org.example;

public class GitHubParser {

    public static void parseAndDisplay(String jsonResponse) {
        String[] events = jsonResponse.split("\\},\\{");

        for (int i = 0; i < events.length; i++) {
            String event = events[i];

            String type = extractValue(event, "\"type\":\"", "\"");
            String repo = extractValue(event, "\"name\":\"", "\"");
            String createdAt = extractValue(event, "\"created_at\":\"", "\"");

            if (type == null || repo == null) continue;

            System.out.println("Event " + (i + 1) + ":");

            switch (type) {
                case "PushEvent":
                    System.out.println("  Pushed code to " + repo);
                    printPushCommits(event);
                    break;

                case "IssuesEvent":
                    String action = extractValue(event, "\"action\":\"", "\"");
                    System.out.println("  " + capitalize(action) + " an issue in " + repo);
                    break;

                case "WatchEvent":
                    System.out.println("  Starred " + repo);
                    break;

                case "ForkEvent":
                    System.out.println("  Forked " + repo);
                    break;

                case "CreateEvent":
                    System.out.println("  Created " + extractValue(event, "\"ref_type\":\"", "\"") + " in " + repo);
                    break;

                default:
                    System.out.println("  " + type + " on " + repo);
                    break;
            }

            if (createdAt != null) {
                System.out.println("  At: " + createdAt);
            }

            System.out.println("--------------------------------------------------");
        }
    }

    private static void printPushCommits(String event) {
        String commitsSection = extractValue(event, "\"commits\":[", "]");

        if (commitsSection == null) {
            System.out.println("    (No commit details)");
            return;
        }

        String[] commits = commitsSection.split("\\},\\{");

        for (String commit : commits) {
            String message = extractValue(commit, "\"message\":\"", "\"");
            if (message != null) {
                System.out.println("    Commit message: " + message);
            }
        }
    }

    private static String extractValue(String source, String start, String end) {
        int startIndex = source.indexOf(start);
        if (startIndex == -1) return null;
        int endIndex = source.indexOf(end, startIndex + start.length());
        if (endIndex == -1) return null;
        return source.substring(startIndex + start.length(), endIndex);
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0,1).toUpperCase() + str.substring(1);
    }
}
