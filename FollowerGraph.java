import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

/**
 * The <code>FollowerGraph</code> represents the connections
 * of users following each other.
 *
 * @author Brian Chau
 *    email brian.chau@stonybrook.edu
 *    Stony Brook ID: 116125954
 *    Recitation: 02
 **/
public class FollowerGraph implements Serializable{
    private ArrayList<User> users;
    public static final int MAX_USERS = 100;
    private boolean[][] connections;
    private ArrayList<ArrayList<User>> knownLoops;

    /**
     * Creates an empty FollowersGraph and intializes
     * all declared variables.
     */
    public FollowerGraph(){
        users = new ArrayList<>();
        connections = new boolean[100][100];
        knownLoops = null;
    }

    /**
     * Adds a user to the users arraylist
     *
     * @param userName
     *    Username of new user
     * @throws IllegalArgumentException
     *    Thrown if the arraylist is full.
     */
    public void addUser(String userName) throws IllegalArgumentException{
        if(users.size() >= MAX_USERS){
            throw new IllegalArgumentException("Site can't handle more users");
        }
        User newUser = new User(userName);
        users.add(newUser);
    }

    /**
     * Adds a connection to the adjacency matrix
     *
     * @param userFrom
     *    The user sending a follow
     * @param userTo
     *    The user being follwed
     */
    public void addConnection(String userFrom, String userTo){
        connections[findIndices(userFrom)][findIndices(userTo)] = true;
    }

    /**
     * Removes a connection to the adjacency matrix
     *
     * @param userFrom
     *    The user unfollowing
     * @param userTo
     *    The user being unfollwed
     */
    public void removeConnection(String userFrom, String userTo){
        connections[findIndices(userFrom)][findIndices(userTo)] = false;
    }

    public String shortestPath(String userFrom, String userTo){
        return "";
    }

    public ArrayList<String> allPaths(String userFrom, String userTo){
        User start = users.get(findIndices(userFrom));
        ArrayList<User> startingList = new ArrayList<>();
        return recursiveAllPaths(start, users.get(findIndices(userTo)), startingList);
    }

    public ArrayList<String> findAllLoops(){
        ArrayList<String> allLoops = new ArrayList<>();
        for(User user: users){
            ArrayList<User> startingList = new ArrayList<>();
            allLoops.addAll(recursiveFindAllLoops(user, user, startingList));
        }
        return allLoops;
    }

    /**
     * Prints the users in an order based on the comparator given
     *
     * @param comp
     *    Comparator that will be used to sort the array
     */
    public void printAllUsers(Comparator comp){
        ArrayList<User> copy = new ArrayList<>(users);
        Collections.sort(copy, comp);
        System.out.printf("%-24s%-25s%s", "Users:", "Number of Followers", "Number Following\n");
        for(User user: copy){
            System.out.printf("%-35s%-26d%d\n", user.getUserName(), findFollowers(user), findFollowing(user));
        }
    }

    public void printAllFollowers(String userName){
        int userIndex = findIndices(userName);
        for(int i = 0; i < users.size(); i++){
            if(connections[userIndex][i]){
                System.out.print(users.get(i).toString() + ", ");
            }
        }
        System.out.println();
    }

    public void printAllFollowing(String userName){
        int userIndex = findIndices(userName);
        for(int i = 0; i < users.size(); i++){
            if(connections[i][userIndex]){
                System.out.print(users.get(i).toString() + ", ");
            }
        }
        System.out.println();
    }

    /**
     * Finds the number of people followed for a user
     *
     * @param user
     *    The user whose number of people followed will be returned
     * @return
     *    Number of people followed of the user
     */
    public int findFollowing(User user){
        int following = 0;
        int index = user.getIndexPos();
        for(int i = 0; i <users.size(); i++) {
            if (connections[i][index]){
                following++;
            }
        }
        return following;
    }

    /**
     * Returns the connection array of the followers
     *
     * @return
     *    The connection array
     */
    public boolean[][] getConnections() {

        return connections;
    }

    /**
     * Returns the list of the users
     *
     * @return
     *    List of users
     */
    public ArrayList<User> getUsers() {
        return users;
    }

    /**
     * Finds the number of followers for a user
     *
     * @param user
     *    The user whose number of followers will be returned
     * @return
     *    Number of followers of the user
     */
    public int findFollowers(User user){
        int followers = 0;
        int index = user.getIndexPos();
        for(int i = 0; i <users.size(); i++) {
            if (connections[index][i]){
                followers++;
            }
        }
        return followers;
    }

    public void loadAllUsers(String filename){
        try{
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream inStream = new ObjectInputStream(file);
            FollowerGraph loadedGraph;
            loadedGraph = (FollowerGraph) inStream.readObject();
            users = loadedGraph.getUsers();

        }
        catch(FileNotFoundException e){
            System.out.println("Could not find save");
        }
        catch(IOException | ClassNotFoundException e){
            System.out.println("Could not load file");
        }
    }

    public void loadAllConnections(String filename){
        try{
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream inStream = new ObjectInputStream(file);
            FollowerGraph loadedGraph;
            loadedGraph = (FollowerGraph) inStream.readObject();
            connections = loadedGraph.getConnections();

        }
        catch(FileNotFoundException e){
            System.out.println("Could not find save");
        }
        catch(IOException | ClassNotFoundException e){
            System.out.println("Could not load file");
        }
    }

    private int findIndices(String user){
        for(int i = 0; i < users.size(); i++){
            User u = users.get(i);
            if(u.getUserName().equals(user)){
                return i;
            }
        }
        return -1;
    }

    private boolean checkRepeat(ArrayList<User> testPath){
        if(knownLoops == null){
            knownLoops = new ArrayList<>();
            knownLoops.add(testPath);
            return true;
        }
        for(ArrayList<User> path: knownLoops){
            if(path.get(0) == testPath.get(testPath.size() - 1) && path.get(1) == testPath.get(0)){
                knownLoops.add(testPath);
                return true;
            }
        }
        return false;
    }

    private ArrayList<String> recursiveFindAllLoops(User startingUser, User curUser, ArrayList<User> curPath){
        ArrayList<String> loops = new ArrayList<>();
        if(curUser == startingUser && curPath.size() != 0){
            curPath.add(curUser);
            if(checkRepeat(curPath)){
                loops.add(createPath(curPath));
            }
            return loops;
        }
        if(curPath.size() == users.size()){
            return loops;
        }
        curPath.add(curUser);
        for(int i = 0; i < connections[curUser.getIndexPos()].length; i++){
            if(connections[curUser.getIndexPos()][i]){
                loops.addAll(recursiveFindAllLoops(startingUser, users.get(i), curPath));
            }
        }
        return loops;
    }

    private ArrayList<String> recursiveAllPaths(User curUser, User endUser, ArrayList<User> curPath){
        ArrayList<String> knownPaths = new ArrayList<>();
        if(curUser == endUser){
            curPath.add(curUser);
            String path = createPath(curPath);
            knownPaths.add(path);
            return knownPaths;
        }
        else if(curPath.contains(curUser)){
            return knownPaths;
        }
        curPath.add(curUser);
        for(int i = 0; i < users.size(); i++){
            if(connections[curUser.getIndexPos()][i]){
                knownPaths.addAll(recursiveAllPaths(users.get(i), endUser, curPath));
                curPath.remove(curPath.size() - 1);
            }
        }
        return knownPaths;
    }

    private String createPath(ArrayList<User> curPath){
        String path = "";
        for(User user: curPath){
            path += user.getUserName() + " -> ";
        }
        path = path.substring(0, path.lastIndexOf(" -> "));
        return path;
    }
}
