import java.io.Serializable;

/**
 * The <code>User</code> represents a user of TWITTOR
 *
 * @author Brian Chau
 *    email brian.chau@stonybrook.edu
 *    Stony Brook ID: 116125954
 *    Recitation: 02
 **/
public class User implements Serializable {
    private String userName;
    private int indexPos;
    private static int userCount = 0;

    /**
     * Creates a user with an empty string
     * with an appropiate indexPos
     */
    public User(){
        userName = "";
        indexPos = userCount;
        userCount++;
    }

    /**
     * Creates an User object with a name
     * with an appropiate indexPos
     */
    public User(String name){
        userName = name;
        indexPos = userCount;
        userCount++;
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

    /**
     * Changes the index of the user
     *
     * @param indexPos
     *    New index of user
     */
    public void setIndexPos(int indexPos) {
        this.indexPos = indexPos;
    }

    /**
     * Changes the userCount by 1 after a user is removed
     */
    public static void removedUser(){
        userCount--;
    }
}
