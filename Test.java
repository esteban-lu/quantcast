/* *****************************************************************************
 *  file:   Test.java
 *  author: Louisa Esteban
 *  date:   2/1/2022
 *
 *  Compilation:  javac Test.java
 *  Execution:    java Test
 *  Dependencies: MostActiveCookie.java
 *
 *  Developed in IntelliJ IDE
 * ************************************************************************** */

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.*;
import java.nio.file.Files;

@SuppressFBWarnings("DM_DEFAULT_ENCODING")
public class Test {

    // Default System.out
    private static PrintStream StdOut;

    // Tests given sample cookie log
    private static void testRegularInput() {
        try {
            File file = new File("cookie_log_test.txt");
            PrintStream output = new PrintStream(file);
            System.setOut(output);
            MostActiveCookie.main(new String[]{"cookie_log.csv", "-d", "2018-12-08"});
            output.close();
            printResult(file, new File("cookie_log_ref.txt"), "testRegularInput()");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Tests case for which no cookie logs match the given date
    private static void testNoDateMatches() {
        try {
            File file = new File("no_date_matches_test.txt");
            PrintStream output = new PrintStream(file);
            System.setOut(output);
            MostActiveCookie.main(new String[]{"no_date_matches.csv", "-d", "2012-10-02"});
            output.close();
            printResult(file, new File("no_date_matches_ref.txt"), "testNoDatesMatch()");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Tests case for which all cookie logs match the given date
    private static void testAllDatesMatch() {
        try {
            File file = new File("all_dates_match_test.txt");
            PrintStream output = new PrintStream(file);
            System.setOut(output);
            MostActiveCookie.main(new String[]{"all_dates_match.csv", "-d", "2018-12-09"});
            output.close();
            printResult(file, new File("all_dates_match_ref.txt"), "testAllDatesMatch()");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Tests case where cookie log file has no logs
    private static void testNoCookieLogs() {
        try {
            File file = new File("no_cookie_logs_test.txt");
            PrintStream output = new PrintStream(file);
            System.setOut(output);
            MostActiveCookie.main(new String[]{"no_cookie_logs.csv", "-d", "2000-10-02"});
            output.close();
            printResult(file, new File("no_cookie_logs_ref.txt"), "testNoCookieLogs()");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Tests case for which all cookies are logged with the same frequency
    private static void testAllCookiesSameFrequency() {
        try {
            File file = new File("all_cookies_same_frequency_test.txt");
            PrintStream output = new PrintStream(file);
            System.setOut(output);
            MostActiveCookie.main(new String[]{"all_cookies_same_frequency.csv", "-d", "2018-12-09"});
            output.close();
            printResult(file, new File("all_cookies_same_frequency_ref.txt"),
                    "testAllCookiesSameFrequency()");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Tests case for which cookie logs span many dates
    private static void testManyDates() {
        try {
            File file = new File("many_dates_test.txt");
            PrintStream output = new PrintStream(file);
            System.setOut(output);
            MostActiveCookie.main(new String[]{"many_dates.csv", "-d", "2018-12-11"});
            output.close();
            printResult(file, new File("many_dates_ref.txt"), "testManyDates()");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Tests input consisting of 100 cookie logs that match the given date
    private static void test100() {
        try {
            File file = new File("cookie_log_100_test.txt");
            PrintStream output = new PrintStream(file);
            System.setOut(output);
            MostActiveCookie.main(new String[]{"cookie_log_100.csv", "-d", "2018-12-07"});
            output.close();
            printResult(file, new File("cookie_log_100_ref.txt"), "test100()");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Tests input consisting of 1000 cookie logs that match the given date
    private static void test1000() {
        try {
            File file = new File("cookie_log_1000_test.txt");
            PrintStream output = new PrintStream(file);
            System.setOut(output);
            MostActiveCookie.main(new String[]{"cookie_log_1000.csv", "-d", "2018-12-07"});
            output.close();
            printResult(file, new File("cookie_log_1000_ref.txt"), "test1000()");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Given a test and reference files, compares the files and prints the result
    // of the test of the name testName.
    private static void printResult(File testFile, File refFile, String testName) {
        if (isEqual(testFile, refFile)) StdOut.println("    Passed Test: " + testName);
        else StdOut.println("  X Failed Test: " + testName);
    }

    // Compares files a, b line-by-line. Returns true if files are equal, returns false otherwise.
    private static boolean isEqual(File a, File b) {
        try {
            if (Files.size(a.toPath()) != Files.size(b.toPath())) return false;

            BufferedReader aReader = Files.newBufferedReader(a.toPath());
            BufferedReader bReader = Files.newBufferedReader(b.toPath());

            boolean isEmpty = false;
            while (!isEmpty) {
                String line = aReader.readLine();
                if (line == null) isEmpty = true;
                else if (!line.equals(bReader.readLine())) {
                    aReader.close();
                    bReader.close();
                    return false;
                }
            }
            aReader.close();
            bReader.close();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("DM_DEFAULT_ENCODING")
    public static void main(String[] args) {

        // Save default System.out
        StdOut = new PrintStream(new FileOutputStream(FileDescriptor.out));

        // Statement Testing
        StdOut.println("Running Statement Tests...");
        testRegularInput();

        // Boundary Testing
        StdOut.println("Running Boundary Tests...");
        testNoDateMatches();
        testAllDatesMatch();
        testNoCookieLogs();

        // Stress Testing
        StdOut.println("Running Stress Tests...");
        testAllCookiesSameFrequency();
        testManyDates();
        test100();
        test1000();

        StdOut.println("Tests completed.");
    }
}
