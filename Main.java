import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static String[] eastern_states = {"CT", "DE", "FL", "GA", "ME", "MD", "MA", "NH", "NJ", "NY", "NC", "PA", "RI", "SC", "VT", "VA", "WV"};
    static String[] middle_states  = {"AL", "AR", "IL", "IN", "IA", "KS", "KY", "LA", "MI", "MN", "MS", "MO", "NE", "ND", "OH", "OK", "SD", "TN", "TX", "WI"};
    static String[] western_states = {"AK", "AZ", "CO", "HI", "ID", "MT", "NV", "NM", "OR", "UT", "WA", "WY", "CA"};

    public static void main(String[] args) {
        readCsv("cleanSentiment.csv");
        readCsv("reviews_data.csv");
    }

    private static void readCsv(String fileName) {
        File file = new File(fileName);

        try (Scanner scanner = new Scanner(file)) {
            System.out.println("Reading file: " + fileName);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(",");

                // Print the full row
                System.out.println(line);
            }

            System.out.println();
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + fileName + " was not found in the directory.");
            e.printStackTrace();
        }
    }
}