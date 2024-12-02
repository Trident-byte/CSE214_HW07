import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * The <code>FollowGraphDriver</code> is the interface for the
 * user to interact with the program
 *
 * @author Brian Chau
 *    email brian.chau@stonybrook.edu
 *    Stony Brook ID: 116125954
 *    Recitation: 02
 **/
public class FollowGraphDriver {
    /**
     * Interface for the user to interact with the graph
     *
     * @param args
     *    String args when used as a command
     */
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        FollowerGraph graph = new FollowerGraph();
        FileOutputStream file = null;
        try{
            FileInputStream test = new FileInputStream("follow_graph.obj");
            ObjectInputStream inStream = new ObjectInputStream(test);
            graph = (FollowerGraph) inStream.readObject();
            file = new FileOutputStream("follow_graph.obj");
        }
        catch(IOException | ClassNotFoundException e){
            System.out.println("follow_graph.obj is not found. New FollowGraph object will be created.");
            createNewFile("follow_graph.obj");
            try{
                file = new FileOutputStream("follow_graph.obj");
            }
            catch(FileNotFoundException t){
                System.out.println("Couldn't connect to file");
            }
        }
        // printMenu();
        boolean running = true;
        while(running){
            printMenu();
            String command = input.nextLine().strip().toUpperCase();
            if(command.equals("U")){
                addAUser(input, graph);
            }
            else if(command.equals("C")){
                addAConnection(input, graph);
            }
            else if(command.equals("AU")){
                loadUser(input, graph);
            }
            else if(command.equals("AC")){
                loadConnection(input, graph);
            }
            else if(command.equals("P")){
                printUsers(input, graph);
            }
            else if(command.equals("L")){
                printLoops(input, graph);
            }
            else if(command.equals("RU")){
                removeAUser(input, graph);
            }
            else if(command.equals("RC")){
                removeAConnection(input, graph);
            }
            else if(command.equals("SP")){
                findShortest(input, graph);
            }
            else if(command.equals("AP")){
                findEveryPath(input, graph);
            }
            else if(command.equals("Q")){
                save(file, graph);
                running = false;
            }
        }
    }

    /**
     * Prints menu of the program
     */
    private static void printMenu(){
        System.out.println("************ Menu ************");
        System.out.println("(U) Add User ");
        System.out.println("(C) Add Connection");
        System.out.println("(AU) Load all users");
        System.out.println("(AC) Load all connections");
        System.out.println("(P) Print all Users");
        System.out.println("(L) Print all Loops");
        System.out.println("(RU) Remove User");
        System.out.println("(RC) Remove Connection");
        System.out.println("(SP) Find Shortest Path");
        System.out.println("(AP) Find All Paths");
        System.out.println("(Q) Quit");
        System.out.print("Enter a Selection: ");
    }

    /**
     * Adds a user to the graph 
     * 
     * @param input
     *    Scanner to get name from user of program
     * @param graph
     *    Graph to be changed
     */
    private static void addAUser(Scanner input, FollowerGraph graph){
        String newUser = prompter(input, "Please enter the user to add: ");
        graph.addUser(newUser);
        System.out.println("User " + newUser + " has been added");
    }

    /**
     * Adds a connection to the graph 
     * 
     * @param input
     *    Scanner to get names from user of program
     * @param graph
     *    Graph to be changed
     */
    private static void addAConnection(Scanner input, FollowerGraph graph){
        String userFrom = prompter(input, "Please enter the source of the connection to add: ");
        String userTo = prompter(input, "Please enter the dest of the connection to add: ");
        graph.addConnection(userFrom, userTo);
        System.out.println("Connection between " + userFrom + " to " + userTo + " has been established");
    }

    /**
     * Loads users to the graph 
     * 
     * @param input
     *    Scanner to get name of file from the user of program
     * @param graph
     *    Graph to be changed
     */
    private static void loadUser(Scanner input, FollowerGraph graph){
        String fileName = prompter(input, "Enter the file name: ");
        graph.loadAllUsers(fileName);
    }

    /**
     * Loads connections to the graph 
     * 
     * @param input
     *    Scanner to get name of file from the user of program
     * @param graph
     *    Graph to be changed
     */
    private static void loadConnection(Scanner input, FollowerGraph graph){
        String fileName = prompter(input, "Enter the file name: ");
        graph.loadAllConnections(fileName);
    }

    /**
     * Prints out users based on the options given
     * 
     * @param input
     *    Scanner to get the option from user of the program
     * @param graph
     *    Graph to be used
     */
    private static void printUsers(Scanner input, FollowerGraph graph){
        String prompt = "(SA) Sort Users by Name\n";
        // prompt += "(SB) Sort Users by Number of Followers\n";
        // prompt += "(SC) Sort Users by Number of Following\n";
        // prompt += "(Q) Quit\n";
        // prompt += "Enter a Selection: ";
        boolean printing = true;
        while(printing){
            String option = prompter(input, prompt).strip().toUpperCase();
            if(option.equals("SA")){
                NameComparator nameComparator = new NameComparator();
                graph.printAllUsers(nameComparator);
            }
            else if(option.equals("SB")){
                FollowersComparator followersComparator = new FollowersComparator(graph);
                graph.printAllUsers(followersComparator);
            }
            else if(option.equals("SC")){
                FollowingComparator followingComparator = new FollowingComparator(graph);
                graph.printAllUsers(followingComparator);
            }
            else if(option.equals("Q")){
                printing = false;
            }
            printing = false;
        }
    }

    /**
     * Prints all the loops in a graph
     * 
     * @param input
     *    Used to recieve input from user
     * @param graph
     *    Graph to be used
     */
    private static void printLoops(Scanner input, FollowerGraph graph){
        ArrayList<String> list = graph.findAllLoops();
        if(list.isEmpty()){
            System.out.println("There are no loops.");
        }
        else if(list.size() == 1){
            System.out.println("There is 1 loop:");
        }
        else{
            System.out.println("There are a total of " + list.size() + " loops:");
        }
        printArrayList(list);
    }

    /**
     * Removes a user from the graph
     * 
     * @param input
     *    Scanner to recieve username of which user to remove
     * @param graph
     *    Graph to be changed
     */
    private static void removeAUser(Scanner input, FollowerGraph graph){
        String userToRemove = prompter(input, "Please enter the user to remove: ");
        graph.removeUser(userToRemove);
    }

    /**
     * Removes a connection from the graph
     * 
     * @param input
     *    Scanner to recieve usernames to find connection to remove
     * @param graph
     *    Graph to be changed
     */
    private static void removeAConnection(Scanner input, FollowerGraph graph){
        String startUser = getRequiredString(input, "Please enter the source of the connection to remove: ", graph);
        String endUser = getRequiredString(input, "Please enter the dest of the connection to remove: ", graph);
        graph.removeConnection(startUser, endUser);
    }

    /**
     * Finds the shortest path between two users from a graph
     * 
     * @param input
     *    Scanner to recieve usernames to find users to start and end path
     * @param graph
     *    Graph to be changed
     */
    private static void findShortest(Scanner input, FollowerGraph graph){
        String startUser = prompter(input, "Please enter the desired source: ");
        String endUser = prompter(input, "Please enter the desired destination: ");
        String path = graph.shortestPath(startUser, endUser);
        System.out.println(path);
    }

    /**
     * Finds every path between two users from a graph
     * 
     * @param input
     *     Scanner to recieve usernames to find users to start and end path
     * @param graph
     *     Graph to be changed
     */
    private static void findEveryPath(Scanner input, FollowerGraph graph){
        String startUser = prompter(input, "Please enter the desired source: ");
        String endUser = prompter(input, "Please enter the desired destination: ");
        ArrayList<String> list = graph.allPaths(startUser, endUser);
        System.out.println("There are a total of " + list.size() + " paths:");
        if(list != null){
            printArrayList(list);
        }
    }

    /**
     * Prompts the user given a prompt and input
     * @param input
     *    Scanner to get input
     * @param prompt
     *    Prompt to be given to the user
     * @return
     *    The input given by the user
     */
    private static String prompter(Scanner input, String prompt){
        System.out.print(prompt);
        String answer = "";
        if(input.hasNextLine()){
            answer = input.nextLine();
        }
        return answer;
    }

    /**
     * Prints out all paths given a list 
     * 
     * @param list
     *   List of string representation of paths
     */
    private static void printArrayList(ArrayList<String> list){
        Collections.sort(list);
        for(String path: list){
            System.out.println(path);
        }
    }

    /**
     * Saves graph to a file
     * 
     * @param file
     *    File to be saved to
     * @param graph
     *    Graph to be saved
     */
    private static void save(FileOutputStream file, FollowerGraph graph){
        try{
            ObjectOutputStream outStream = new ObjectOutputStream(file);
            outStream.writeObject(graph);
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Creates a new file given a file name
     * 
     * @param fileName
     *    Name of new file
     */
    private static void createNewFile(String fileName){
        File newFile = new File(fileName);
        try{
            newFile.createNewFile();
        } catch(Exception e){
            System.out.println("Couldn't create file");
        }
    }

    /**
     * Ensures that a valid user is given
     * 
     * @param input
     *    Scanner to recieve the input
     * @param prompt
     *    Prompt given to the user of the program
     * @param graph
     *    Graph to be checked to see if user gave a valid input
     * @return
     *    Valid input of a username by user of the program
     */
    private static String getRequiredString(Scanner input, String prompt, FollowerGraph graph){
        boolean validString = false;
        String ans = "";
        while(!validString){
            ans = prompter(input, prompt);
            if(ans.equals("")){
                System.out.println("You can not leave this field empty.");
                System.out.println("There is no user with this name, Please choose a valid user!");
            }
            else{
                try{
                    graph.findIndices(ans);
                    validString = true;
                }
                catch(Exception e){
                     System.out.println(e.getMessage());
                }
            }
        }
        return ans;
    }

}
