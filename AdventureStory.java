// Title: AdventureStory
// Files: AdventureStory.java, TestAdventureStory.java
//
// Author: Jules Vigy
// Email: jules.vigy@aol.com

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileInputStream;

public class AdventureStory {

    public enum ParseState {
        DEFAULT, ROOMS, TRANS
    }

    /**
     * Prompts the user for a value by displaying prompt. Note: This method should not add a new
     * line to the output of prompt.
     *
     * After prompting the user, the method will consume an entire line of input while reading an
     * int. If the value read is between min and max (inclusive), that value is returned. Otherwise,
     * "Invalid value." terminated by a new line is output and the user is prompted again.
     *
     * @param sc     The Scanner instance to read from System.in.
     * @param prompt The name of the value for which the user is prompted.
     * @param min    The minimum acceptable int value (inclusive).
     * @param max    The maximum acceptable int value (inclusive).
     * @return Returns the value read from the user.
     */
    public static int promptInt(Scanner sc, String prompt, int min, int max) {
        int userInt; // int used to store user input
        while (true) {
            // prompts user for int and checks that input is an int.
            System.out.print(prompt);
            if (sc.hasNextInt()) {
                userInt = sc.nextInt();
            } else { // discards line when input is not an int.
                System.out.println("Invalid value.");
                sc.nextLine();
                continue;
            }
            if ((min <= userInt) && (userInt <= max)) { // checks if input is within range.
                sc.nextLine();
                return userInt;
            } else {
                System.out.println("Invalid value.");
                continue;
            }
        }
    }

    /**
     * Prompts the user for a char value by displaying prompt.
     * Note: This method should not add a new line to the output of prompt. 
     *
     * After prompting the user, the method will read an entire line of input and return the first
     * non-whitespace character converted to lower case.
     *
     * @param sc The Scanner instance to read from System.in
     * @param prompt The user prompt.
     * @return Returns the first non-whitespace character (in lower case) read from the user. If 
     *         there are no non-whitespace characters read, the null character is returned.
     */
    public static char promptChar(Scanner sc, String prompt) {
        char userCharForReturn;
        String userString;
        System.out.print(prompt);
        // checks to see if there is something worth while to read
        if (sc.hasNextLine() && !(userString = sc.nextLine().trim()).equals("")) {
            userString = userString.trim(); // user line is read and trimmed
            userString = userString.toLowerCase();
            userCharForReturn = userString.charAt(0);
            return userCharForReturn; // returns first char of string
        } else {
            return '\0'; // if string is null null is returned.
        }
    }

    /**
     * Prompts the user for a string value by displaying prompt.
     * Note: This method should not add a new line to the output of prompt. 
     *
     * After prompting the user, the method will read an entire line of input, removing any leading and 
     * trailing whitespace.
     *
     * @param sc The Scanner instance to read from System.in
     * @param prompt The user prompt.
     * @return Returns the string entered by the user with leading and trailing whitespace removed.
     */
    public static String promptString(Scanner sc, String prompt) {
        String userString;
        System.out.print(prompt); // prompts user to enter something
        if (sc.hasNextLine()) {
            userString = sc.nextLine();
            userString = userString.trim(); // what user enters is read and trimmed
            return userString; // returns user input.
        } else {
            return null;
        }
    }

