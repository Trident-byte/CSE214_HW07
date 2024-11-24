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
        boolean running = true;
        printMenu();
        while(running){
            System.out.print("Enter a selection: ");
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
                running = false;
            }
        }
    }

    private static void printMenu(){
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
    }

    private static void addAUser(Scanner input, FollowerGraph graph){
        String newUser = prompter(input, "Please enter the user to add: ");
        graph.addUser(newUser);
        System.out.println("User " + newUser + " has been added");
    }

    private static void addAConnection(Scanner input, FollowerGraph graph){
        String userFrom = prompter(input, "Please enter the source of the connection to add: ");
        String userTo = prompter(input, "Please enter the dest of the connection to add: ");
        graph.addConnection(userFrom, userTo);
        System.out.println("Connection between " + userFrom + " to " + userTo + " has been established");
    }

    private static void loadUser(Scanner input, FollowerGraph graph){

    }

    private static void loadConnection(Scanner input, FollowerGraph graph){

    }

    private static void printUsers(Scanner input, FollowerGraph graph){
        String prompt = "(SA) Sort Users by Name\n";
        prompt += "(SB) Sort Users by Number of Followers\n";
        prompt += "(SC) Sort Users by Number of Following\n";
        prompt += "(Q) Quit\n";
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
    }

    private static void printLoops(Scanner input, FollowerGraph graph){
        ArrayList<String> list = graph.findAllLoops();
        printArrayList(list);
    }

    private static void removeAUser(Scanner input, FollowerGraph graph){

    }

    private static void removeAConnection(Scanner input, FollowerGraph graph){

    }

    private static void findShortest(Scanner input, FollowerGraph graph){

    }

    private static void findEveryPath(Scanner input, FollowerGraph graph){
        String startUser = prompter(input, "Please enter the desired source: ");
        String endUser = prompter(input, "Please enter the desired destination: ");
        ArrayList<String> list = graph.allPaths(startUser, endUser);
        printArrayList(list);
    }

    private static String prompter(Scanner input, String prompt){
        System.out.print(prompt);
        String answer = "";
        if(input.hasNextLine()){
            answer = input.nextLine();
        }
        return answer;
    }

    private static void printArrayList(ArrayList<String> list){
        Collections.sort(list);
        for(String path: list){
            System.out.println(path);
        }
    }
}
