package org.example;

import java.util.Scanner;

public class GitHubActivity {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter GitHub username: ");
        String username = scanner.nextLine().trim();

        if (username.isEmpty()) {
            System.out.println("Username cannot be empty!");
            scanner.close();
            return;
        }

        GitHubFetcher.fetchActivityWithUI(username);
        scanner.close();
    }
}
