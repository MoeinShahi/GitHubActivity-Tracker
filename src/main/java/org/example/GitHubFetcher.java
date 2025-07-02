package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GitHubFetcher {

    private static final String GREEN = "\u001B[32m";
    private static final String RESET = "\u001B[0m";

    public static void fetchActivityWithUI(String username) {
        // Show public IP
        System.out.println("Fetching your public IP...");
        String ip = fetchPublicIP();
        System.out.println("Your public IP is: " + GREEN + ip + RESET + "\n");

        System.out.println("Fetching recent activity for user: " + username);

        // Spinner thread
        Thread spinnerThread = new Thread(() -> {
            try {
                String[] spinner = {"|", "/", "-", "\\"};
                int i = 0;
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.print("\rFetching GitHub activity " + spinner[i++ % spinner.length]);
                    Thread.sleep(100);
                }
            } catch (InterruptedException ignored) {}
        });

        spinnerThread.start();

        int page = 1;
        boolean moreEvents = true;
        int totalEventsFetched = 0;
        StringBuilder allEvents = new StringBuilder();

        while (moreEvents && page <= 5) {
            String apiUrl = "https://api.github.com/users/" + username + "/events?per_page=100&page=" + page;
            try {
                URL url = new URL(apiUrl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("User-Agent", "Java");

                int status = con.getResponseCode();
                if (status != 200) {
                    spinnerThread.interrupt();
                    System.out.print("\r");
                    System.out.println("Error: Unable to fetch data. HTTP Status " + status);
                    return;
                }

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();
                con.disconnect();

                String jsonResponse = response.toString();

                if (jsonResponse.equals("[]") || jsonResponse.length() < 5) {
                    moreEvents = false;
                } else {
                    allEvents.append(jsonResponse);  // collect all pages
                    int eventsCount = jsonResponse.split("\\},\\{").length;
                    totalEventsFetched += eventsCount;
                    page++;
                }

            } catch (Exception e) {
                spinnerThread.interrupt();
                System.out.print("\r");
                System.out.println("An error occurred: " + e.getMessage());
                return;
            }
        }

        // Stop spinner and print success
        spinnerThread.interrupt();
        System.out.print("\r");
        System.out.println(GREEN + "✔ Fetching completed!" + RESET);

        // Delay before displaying data
        try {
            Thread.sleep(1000);  // 1 second pause
        } catch (InterruptedException ignored) {}

        // Loading screen
        System.out.println("\nPreparing output...\n");
        try {
            Thread.sleep(500); // short pause
        } catch (InterruptedException ignored) {}

        GitHubParser.parseAndDisplay(allEvents.toString());
        System.out.println("\nTotal events fetched: " + totalEventsFetched);
    }

    private static String fetchPublicIP() {
        try {
            URL url = new URL("https://api.ipify.org");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String ip = in.readLine();
            in.close();
            return ip;
        } catch (Exception e) {
            return "Unknown";
        }
    }
}
