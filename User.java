/**
 * The <code>User</code>
 *
 * @author Brian Chau
 *    email brian.chau@stonybrook.edu
 *    Stony Brook ID: 116125954
 *    Recitation: 02
 **/
public class User {
    private String userName;
    private int indexPos;
    private static int userCount;

    /**
     * Creates a user with an empty string
     * with an appropiate indexPos
     */
    public User(){
        userName = "";
        indexPos = userCount++;
    }

    /**
     * Creates an User object with a name
     * with an appropiate indexPos
     */
    public User(String name){
        userName = name;
        indexPos = userCount++;
    }

    /**
     * Returns the user count 
     * 
     * @return
     *    The count of the user
     */
    public int getUserCount(){
        return userCount;
    }

    /**
     * Returns name of the user
     * 
     * @return
     *    The name of the user
     */
    public String getUserName(){
        return userName;
    }

    /**
     * Returns the index position of the user
     * 
     * @return
     *    Index position of the user
     */
    public int getIndexPos(){
        return indexPos;
    }

    /**
     * Returns a string representation of the user
     * 
     * @return
     *    The string representation of the user
     */
    public String toString(){
        return userName;
    }

    /**
     * Changes the userName
     * 
     * @param newName
     *    The new name of the user
     */
    public void setUserName(String newName){
        userName = newName;
    }
}
