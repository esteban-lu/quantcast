/* *****************************************************************************
 *  file:   MostActiveCookie.java
 *  author: Louisa Esteban
 *  date:   2/1/2022
 *
 *  Compilation:  javac MostActiveCookie.java
 *  Execution:    java MostActiveCookie filename.csv -d XXXX-XX-XX
 *
 *  Developed in IntelliJ IDE
 * ****************************************************************************/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

// Helper class to accumulate frequencies of cookies
class Cookie {
    private final String cookie; // cookie reference id
    private int count;           // frequency of cookie in the logfile

    public Cookie(String cookie, int count) {
        this.cookie = cookie;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public String getCookie() {
        return cookie;
    }

    public void increment() {
        this.count++;
    }

}

// Helper class to sort cookies
class SortCookieByFrequency implements Comparator<Cookie> {
    public int compare(Cookie a, Cookie b) {
        return b.getCount() - a.getCount();
    }
}

public class MostActiveCookie {

    // Validate CLI Arguments
    public static void validateArgs(String[] args) {
        if (!Pattern.matches("^.*\\.csv", args[0]))
            throw new IllegalArgumentException("File argument must be a .csv file.");
        if (!Pattern.matches("-d", args[1]))
            throw new IllegalArgumentException("Missing -d flag.");
        if (!Pattern.matches("^\\d{4}-\\d{2}-\\d{2}$", args[2]))
            throw new IllegalArgumentException("Date argument formatted incorrectly.");
    }

    // Creates a scanner to read data from a file of the specified filename.
    // Throws a FileNotFound Exception if the file does not exist.
    public static Scanner csvScanner(String filename) throws FileNotFoundException {
        Scanner in = new Scanner(new File(filename));
        if (!in.nextLine().equals("cookie,timestamp"))
            throw new Error("CSV formatted incorrectly.");
        return in;
    }

    // Reads data from the Scanner and returns an array of all Cookie objects
    // and their frequencies that were logged on the given date.
    public static Cookie[] getCookies(Scanner in, String date) {
        HashMap<String, Cookie> hashTable = new HashMap<>();
        boolean foundDate = false;

        while (in.hasNext()) {
            String[] line = in.nextLine().split(",");
            String cookie = line[0];

            if (line[1].substring(0, 10).equals(date.substring(0, 10))) {
                foundDate = true;
                if (hashTable.containsKey(cookie)) {
                    Cookie temp = hashTable.get(cookie);
                    temp.increment();
                } else hashTable.put(cookie, new Cookie(cookie, 1));
            } else if (foundDate) break;
        }
        return hashTable.values().toArray(new Cookie[0]);
    }

    // Given an array of cookies sorted in decreasing order of frequency,
    // print the reference ids of the most frequently logged cookie(s).
    public static void printMostFrequent(Cookie[] arr) {
        int index = 0;
        Cookie currCookie = arr[index];
        while (index < arr.length - 1) {
            if (arr[index + 1].getCount() < arr[index].getCount()) break;
            System.out.println(currCookie.getCookie());
            index++;
            currCookie = arr[index];
        }
        System.out.println(currCookie.getCookie());
    }

    public static void main(String[] args) throws FileNotFoundException {

        // Validate CLI arguments
        validateArgs(args);

        // Read data from the specified file
        Scanner in = csvScanner(args[0]);
        in.useDelimiter(",");

        // Find and print the most frequent cookie(s) logged on the specified date
        Cookie[] cookies = getCookies(in, args[2]);
        Arrays.sort(cookies, new SortCookieByFrequency());
        if (cookies.length > 0) printMostFrequent(cookies);
    }
}
