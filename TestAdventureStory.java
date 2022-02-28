// Title: AdventureStory
// Files: AdventureStory.java, TestAdventureStory.java
//
// Author: Jules Vigy
// Email: jules.vigy@aol.com

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;

public class TestAdventureStory {

    /**
     * This is the main method that runs the various tests.
     * 
     * @param args (unused)
     */
    public static void main(String[] args) {
        // Milestone 1 Tests
        testPromptInt();
        testPromptChar();
        testPromptString();
        testGetRoomIndex();
        testGetRoomDetails();

        // Milestone 2 Tests
        testParseStory();

        // Milestone 3 Tests
        testprobsTran();
    }

    /**
     * This runs some tests on the promptInt method.
     */

    private static void testPromptInt() {
        boolean error = false;

        {
            Scanner in = new Scanner("8\n");
            int expected = 8;
            int result = AdventureStory.promptInt(in, "Enter integer: ", 5, 15);
            if (expected != result) {
                System.out.println("1) testPromptInt expected: " + expected + " result: " + result);
                error = true;
            }
        }

        if (error) {
            System.out.println("testPromptInt failed");
        } else {
            System.out.println("testPromptInt passed");
        }
    }

    /**
     * This runs some tests on the promptString method.
     */
    private static void testPromptString() {
        boolean error = false;

        {
            Scanner in = new Scanner("foobar\n");
            String expected = "foobar";
            String result = AdventureStory.promptString(in, "Enter: ");
            if (!expected.equals(result)) {
                System.out
                    .println("1) testPromptString expected: " + expected + " result: " + result);
                error = true;
            }
        }

        if (error) {
            System.out.println("testPromptString failed");
        } else {
            System.out.println("testPromptString passed");
        }
    }

    /**
     * This runs some tests on the promptChar method.
     */
    private static void testPromptChar() {
        boolean error = false;

        {
            Scanner in = new Scanner("  foobar\n");
            char expected = 'f';
            char result = AdventureStory.promptChar(in, "Enter: ");
            if (expected != result) {
                System.out
                    .println("1) testPromptChar expected: " + expected + " result: " + result);
                error = true;
            }
        }

        if (error) {
            System.out.println("testPromptChar failed");
        } else {
            System.out.println("testPromptChar passed");
        }
    }


    /**
     * This runs some tests on the getRoomIndex method.
     */
    private static void testGetRoomIndex() {
        boolean error = false;

        {
            // Assuming normal Config.java
            ArrayList<String[]> in =
                new ArrayList<>(Arrays.asList(new String[][] {{"id1", "", ""}, {"id2", "", ""}}));
            int expected = -1;
            int result = AdventureStory.getRoomIndex(new String("id3"), in);
            if (expected != result) {
                System.out
                    .println("1) testGetRoomIndex expected: " + expected + " result: " + result);
                error = true;
            }
        }

        if (error) {
            System.out.println("testGetRoomIndex failed");
        } else {
            System.out.println("testGetRoomIndex passed");
        }
    }

    /**
     * This runs some tests on the getRoomIndex method.
     */
    private static void testGetRoomDetails() {
        boolean error = false;

        {
            // Assuming normal Config.java
            ArrayList<String[]> in =
                new ArrayList<>(Arrays.asList(new String[][] {{"id1", "", ""}, {"id2", "", ""}}));
            String[] expected = in.get(0);
            String[] result = AdventureStory.getRoomDetails(new String("id1"), in);
            if (!Arrays.equals(expected, result)) {
                System.out.println("1) testGetRoomDetails expected: " + Arrays.toString(expected)
                    + " result: " + Arrays.toString(result));
                error = true;
            }
        }

        if (error) {
            System.out.println("testGetRoomDetails failed");
        } else {
            System.out.println("testGetRoomDetails passed");
        }
    }

    /**
     * This runs some test on ParseStory
     */

