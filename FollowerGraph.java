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
        newUser.setIndexPos(users.indexOf(newUser));
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
        try{
            connections[findIndices(userTo)][findIndices(userFrom)] = true;
        }
        catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
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
        try{
            connections[findIndices(userTo)][findIndices(userFrom)] = false;
        }
        catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Removes a user from the adjacency matrix
     * 
     * @param userToRemove
     *    Name of user to remove
     */
    public void removeUser(String userToRemove){
        try{
            int index = findIndices(userToRemove);
            users.get(users.size() - 1).setIndexPos(index);
            connections[index] = connections[users.size() - 1];
            for(int i = 0; i < users.size(); i++){
                connections[i][index] = connections[i][users.size() - 1];
            }
            users.set(index, users.get(users.size() - 1));
            users.remove(users.size() - 1);
            User.removedUser();
        }
        catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Finds the shortest path from userFrom to userTo
     * 
     * @param userFrom
     *    Name of starting user
     * @param userTo
     *    Name of desired end user
     * @return
     *    Returns the string representation of the path along with distance
     *    or "" if there is no path.
     */
    public String shortestPath(String userFrom, String userTo){
        double[][] dist = new double[users.size()][users.size()];
        User[][] next= new User[users.size()][users.size()];
        for(double[] init: dist){
            Arrays.fill(init, Double.POSITIVE_INFINITY);
        }
        for(int i = 0; i < users.size(); i++){
            for(int j = 0; j < users.size(); j++){
                if(j == i){
                    dist[j][i] = 0;
                }
                if(connections[j][i]){
                    next[j][i] = users.get(j);
                    dist[j][i] = 1;
                }
            }
        }
        for(int k = 0; k < users.size(); k++){
            for(int i = 0; i < users.size(); i++){
                for(int j = 0; j < users.size(); j++){
                    if(dist[j][k] + dist[k][i] < dist[j][i]){
                        dist[j][i] = dist[j][k] + dist[k][i];
                        next[j][i] = next[k][i];
                    } 
                }
            }
        }
        int start = findIndices(userFrom);
        int end = findIndices(userTo);
        if(next[end][start] == null){
            System.out.println(end + " " + start);
            return "No path was found";
        }
        ArrayList<User> path = new ArrayList<>();
        User curUser = users.get(start);
        path.add(curUser);
        while(curUser.getIndexPos() != end){
            curUser = next[end][curUser.getIndexPos()];
            path.add(curUser);
        }
        String ans = "The shortest path is: " + createPath(path) + "\n";
        ans +=  "The number of users in this path is: " + ((int) dist[end][start] + 1);
        return ans;
    }

    /**
     * Returns all paths between two points 
     * 
     * @param userFrom
     *    Name of starting user
     * @param userTo
     *    Name of end user
     * @return
     *    String representations of all paths
     */
    public ArrayList<String> allPaths(String userFrom, String userTo){
        try{
            User start = users.get(findIndices(userFrom));
            ArrayList<User> startingList = new ArrayList<>();
            return recursiveAllPaths(start, users.get(findIndices(userTo)), startingList);
        }
        catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Finds all loops in Twitor
     * 
     * @return
     *    A list of string representations of the loops
     */
    public ArrayList<String> findAllLoops(){
        ArrayList<String> allLoops = new ArrayList<>();
        ArrayList<ArrayList<User>> knownLoops = null;
        for(User user: users){
            // System.out.println(user.getUserName());
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
        System.out.println("Users:");
        System.out.printf("%-24s%-25s%s", "User Name", "Number of Followers", "Number of Following\n");
        for(User user: copy){
            System.out.printf("%-35s%-26d%d\n", user.getUserName(), findFollowers(user), findFollowing(user));
        }
    }

    /**
     * Prints all the followers of a user
     * 
     * @param userName
     *    Name of the user whose followers will be printed
     */
    public void printAllFollowers(String userName){
        try{
            int userIndex = findIndices(userName);
            for(int i = 0; i < users.size(); i++){
                if(connections[userIndex][i]){
                    System.out.print(users.get(i).toString() + ", ");
                }
            }
            System.out.println();
        }
        catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Prints all users followed by a certain user
     * 
     * @param userName
     *    The name of hte user whose people followed will be printed
     */
    public void printAllFollowing(String userName){
        try{
            int userIndex = findIndices(userName);
            for(int i = 0; i < users.size(); i++){
                if(connections[i][userIndex]){
                    System.out.print(users.get(i).toString() + ", ");
                }
            }
            System.out.println();
        }
        catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
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

    /**
     * Loads all users from a file
     * 
     * @param filename
     *    Name of the file to be read
     */
    public void loadAllUsers(String filename){
        try{
            File file = new File(filename);
            Scanner read = new Scanner(file);
            while(read.hasNextLine()){
                String userName = read.nextLine();
                System.out.println(userName + " has been added");
                addUser(userName);
            }
            read.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * Loads all connections from a file that are valid
     * 
     * @param filename
     *    Name of the file to be read
     */
    public void loadAllConnections(String filename){
        try{
            File file = new File(filename);
            Scanner read = new Scanner(file);
            while(read.hasNextLine()){
                String[] connectionVertices = read.nextLine().split(", ");
                addConnection(connectionVertices[0], connectionVertices[1]);
                System.out.println(connectionVertices[0] + ", " + connectionVertices[1] + " added");
            }
            read.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * Returns the index of a user given the name. Throws an error if
     * user is not found. 
     * 
     * @param user
     *    The name of the user
     * @return
     *    The index of the user
     * @throws IllegalArgumentException
     *    Thrown if the user is not found
     */
    public int findIndices(String user) throws IllegalArgumentException{
        for(int i = 0; i < users.size(); i++){
            User u = users.get(i);
            if(u.getUserName().equals(user)){
                return i;
            }
        }
        throw new IllegalArgumentException("There is no user with this name, Please choose a valid user!");
    }

    /**
     * Checks if the loops has already been found
     * 
     * @param testPath
     *    The loop which will be checked
     * @return
     *    True if the loop has already been repeated
     *    False if the loop is new
     */
    private boolean checkRepeat(ArrayList<User> testPath){
        if(knownLoops == null){
            knownLoops = new ArrayList<>();
            ArrayList<User> knownLoop = new ArrayList<>(testPath);
            knownLoops.add(knownLoop);
            return false;
        }
        for(ArrayList<User> path: knownLoops){
            if(path.size() == testPath.size()){
                int start = path.indexOf(testPath.get(0));
                boolean difference = false;
                for(int i = 0; i < path.size(); i++){
                    if(start == -1){
                        difference = true;
                        break;
                    }
                    if(!testPath.get(i).getUserName().equals(path.get((i+start)%path.size()).getUserName())){
                        difference = true;
                        break;
                    }
                }
                if(!difference){
                    return true;
                }
            }
        }
        System.out.println(createPath(testPath));
        ArrayList<User> knownLoop = new ArrayList<>(testPath);
        knownLoops.add(knownLoop);
        return false;
    }

    /**
     * Recursive method to find all loops
     * 
     * @param startingUser
     *    Starting user for the loops
     * @param curUser
     *    The current user object
     * @param curPath
     *    The path of users between startingUser and curUser taken
     * @return
     *    A list of string representations of the loops
     */
    private ArrayList<String> recursiveFindAllLoops(User startingUser, User curUser, ArrayList<User> curPath){
        ArrayList<String> loops = new ArrayList<>();
        if(curUser == startingUser && curPath.size() != 0){
            if(!checkRepeat(curPath)){
                curPath.add(curUser);
                loops.add(createPath(curPath));
            }
            else{
                curPath.add(curUser);
            }
            return loops;
        }
        curPath.add(curUser);
        if(curPath.size() == users.size()){
            return loops;
        }
        // System.out.println(createPath(curPath));
        for(int i = 0; i < users.size(); i++){
            // System.out.println(users.get(i) + " " + connections[i][curUser.getIndexPos()]);
            if(connections[i][curUser.getIndexPos()]){
                loops.addAll(recursiveFindAllLoops(startingUser, users.get(i), curPath));
                if(!curPath.isEmpty()){
                    curPath.remove(curPath.size() - 1);
                }
            }
        }
        return loops;
    }

    /**
     * Recursively finds all paths between two users
     * 
     * @param curUser
     *    Current user that is being checked
     * @param endUser
     *    The end user that designates the end of the path
     * @param curPath
     *    The current path the recursive function is on
     * @return
     *    List of string representations of paths between end and start user
     */
    private ArrayList<String> recursiveAllPaths(User curUser, User endUser, ArrayList<User> curPath){
        ArrayList<String> knownPaths = new ArrayList<>();
        if(curPath.contains(curUser)){
            curPath.add(curUser);
            return knownPaths;
        }
        curPath.add(curUser);
        if(curUser == endUser){
            String path = createPath(curPath);
            knownPaths.add(path);
            return knownPaths;
        }
        for(int i = 0; i < users.size(); i++){
            if(connections[i][curUser.getIndexPos()]){
                knownPaths.addAll(recursiveAllPaths(users.get(i), endUser, curPath));
                if(!curPath.isEmpty()){
                    curPath.remove(curPath.size() - 1);
                }
            }
        }
        return knownPaths;
    }

    /**
     * Turns path of users into a string representation of the path 
     * 
     * @param curPath
     *    Path of users that is being converted
     * @return
     *    String representation of the path
     */
    private String createPath(ArrayList<User> curPath){
        String path = "";
        for(User user: curPath){
            path += user.getUserName() + " -> ";
        }
        path = path.substring(0, path.lastIndexOf(" -> "));
        return path;
    }
}
