package rag.input;

import java.util.Scanner;

public class ConsoleQuerySource implements QuerySource {

    @Override
    public String getQuery() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the query string");
        String query = "";
        while (query.isEmpty()) {
            query = scanner.nextLine().trim();
            if (query.isEmpty()) {
                System.out.println("The request cannot be empty");
            }
        }
        return query;
    }
}