    private static void testParseStory() {
        {
            Scanner testSc = new Scanner("#!STORY\n"
                + "R1: Room 1\nRoom 1 description\n;;;\n: Transition 1 -> 1\n: Transition 2 -> 2\n"
                + "R2: Room 2 \n Room 2 description \n;;;\n =) \n");
            ArrayList<String[]> arrRooms = new ArrayList<String[]>();
            ArrayList<ArrayList<String[]>> arrTrans = new ArrayList<ArrayList<String[]>>();
            String[] curRoom = new String[1];
            boolean passed = true;
            if (!AdventureStory.parseStory(testSc, arrRooms, arrTrans, curRoom)) {
                System.out.println("parseStory 1: returned false instead of true.");
                passed = false;
            }
            // Assuming normal Config.java
            // Expected ArrayList of room details:
            ArrayList<String[]> expRooms = new ArrayList<String[]>(Arrays.asList(new String[][] {
                {"1", "Room 1", "Room 1 description"}, {"2", "Room 2", "Room 2 description"}}));
            if (!compareArrayListsArrays(arrRooms, expRooms)) {
                System.out.println("parseStory 1: \nrooms ArrayList returned: \n"
                    + Arrays.deepToString(arrRooms.toArray()) + "\nExpected: \n"
                    + Arrays.deepToString(expRooms.toArray()) + "\n");
                passed = false;
            } // Expected ArrayList of ArrayList of transition details:
            ArrayList<String[]> room1Trans = new ArrayList<String[]>(Arrays
                .asList(new String[][] {{"Transition 1", "1", null}, {"Transition 2", "2", null}}));
            ArrayList<String[]> room2Trans =
                new ArrayList<String[]>(Arrays.asList(new String[][] {{"=)", null, null}}));
            ArrayList<ArrayList<String[]>> expTrans = new ArrayList<ArrayList<String[]>>();
            expTrans.add(room1Trans);
            expTrans.add(room2Trans);
            if (!compare2dArrayLists(arrTrans, expTrans)) {
                System.out.println("parseStory 1: \ntransition ArrayList returned: \n"
                    + toString2dArrayLists(arrTrans) + "\nExpected: \n"
                    + toString2dArrayLists(expTrans) + "\n");
                passed = false;
            }
            if (passed)
                System.out.println("parseStory 1: passed");
        }

        { // this test will check # being first character on a line in description is not a problem
            Scanner testSc = new Scanner("#!STORY\n"
                + "R1: Room 1\n#Room 1 description\n;;;\n: Transition 1 -> 1\n: Transition 2 -> 2\n"
                + "R2: Room 2 \n Room 2 description \n;;;\n\n\n\n\n\n\n =) \n");
            ArrayList<String[]> arrRooms = new ArrayList<String[]>();
            ArrayList<ArrayList<String[]>> arrTrans = new ArrayList<ArrayList<String[]>>();
            String[] curRoom = new String[1];
            boolean passed = true;
            if (!AdventureStory.parseStory(testSc, arrRooms, arrTrans, curRoom)) {
                System.out.println("parseStory 1: returned false instead of true.");
                passed = false;
            }
            // Assuming normal Config.java
            // Expected ArrayList of room details:
            ArrayList<String[]> expRooms = new ArrayList<String[]>(Arrays.asList(new String[][] {
                {"1", "Room 1", "#Room 1 description"}, {"2", "Room 2", "Room 2 description"}}));
            if (!compareArrayListsArrays(arrRooms, expRooms)) {
                System.out.println("parseStory 1: \nrooms ArrayList returned: \n"
                    + Arrays.deepToString(arrRooms.toArray()) + "\nExpected: \n"
                    + Arrays.deepToString(expRooms.toArray()) + "\n");
                passed = false;
            } // Expected ArrayList of ArrayList of transition details:
            ArrayList<String[]> room1Trans = new ArrayList<String[]>(Arrays
                .asList(new String[][] {{"Transition 1", "1", null}, {"Transition 2", "2", null}}));
            ArrayList<String[]> room2Trans =
                new ArrayList<String[]>(Arrays.asList(new String[][] {{"=)", null, null}}));
            ArrayList<ArrayList<String[]>> expTrans = new ArrayList<ArrayList<String[]>>();
            expTrans.add(room1Trans);
            expTrans.add(room2Trans);
            if (!compare2dArrayLists(arrTrans, expTrans)) {
                System.out.println("parseStory 1: \ntransition ArrayList returned: \n"
                    + toString2dArrayLists(arrTrans) + "\nExpected: \n"
                    + toString2dArrayLists(expTrans) + "\n");
                passed = false;
            }
            if (passed)
                System.out.println("parseStory 2: passed");
        }

        { // test checks to see if ignoring more than one comment is not a problem
            Scanner testSc = new Scanner("#!STORY\n" + "#FUNTIMES\n" + "#FUNTIMES\n"
                + "R1: Room 1\nRoom 1 description\n;;;\n: Transition 1 -> 1\n: Transition 2 -> 2\n"
                + "R2: Room 2 \n Room 2 description \n;;;\n =) \n");
            ArrayList<String[]> arrRooms = new ArrayList<String[]>();
            ArrayList<ArrayList<String[]>> arrTrans = new ArrayList<ArrayList<String[]>>();
            String[] curRoom = new String[1];
            boolean passed = true;
            if (!AdventureStory.parseStory(testSc, arrRooms, arrTrans, curRoom)) {
                System.out.println("parseStory 1: returned false instead of true.");
                passed = false;
            }
            // Assuming normal Config.java
            // Expected ArrayList of room details:
            ArrayList<String[]> expRooms = new ArrayList<String[]>(Arrays.asList(new String[][] {
                {"1", "Room 1", "Room 1 description"}, {"2", "Room 2", "Room 2 description"}}));
            if (!compareArrayListsArrays(arrRooms, expRooms)) {
                System.out.println("parseStory 1: \nrooms ArrayList returned: \n"
                    + Arrays.deepToString(arrRooms.toArray()) + "\nExpected: \n"
                    + Arrays.deepToString(expRooms.toArray()) + "\n");
                passed = false;
            }
            // Expected ArrayList of ArrayList of transition details
            ArrayList<String[]> room1Trans = new ArrayList<String[]>(Arrays
                .asList(new String[][] {{"Transition 1", "1", null}, {"Transition 2", "2", null}}));
            ArrayList<String[]> room2Trans =
                new ArrayList<String[]>(Arrays.asList(new String[][] {{"=)", null, null}}));
            ArrayList<ArrayList<String[]>> expTrans = new ArrayList<ArrayList<String[]>>();
            expTrans.add(room1Trans);
            expTrans.add(room2Trans);
            if (!compare2dArrayLists(arrTrans, expTrans)) {
                System.out.println("parseStory 1: \ntransition ArrayList returned: \n"
                    + toString2dArrayLists(arrTrans) + "\nExpected: \n"
                    + toString2dArrayLists(expTrans) + "\n");
                passed = false;
            }
            if (passed)
                System.out.println("parseStory 3: passed");

        }
    }