    /**
     * Saves the current position in the story to a file.
     *
     * The format of the bookmark file is as follows:
     * Line 1: The value of Config.MAGIC_BOOKMARK
     * Line 2: The filename of the story file from storyFile
     * Line 3: The current room id from curRoom
     *
     * Note: use PrintWriter to print to the file.
     *
     * @param storyFile The filename containing the cyoa story.
     * @param curRoom The id of the current room.
     * @param bookmarkFile The filename of the bookmark file.
     * @return false on an IOException, and true otherwise.
     */
    public static boolean saveBookmark(String storyFile, String curRoom, String bookmarkFile) {
        PrintWriter writeToStory = null;

        try { // writes to fill using user file name
            writeToStory = new PrintWriter(new FileWriter(bookmarkFile));
            writeToStory.println(Config.MAGIC_BOOKMARK);
            writeToStory.println(storyFile);
            writeToStory.println(curRoom);
            writeToStory.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * Loads the story and current location from a file either a story file or a bookmark file. 
     * NOTE: This method is partially implementd in Milestone 2 and then finished in Milestone 3.
     * 
     * The type of the file will be determined by reading the first line of the file. The first
     * line of the file should be trimmed of whitespace.
     *
     * If the first line is Config.MAGIC_STORY, then the file is parsed using the parseStory method.
     * If the first line is Config.MAGIC_BOOKMARK, the the file is parsed using the parseBookmark
     * method.
     * Otherwise, print an error message, terminated by a new line, to System.out, displaying: 
     * "First line: trimmedLineRead does not correspond to known value.", where trimmedLineRead is 
     * the trimmed value of the first line from the file. 
     *
     * If there is an IOException, print an error message, terminated by a new line, to System.out,
     * saying "Error reading file: fName", where fName is the value of the parameter.
     *
     * If there is an error reading the first line, print an error message, terminated by a new 
     * line, to System.out, displaying: "Unable to read first line from file: fName", where fName is
     * the value of the parameter. 
     *
     * This method will be partially implemented in Milestone #2 and completed in Milestone #3 as 
     * described below.
     *
     * Milestone #2: Open the file, handling the IOExceptions as described above. Do not read the
     * the first line: Assume the file is a story file and call the parseStory method.
     *
     * Milestone #3: Complete the implementation of this method by reading the first line from the
     * file and following the rules of the method as described above.
     *
     * @param fName The name of the file to read.
     * @param rooms The ArrayList structure that will contain the room details. A parallel ArrayList
     *              trans.
     * @param trans The ArrayList structure that will contain the transition details. A parallel 
     *              ArrayList to rooms. Since the rooms can have multiple transitions, each room 
     *              will be an ArrayList<String[]> with one String[] per transition with the 
     *              overall structure being an ArrayList of ArrayLists of String[].
     * @param curRoom An array of at least length 1. The current room id will be stored in the cell
     *                at index 0.
     * @return false if there is an IOException or a parsing error. Otherwise, true. 
     */
    public static boolean parseFile(String fName, ArrayList<String[]> rooms,
        ArrayList<ArrayList<String[]>> trans, String[] curRoom) {
        String firstLine;
        Scanner scan = null;
        FileInputStream fileText = null;
        boolean checkReturn = false;

        try { // opens file and feeds it into scanner
            File textFile = new File(fName);
            fileText = new FileInputStream(textFile);
            scan = new Scanner(fileText);
            // reads first line of file
            firstLine = scan.nextLine().trim();
            // block determines story type
            if (firstLine.equals(Config.MAGIC_STORY)) { // normal story format
                checkReturn = parseStory(scan, rooms, trans, curRoom);
            } else if (firstLine.equals(Config.MAGIC_BOOKMARK)) { // bookmark story
                if (scan.hasNextLine() && scan.hasNextLine()) {
                    checkReturn = parseBookmark(scan, rooms, trans, curRoom);
                } else {
                    scan.close();
                    return false;
                }
            } else {
                System.out
                    .println("First line: " + firstLine + " does not correspond to known value.");
                scan.close();
                return false;
            }
        }
        // catch block
        catch (IOException e) {
            System.out.println("Error reading file: " + fName);
            return false;
        } catch (NullPointerException e) {
            System.out.print("Unable to read first line from file: " + fName + " ");
            return false;
        } catch (Exception e) {
            return false;
        } finally { // closes scanner if need be
            if (checkReturn) {
                scan.close();
                return checkReturn;
            }
        }
        return false;
    }

    /**
     * Loads the story and the current room from a bookmark file. This method assumes that the first
     * line of the file, containing Config.MAGIC_BOOKMARK, has already been read from the Scanner.
     *
     * The format of a bookmark file is as follows:
     * Line No: Contents
     *       1: Config.MAGIC_BOOKMARK
     *       2: Story filename
     *       3: Current room id
     *
     * As an example, the following contents would load the story Goldilocks.story and set the 
     * current room to id 7.
     *
     * #!BOOKMARK
     * Goldilocks.story
     * 7
     *
     * Your method should not duplicate the code from the parseFile method. It must use the
     * parseFile method to populate the rooms and trans methods based on the contents of the story
     * filename read and trimmed from line 2 of the file. The value of for the cell at index 0 of 
     * curRoom is the trimmed value read on line 3 of the file.
     *
     * @param sc The Scanner object buffering the input file to read.
     * @param rooms The ArrayList structure that will contain the room details. A parallel ArrayList
     *              trans.
     * @param trans The ArrayList structure that will contain the transition details. A parallel 
     *              ArrayList to rooms.
     * @param curRoom An array of at least length 1. The current room id will be stored in the cell
     *                at index 0.
     * @return false if there is a parsing error. Otherwise, true. 
     */
    public static boolean parseBookmark(Scanner sc, ArrayList<String[]> rooms,
        ArrayList<ArrayList<String[]>> trans, String[] curRoom) {
        boolean returnStatement = false;
        String currentRoomUpdate = "";
        String fileName = "";
        // reads the first two lines of input and then calls parseFile with that info.
        fileName = sc.nextLine().trim();
        currentRoomUpdate = sc.nextLine().trim();
        curRoom[0] = currentRoomUpdate;
        returnStatement = parseFile(fileName, rooms, trans, curRoom);
        return returnStatement;
    }

    /**
     * This method parses a story adventure file.
     *
     * The method will read the contents from the Scanner, line by line, and populate the parallel 
     * ArrayLists rooms and trans. As such the story files have a specific structure. The order of
     * the rooms in the story file correspond to the order in which they will be stored in the 
     * parallel ArrayLists.
     *
     * When reading the file line-by-line, whitespace at the beginning and end of the line should be
     * trimmed. The file format described below assumes that whitespace has been trimmed.
     *
     * Story file format:
     *
     * - Any line (outside of a room's description) that begins with a '#' is considered a comment 
     *   and should be ignored.
     * - Room details begin with a line starting with 'R' followed by the room id, terminated with 
     *   a ':'. Everything  after the first colon is the room title. The substrings of the room id 
     *   and the room title should be trimmed.
     * - The room description begins on the line immediate following the line prefixed with 'R',
     *   containing the room id, and continues until a line of ";;;" is read.
     *   - The room description may be multi-line. Every line after the first one, should be 
     *     prefixed with a newline character ('\n'), and concatenated to the previous description 
     *     lines read for the current room.
     * - The room transitions begin immediately after the line of ";;;", and continue until a line
     *   beginning with 'R' is encountered. There are 3 types of transition lines:
     *   - 1 -- Terminal Transition: A terminal transition is either Config.SUCCESS or 
     *                               Config.FAIL. This room is the end of the story. 
     *                               This value should be stored as a transition with the String at
     *                               index Config.TRAN_DESC set to the value read. The rest of the 
     *                               Strings in the transition String array should be null.
     *                               A room with a terminal transition can only have one transition 
     *                               associated with it. Any additional transitions should result in
     *                               a parse error.rrr
     *   - 2 -- Normal Transition: The line begins with ':' followed by the transition description, 
     *                             followed by " -> " (note the spaces), followed by the room id to 
     *                             transition to. For normal transitions (those without a transition
     *                             weight), set the value at index Config.TRAN_PROB to null.
     *   - 3 -- Weighted Transition: Similar to a normal transition except that there is a 
     *                               probability weight associated with the transition. After the 
     *                               room id (as described in the normal transition) is a '?' 
     *                               followed by the probability weight. 
     *   - You can assume that room ids do not contain a '?'.
     *   - You can assume that Config.SUCCESS and Config.FAIL do not start with a ':'.
     *
     * In the parallel ArrayLists rooms and trans, the internal structures are as follows:
     *
     * The String array structure for each room has a length of Config.ROOM_DET_LEN. The entries in
     * the array are as follows:
     * Index              | Description
     * --------------------------------------------
     * Config.ROOM_ID     | The room id
     * Config.ROOM_TITLE  | The room's title
     * Config.ROOM_DESC   | The room's description
     *
     * The String array structure for each transition. Note that each room can have multiple 
     * transitions, hence, the ArrayList of ArrayLists of String[]. The length of the String[] is
     * Config.TRAN_DET_LEN. The entries in the String[] are as follows:
     * Index               | Description
     * ------------------------------------------------------------------
     * Config.TRAN_DESC    | The transition description
     * Config.TRAN_ROOM_ID | The transition destination (id of the room) 
     * Config.TRAN_PROB    | The probability weight for the transition
     *
     * If you encounter a line that violates the story file format, the method should print out an 
     * error message, terminated by a new line, to System.out displaying: 
     * "Error parsing file on line: lineNo: lineRead", where lineNo is the number of lines read
     * by the parseStory method (i.e. ignoring the magic number if Milestone #3), and lineRead is 
     * the offending trimmed line read from the Scanner.
     *
     * After parsing the file, if rooms or trans have zero size, or they have different sizes, print
     * out an error message, terminated by a new line, to System.out displaying:
     * "Error parsing file: rooms or transitions not properly parsed."
     *
     * After parsing the file, if curRoom is not null, store the reference of the id of the room at 
     * index 0 of the rooms ArrayList into the cell at index 0 of curRoom. 
     *
     * Hint: This method only needs a single loop, reading the file line-by-line.
     * 
     * Hint: To successfully parse the file, you will need to maintain a state of where you are in 
     *       the file. I.e., are you parsing the description, parsing the transitions; is there an 
             error; etc? One suggestion would be to use an enum to enumerate the different states. 
     *
     * @param sc The Scanner object buffering the input file to read.
     * @param rooms The ArrayList structure that will contain the room details.
     * @param trans The ArrayList structure that will contain the transition details.
     * @param curRoom An array of at least length 1. The current room id will be stored in the cell
     *                at index 0.
     * @return false if there is a parsing error. Otherwise, true. 
     */
    public static boolean parseStory(Scanner sc, ArrayList<String[]> rooms,
        ArrayList<ArrayList<String[]>> trans, String[] curRoom) {
        String[] arrStringHelper = new String[Config.ROOM_DET_LEN];
        String readText;
        String roomDesc = "";
        int linesReadCounter = 0;
        ParseState textFileState = ParseState.DEFAULT;

        while (sc.hasNextLine()) {
            linesReadCounter += 1; // keeps track of what line No. is read.
            // reads and checks is line is empty when in a room state
            if (((readText = sc.nextLine()).isEmpty()) && (textFileState == ParseState.ROOMS)) {
                roomDesc = roomDesc + "\n";
                continue;
            } // if line is simply empty then continue
            if (readText.isEmpty()) {
                continue;
            } // trims the input
            readText = readText.trim();
            // if hashtag then discard input and continue reading.
            if (readText.charAt(0) == '#' && (textFileState != ParseState.ROOMS)) {
                readText = null;
                continue;
            } // first checks for default state
            if (textFileState == ParseState.DEFAULT) {
                if (readText.startsWith("R")) { // rooms desc?
                    arrStringHelper = new String[Config.ROOM_DET_LEN];
                    arrStringHelper[Config.ROOM_ID] =
                        readText.substring(readText.indexOf('R') + 1, readText.indexOf(':')).trim();
                    arrStringHelper[Config.ROOM_TITLE] =
                        readText.substring(readText.indexOf(':') + 1, readText.length()).trim();
                    textFileState = ParseState.ROOMS;
                    rooms.add(arrStringHelper);
                    trans.add(new ArrayList<>()); // adds another AL<> to keep rooms == trans
                }
            } // checks to see if in Rooms state and adds the to trans.
            else if (textFileState == ParseState.ROOMS) {
                if (readText.startsWith(";;;")) {
                    rooms.get(rooms.size() - 1)[Config.ROOM_DESC] = roomDesc.trim();
                    roomDesc = "";
                    textFileState = ParseState.TRANS;
                } else { // if room desc has a space
                    roomDesc += readText;
                    roomDesc += '\n';
                }
            } else { // block sets rooms state and adds the string[] it builds
                if (readText.startsWith("R")) {
                    arrStringHelper = new String[Config.ROOM_DET_LEN];
                    arrStringHelper[Config.ROOM_ID] =
                        readText.substring(readText.indexOf('R') + 1, readText.indexOf(':')).trim();
                    arrStringHelper[Config.ROOM_TITLE] =
                        readText.substring(readText.indexOf(':') + 1, readText.length()).trim();
                    textFileState = ParseState.ROOMS;
                    rooms.add(arrStringHelper);
                    trans.add(new ArrayList<>()); // adds another AL<> to keep rooms == trans
                } // checks for a trans state with a probability and adds to trans

                else if (readText.startsWith(":") && readText.contains("?")) {
                    arrStringHelper = new String[Config.TRAN_DET_LEN];
                    textFileState = ParseState.TRANS;
                    arrStringHelper[Config.TRAN_DESC] =
                        readText.substring(readText.indexOf(':') + 1, readText.indexOf('-')).trim();
                    arrStringHelper[Config.TRAN_ROOM_ID] = readText
                        .substring(readText.indexOf('>') + 2, readText.lastIndexOf('?') - 1).trim();
                    arrStringHelper[Config.TRAN_PROB] =
                        readText.substring(readText.lastIndexOf('?') + 1, readText.length()).trim();
                    trans.get(trans.size() - 1).add(arrStringHelper);
                } // for normal trans and adds to trans

                else if (readText.startsWith(":")) {
                    arrStringHelper = new String[Config.TRAN_DET_LEN];
                    textFileState = ParseState.TRANS;
                    arrStringHelper[Config.TRAN_DESC] =
                        (readText.substring(readText.indexOf(':') + 2, readText.indexOf('-') - 1))
                            .trim();
                    arrStringHelper[Config.TRAN_ROOM_ID] =
                        (readText.substring(readText.indexOf('>') + 2, readText.length())).trim();
                    arrStringHelper[Config.TRAN_PROB] = null;
                    trans.get(trans.size() - 1).add(arrStringHelper);
                } // looks for terminal transitions and adds to trans

                else if ((readText.equals(Config.SUCCESS) || (readText.equals(Config.FAIL)))) {
                    arrStringHelper = new String[Config.TRAN_DET_LEN];
                    textFileState = ParseState.TRANS;
                    arrStringHelper[Config.TRAN_DESC] = readText;
                    arrStringHelper[Config.TRAN_ROOM_ID] = null;
                    arrStringHelper[Config.TRAN_PROB] = null;
                    trans.get(trans.size() - 1).add(arrStringHelper);
                } else { // line was unable to be read error message is given and input is cleared
                    System.out.println(
                        "Error parsing file on line: " + linesReadCounter + ": " + readText);
                    readText = "";
                }
            }
        } // checks for empty curRoom (parseBookmark != empty)
        if (curRoom[0] == null) {
            curRoom[0] = rooms.get(0)[Config.ROOM_ID];
        }
        return true;
    }

    /**
     * Returns the index of the given room id in an ArrayList of rooms. 
     *
     * Each entry in the ArrayList contain a String array, containing the details of a room. The 
     * String array structure, which has a length of Config.ROOM_DET_LEN, and has the following 
     * entries:
     * Index              | Description
     * --------------------------------------------
     * Config.ROOM_ID     | The room id
     * Config.ROOM_TITLE  | The room's title
     * Config.ROOM_DESC   | The room's description
     *
     * @param id The room id to search for.
     * @param rooms The ArrayList of rooms.
     * @return The index of the room with the given id if found in rooms. Otherwise, -1.
     */
    public static int getRoomIndex(String id, ArrayList<String[]> rooms) {
        String[] tempString = null;
        for (int i = 0; i < rooms.size(); i++) { // iterates through arrayList
            tempString = rooms.get(i);
            if (tempString[Config.ROOM_ID].equals(id)) { // checks index 0 of string[]
                return i; // if room designation matches that index of arrayList returned.
            }
        }
        return -1;
    }

    /**
     * Returns the room String array of the given room id in an ArrayList of rooms. 
     *
     * Remember to avoid code duplication!
     *
     * @param id The room id to search for.
     * @param rooms The ArrayList of rooms.
     * @return The reference to the String array in rooms with the room id of id. Otherwise, null.
     */
    public static String[] getRoomDetails(String id, ArrayList<String[]> rooms) {
        String[] tempString = null;
        for (int i = 0; i < rooms.size(); i++) {
            tempString = rooms.get(i); // checks to see which element of arrayList matches.
            if (tempString[Config.ROOM_ID].equals(id)) {
                return rooms.get(i); // returns String[] of correct index.
            }
        }
        return null;
    }

    /**
     * Prints out a line of characters to System.out. The line should be terminated by a new line.
     *
     * @param len The number of times to print out c. 
     * @param c The character to print out.
     */
    public static void printLine(int len, char c) {
        for (int i = 0; i < len; i++) {
            System.out.print(c);
        }
    }

    /**
     * Prints out a String to System.out, formatting it into lines of length no more than len 
     * characters.
     * 
     * This method will need to print the string out character-by-character, counting the number of
     * characters printed per line. 
     * If the character to output is a newline, print it out and reset your counter.
     * If it reaches the maximum number of characters per line, len, and the next character is:
     *   - whitespace (as defined by the Character.isWhitespace method): print a new line 
     *     character, and move onto the next character.
     *   - NOT a letter or digit (as defined by the Character.isLetterOrDigit method): print out the
     *     character, a new line, and move onto the next character.
     *   - Otherwise:
     *       - If the previous character is whitespace, print a new line then the character.
     *       - Otherwise, print a '-', a new line, and then the character. 
     * Remember to reset the counter when starting a new line. 
     *
     * After printing out the characters in the string, a new line is output.
     *
     * @param len The maximum number of characters to print out.
     * @param val The string to print out.
     */
    public static void printString(int len, String val) {
        int lineCounter = 0; // keeps track of char for width
        for (int i = 0; i < val.length(); i++) {
            if (val.charAt(i) == '\n') { // if char is \n print one "\n"
                System.out.println("");
                lineCounter = 0;
                continue;
            }
            if (lineCounter == len - 1) { // checks length
                if (Character.isWhitespace(val.charAt(i))) {
                    System.out.println("");
                    lineCounter = 0; // resets counter to zero if length is reached
                } else if (!Character.isLetterOrDigit(val.charAt(i))) {
                    System.out.println(val.charAt(i));
                    lineCounter = 0; // resets counter to zero if length is reached
                } else if (Character.isWhitespace(val.charAt(i - 1))) {
                    System.out.println("");
                    System.out.print(val.charAt(i));
                    lineCounter = 1; // resets counter to one
                } else {
                    System.out.print("-");
                    System.out.println("");
                    System.out.print(val.charAt(i));
                    lineCounter = 1; // resets counter to one
                }
            } else { // else this is how most chars are printed
                System.out.print(val.charAt(i));
                lineCounter += 1;
            }
        }
    }

    /**
     * This method prints out the room title and description to System.out. Specifically, it first
     * loads the room details, using the getRoomDetails method. If no room is found, the method
     * should return, avoiding any runtime errors.
     *
     * If the room is found, first a line of Config.LINE_CHAR of length Config.DISPLAY_WIDTH is 
     * output. Followed by the room's title, a new line, and the room's description. Both the title
     * and the description should be printed using the printString method with a maximum length of
     * Config.DISPLAY_WIDTH. Finally, a line of Config.LINE_CHAR of length Config.DISPLAY_WIDTH is 
     * output.
     *
     * @param id Room ID to display
     * @param rooms ArrayList containing the room details.
     */
    public static void displayRoom(String id, ArrayList<String[]> rooms) {
        String[] strArr = getRoomDetails(id, rooms);

        if (strArr == null) {
            return;
        } // prints rows of "-"
        printLine(Config.DISPLAY_WIDTH, Config.LINE_CHAR); // top line
        System.out.println("");
        // prints rooms title
        printString(Config.DISPLAY_WIDTH, strArr[Config.ROOM_TITLE]);
        System.out.println("");
        System.out.println("");
        // prints room desc
        printString(Config.DISPLAY_WIDTH, strArr[Config.ROOM_DESC]);
        System.out.println("");
        // prints row of "-"
        printLine(Config.DISPLAY_WIDTH, Config.LINE_CHAR); // bottom line
        System.out.println("");
        return;
    }

    /**
     * Prints out and returns the transitions for a given room. 
     *
     * If the room ID of id cannot be found, nothing should be output to System.out and null should
     * be returned.
     *
     * If the room is a terminal room, i.e., the transition list is consists of only a single 
     * transition with the value at index Config.TRAN_DESC being either Config.SUCCESS or 
     * Config.FAIL, nothing should be printed out.
     *
     * The transitions should be output in the same order in which they are in the ArrayList, and 
     * only if the transition probability (String at index TRAN_PROB) is null. Each transition 
     * should be output on its own line with the following format:
     * idx) transDesc
     * where idx is the index in the transition ArrayList and transDesc is the String at index 
     * Config.TRAN_DESC in the transition String array.
     *
     * See parseStory method for the details of the transition String array.
     *
     * @param id The room id of the transitions to output and return.
     * @param rooms The ArrayList structure that contains the room details.
     * @param trans The ArrayList structure that contains the transition details.
     * @return null if the id cannot be found in rooms. Otherwise, the reference to the ArrayList of
     *         transitions for the given room.
     */
    public static ArrayList<String[]> displayTransitions(String id, ArrayList<String[]> rooms,
        ArrayList<ArrayList<String[]>> trans) {
        int check = getRoomIndex(id, rooms);
        if (check < 0) { // if index not found return null
            return null;
        } // if terminal transition return current tran
        if (trans.get(check).get(0)[Config.TRAN_DESC].equals(Config.SUCCESS)
            || trans.get(check).get(0)[Config.TRAN_DESC].equals(Config.FAIL)) {
            return trans.get(check);
        } // prints out each transition for a given room
        for (int j = 0; j < trans.get(check).size(); j++) {
            if (trans.get(check).get(0)[Config.TRAN_PROB] == null) {
                System.out.print(j + ") ");
                System.out.println(trans.get(check).get(j)[Config.TRAN_DESC]);
            }
        }
        return trans.get(check); // return the AL for the given index to curTran in Main
    }

    /**
     * Returns the next room id, selected randomly based on the transition probability weights.
     *
     * If curTrans is null or the total sum of all the probability weights is 0, then return null. 
     * Use Integer.parseInt to convert the Strings at index Config.TRAN_PROB of the transition
     * String array to integers. If there is a NumberFormatException, return null.
     *
     * It is important to follow the specifications of the random process exactly. Any deviation may
     * result in failed tests. The random transition work as follows:
     *   - Let totalWeight be the sum of the all the transition probability weights in curTrans.
     *   - Draw a random integer between 0 and totalWeight - 1 (inclusive) from rand.
     *   - From the beginning of the ArrayList curTrans, start summing up the transition probability 
     *     weights.
     *   - Return the String at index Config.TRAN_ROOM_ID of the first transition that causes the 
     *     running sum of probability weights to exceed the random integer.   
     *
     * See parseStory method for the details of the transition String array.
     *
     * @param rand The Random class from which to draw random values.
     * @param curTrans The ArrayList structure that contains the transition details.
     * @return The room id that was randomly selected if the sum of probabilities is greater than 0.
     *         Otherwise, return null. Also, return null if there is a NumberFormatException. 
     */
    public static String probTrans(Random rand, ArrayList<String[]> curTrans) {
        if (curTrans == null) { // if curTran null return null
            return null;
        }
        try { // try black catching NumberFormatException
            int totalWeight = 0;
            int randomNumber;
            for (int i = 0; i < curTrans.size(); i++) { // loops to add up total weight
                totalWeight += Integer.parseInt(curTrans.get(i)[Config.TRAN_PROB]);
            }
            if (totalWeight == 0) {
                return null;
            }
            randomNumber = rand.nextInt(totalWeight); // generates random int
            int j;
            int runningTotal = 0;
            for (j = 0; j < curTrans.size(); ++j) {
                runningTotal += Integer.parseInt(curTrans.get(j)[Config.TRAN_PROB]);
                if (runningTotal > randomNumber) { // runningTotal exceeds random break and return
                    break;
                }
            }
            return curTrans.get(j)[Config.TRAN_ROOM_ID];
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * This is the main method for the Story Adventure game. It consists of the main game loop and
     * play again loop with calls to the various supporting methods. This method will evolve over 
     * the 3 milestones.
     * 
     * The Scanner object to read from System.in and the Random object with a seed of Config.SEED 
     * will be created in the main method and used as arguments for the supporting methods as 
     * required.
     *
     * Milestone #1:
     *   - Print out the welcome message: "Welcome to this choose your own adventure system!"
     *   - Begin the play again loop:
     *       - Prompt for a filename using the promptString method with the prompt:
     *         "Please enter the story filename: "
     *       - Prompt for a char using the promptChar method with the prompt:
     *         "Do you want to try again? "
     *   - Repeat until the character returned by promptChar is an 'n'
     *   - Print out "Thank you for playing!", terminated by a newline.
     *
     * Milestone #2:
     *   - Print out the welcome message: "Welcome to this choose your own adventure system!"
     *   - Begin the play again loop:
     *       - Prompt for a filename using the promptString method with the prompt:
     *         "Please enter the story filename: "
     *       - If the file is successfully parsed using the parseFile method:
     *            - Begin the game loop with the current room ID being that in the 0 index of the 
     *              String array passed into the parseFile method as the 4th parameter
     *                 - Output the room details via the displayRoom method
     *                 - Output the transitions via the displayTransitions method
     *                 
     *                 - If the current transition is not terminal:
     *                   - Prompt the user for a number between -1 and the number of transitions 
     *                     minus 1, using the promptInt method with a prompt of "Choose: "
     *                   - If the returned value is -1:
     *                      - read a char using promptChar with a prompt of
     *                        "Are you sure you want to quit the adventure? "
     *                      - Set the current room ID to Config.FAIL if that character returned is 'y'
     *                   - Otherwise: Set the current room ID to the room ID at index 
     *                                Config.TRAN_ROOM_ID of the selected transition.
     *                 - Otherwise, the current transition is terminal: Set the current room ID to 
     *                   the terminal state in the transition String array.
     *            - Continue the game loop until the current room ID is Config.SUCCESS or
     *              Config.FAIL
     *            - If the current room ID is Config.FAIL, print out the message (terminated by a 
     *              line): "You failed to complete the adventure. Better luck next time!"
     *            - Otherwise: print out the message (terminated by a line): 
     *              "Congratulations! You successfully completed the adventure!"
     *       - Prompt for a char using the promptChar method with the prompt:
     *         "Do you want to try again? "
     *   - Repeat until the character returned by promptChar is an 'n'
     *   - Print out "Thank you for playing!", terminated by a newline.
     *
     * Milestone #3:
     *   - Print out the welcome message: "Welcome to this choose your own adventure system!"
     *   - Begin the play again loop:
     *       - Prompt for a filename using the promptString method with the prompt:
     *         "Please enter the story filename: "
     *       - If the file is successfully parsed using the parseFile method:
     *            - Begin the game loop with the current room ID being that in the 0 index of the 
     *              String array passed into the parseFile method as the 4th parameter
     *                 - Output the room details via the displayRoom method
     *                 - Output the transitions via the displayTransitions method
     *                 - If the current transition is not terminal:
     *                   - If the value returnd by the probTrans method is null:
     *                     - Prompt the user for a number between -2 and the number of transitions 
     *                       minus 1, using the promptInt method with a prompt of "Choose: "
     *                     - If the returned value is -1:
     *                        - read a char using promptChar with a prompt of
     *                          "Are you sure you want to quit the adventure? "
     *                        - Set the current room ID to Config.FAIL if that character returned is 
     *                          'y'
     *                     - If the returned value is -2:
     *                        - read a String using the promptString method with a prompt of:
     *                          "Bookmarking current location: curRoom. Enter bookmark filename: ", 
     *                          where curRoom is the current room ID.
     *                        - Call the saveBookmark method and output (terminated by a new line):
     *                           - if successful: "Bookmark saved in fSave"
     *                           - if unsuccessful: "Error saving bookmark in fSave"
     *                       where fSave is the String returned by promptString.
     *                     - Otherwise: Set the current room ID to the room id at index 
     *                                  Config.TRAN_ROOM_ID of the selected transition.
     *                   - Otherwise, the value returned by probTrans is not null: make this value
     *                     the current room ID.
     *            - Continue the game loop until the current room ID is Config.SUCCESS or
     *              Config.FAIL.
     *            - If the current room ID is Config.FAIL, print out the message (terminated by a 
     *              line): "You failed to complete the adventure. Better luck next time!"
     *            - Otherwise: print out the message (terminated by a line): 
     *              "Congratulations! You successfully completed the adventure!"
     *       - Prompt for a char using the promptChar method with the prompt:
     *         "Do you want to try again? "
     *   - Repeat until the character returned by promptChar is an 'n'
     *   - Print out "Thank you for playing!", terminated by a newline.
     *
     * @param args Unused
     */
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Random rand = new Random(Config.SEED);
        boolean gameStatus = true; // for initial prompt and reprompt
        boolean gameOver = false;
        boolean gameTime = true;
        char userChar;
        ArrayList<String[]> rooms = new ArrayList<String[]>();
        ArrayList<ArrayList<String[]>> trans = new ArrayList<ArrayList<String[]>>();
        ArrayList<String[]> curTrans = null;
        String[] curRoom = new String[1];
        String randomTran = "";
        Integer userInt = null; // stores user int

        System.out.println("Welcome to this choose your own adventure system!");
        String userFile = "";
        // loop to promt for intial file
        while (gameStatus) {
            userFile = promptString(scan, "Please enter the story filename: ");
            boolean gameStatusInitial = parseFile(userFile, rooms, trans, curRoom);
            if (!gameStatusInitial) { // if parseFile return false
                userChar = promptChar(scan, "Do you want to try again? ");
                if (userChar == 'n') { // does not want to play
                    System.out.println("Thank you for playing!");
                    gameTime = false;
                    break;
                } else {
                    continue;
                }
            } else {
                break;
            }
        }
        do { // main game loop
            if (userInt != null) {
                curRoom[0] = curTrans.get(userInt)[Config.TRAN_ROOM_ID];
            }
            displayRoom(curRoom[0], rooms);
            curTrans = displayTransitions(curRoom[0], rooms, trans);
            if (curRoom[0] != null) {
                // if user has won the game
                if (curTrans.get(0)[Config.TRAN_DESC].equals(Config.SUCCESS)
                    || curRoom[0].equals(Config.SUCCESS)) {
                    System.out
                        .println("Congratulations! You successfully completed the adventure!");
                    gameOver = true;
                    gameTime = false;
                } // if user has lost the game
                else if (curTrans.get(0)[Config.TRAN_DESC].equals(Config.FAIL)
                    || curRoom[0].equals(Config.FAIL)) {
                    System.out
                        .println("You failed to complete the adventure. Better luck next time!");
                    gameOver = true;
                    gameTime = false;
                }
            } // black checks if random is to be selected or prompts user for int
            if ((gameTime) && ((randomTran = probTrans(rand, curTrans)) == null)) {
                userInt = promptInt(scan, "Choose: ", -2, curTrans.size() - 1);
                if (userInt == -1) {
                    userChar = promptChar(scan, "Are you sure you want to quit the adventure? ");
                    if (userChar == 'y') {
                        curRoom[0] = Config.FAIL;
                        System.out.println(
                            "You failed to complete the adventure. Better luck next time!");
                        // sets all the variable for a new game to be played
                        gameTime = false;
                        userInt = null;
                        gameOver = true;
                    } else { // if users does not want to quiet
                        userInt = null; // user int is set to null in order to display same room
                    }
                } else if (userInt == -2) { // if -2 is typed when prompted for a int
                    String fileName = promptString(scan, "Bookmarking current location: "
                        + curRoom[0] + ". " + "Enter bookmark filename: ");
                    boolean saveBookMarkStatus = saveBookmark(userFile, curRoom[0], fileName);
                    if (saveBookMarkStatus) {
                        System.out.println("Bookmark saved in " + fileName);
                        userInt = null; // userInt is set to null in order to display same room
                    } else {
                        System.out.println("Error saving bookmark in " + fileName);
                        System.out.println("hey");
                        userInt = null; // userInt is set to null in order to display same room
                    }
                }
            } else {
                curRoom[0] = randomTran;
            }
            // loop that is engages once a terminal state is activated
            while (gameOver) {
                userChar = promptChar(scan, "Do you want to try again? ");
                if (userChar == 'n') {
                    System.out.println("Thank you for playing!");
                    gameTime = false;
                    break;
                } else { // calls for a new file name and calls parseFile
                    curRoom = new String[1];
                    rooms = new ArrayList<String[]>();
                    trans = new ArrayList<ArrayList<String[]>>();
                    curTrans = null;
                    userFile = promptString(scan, "Please enter the story filename: ");
                    gameStatus = parseFile(userFile, rooms, trans, curRoom);
                    if (gameStatus) {
                        gameOver = false;
                        gameTime = true;
                        userInt = null;
                    } else {
                        continue;
                    }
                }
            }
        } while (gameTime); // game status check
    }
}
