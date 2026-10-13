import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Main {
    static String[] eastern_states = {"CT", "DE", "FL", "GA", "ME", "MD", "MA", "NH", "NJ", "NY", "NC", "PA", "RI", "SC", "VT", "VA", "WV"};
    static String[] middle_states  = {"AL", "AR", "IL", "IN", "IA", "KS", "KY", "LA", "MI", "MN", "MS", "MO", "NE", "ND", "OH", "OK", "SD", "TN", "TX", "WI"};
    static String[] western_states = {"AK", "AZ", "CO", "HI", "ID", "MT", "NV", "NM", "OR", "UT", "WA", "WY", "CA"};

    public static void main(String[] args) {
        regionSentiment();
    }

    private static void regionSentiment() {
        // Load every word and its score from cleanSentiment.csv
        HashMap<String, Double> wordScores = new HashMap<>();
        try (Scanner scanner = new Scanner(new File("cleanSentiment.csv"))) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                wordScores.put(parts[0], Double.parseDouble(parts[1]));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: cleanSentiment.csv was not found.");
            return;
        }

        double easternTotal = 0;
        double middleTotal = 0;
        double westernTotal = 0;
        int easternCount = 0;
        int middleCount = 0;
        int westernCount = 0;

        try (Scanner scanner = new Scanner(new File("reviews_data - Copy.csv"))) {
            scanner.nextLine(); // skip the header row

            while (scanner.hasNextLine()) {
                String[] columns = splitCsvLine(scanner.nextLine());
                String location = columns[1];
                String review = columns[4];

                // The state is the last 2 letters of the location, like "Seattle, WA"
                String state = location.substring(location.length() - 2).toUpperCase();

                // Find the average sentiment of the words in this review
                double reviewTotal = 0;
                int wordsFound = 0;
                String[] words = review.toLowerCase().split("[^a-z]+");
                for (String word : words) {
                    if (wordScores.containsKey(word)) {
                        reviewTotal += wordScores.get(word);
                        wordsFound++;
                    }
                }
                if (wordsFound == 0) {
                    continue; // no scored words, skip this review
                }
                double reviewAverage = reviewTotal / wordsFound;

                // Add the review to its part of the US
                if (contains(eastern_states, state)) {
                    easternTotal += reviewAverage;
                    easternCount++;
                } else if (contains(middle_states, state)) {
                    middleTotal += reviewAverage;
                    middleCount++;
                } else if (contains(western_states, state)) {
                    westernTotal += reviewAverage;
                    westernCount++;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: reviews_data - Copy.csv was not found.");
            return;
        }

        double easternAverage = easternTotal / easternCount;
        double middleAverage = middleTotal / middleCount;
        double westernAverage = westernTotal / westernCount;

        System.out.println("Average starbuck review Sentiment for East America: " + String.format("%.4f", easternAverage));
        System.out.println("Average starbuck review Sentiment for Middle America: " + String.format("%.4f", middleAverage));
        System.out.println("Average starbuck review Sentiment for West America: " + String.format("%.4f", westernAverage));
    }

    // Returns true if the state is in the list
    private static boolean contains(String[] states, String state) {
        for (String s : states) {
            if (s.equals(state)) {
                return true;
            }
        }
        return false;
    }

    // Splits a CSV line on commas, but not commas inside "quotes"
    private static String[] splitCsvLine(String line) {
        ArrayList<String> columns = new ArrayList<>();
        String current = "";
        boolean inQuotes = false;
        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                columns.add(current);
                current = "";
            } else {
                current += c;
            }
        }
        columns.add(current);
        return columns.toArray(new String[0]);
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