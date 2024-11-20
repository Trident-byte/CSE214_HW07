import java.io.Serializable;
import java.util.ArrayList;
/**
 * The <code>FollowerGraph</code>
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

    /**
     * Creates an empty FollowersGraph and intializes
     * all declared variables.
     */
    public FollowerGraph(){
        users = new ArrayList<>();
        connections = new boolean[100][100];
    }
}