    /**
     * This runs some test on probsTran
     */
    private static void testprobsTran() {

        { // test for case probTrans returns a Room ID
            Random rand = new Random();
            ArrayList<String[]> trans = new ArrayList<String[]>();
            String[] str = {null, "Yup", "1"};
            String[] str1 = {null, null, "0"};
            trans.add(str);
            trans.add(str1);
            String checkIt = AdventureStory.probTrans(rand, trans);
            if (checkIt.equals("Yup")) {
                System.out.println("parseStory 4: passed");
            }
        }

        { // tests for when a string at Config.TRAN_PROB has incorrect format
            Random rand = new Random();
            ArrayList<String[]> trans = new ArrayList<String[]>();
            String[] str = {null, null, null};
            String[] str1 = {null, null, "0"};
            trans.add(str);
            trans.add(str1);
            String checkIt = AdventureStory.probTrans(rand, trans);
            if (checkIt == null) {
                System.out.println("parseStory 5: passed");
            }
        }
    }

    private static String toString2dArrayLists(ArrayList<ArrayList<String[]>> arrL1) {
        String toRet = "[\n";
        for (int i = 0; i < arrL1.size(); ++i) {
            toRet += "\t" + Arrays.deepToString(arrL1.get(i).toArray());
            if (i < arrL1.size() - 1)
                toRet += ",";
            toRet += "\n";
        }
        toRet += "]";
        return toRet;
    }

    private static boolean compareArrayListsArrays(ArrayList<String[]> arrL1,
        ArrayList<String[]> arrL2) {
        if (arrL1.size() != arrL2.size())
            return false;
        for (int i = 0; i < arrL1.size(); ++i) {
            if (!Arrays.deepEquals(arrL1.get(i), arrL2.get(i)))
                return false;
        }
        return true;
    }

    private static boolean compare2dArrayLists(ArrayList<ArrayList<String[]>> arrL1,
        ArrayList<ArrayList<String[]>> arrL2) {
        if (arrL1.size() != arrL2.size())
            return false;
        for (int i = 0; i < arrL1.size(); ++i) {
            if (!compareArrayListsArrays(arrL1.get(i), arrL2.get(i)))
                return false;
        }
        return true;
    }
}
