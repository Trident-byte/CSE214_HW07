import java.util.Comparator;
import java.util.jar.Attributes.Name;

/**
 * The <code>FollowersComparator</code> compares users based on
 * the number of followers they have.
 *
 * @author Brian Chau
 *    email brian.chau@stonybrook.edu
 *    Stony Brook ID: 116125954
 *    Recitation: 02
 **/
public class FollowersComparator implements Comparator<User>{
    private FollowerGraph graph;

    /**
     * Creates a new FollowersComparator with a graph to check connections
     *
     * @param graph
     *    The network of users following and being followers
     */
    public FollowersComparator(FollowerGraph graph){
        super();
        this.graph = graph;
    }

    /**
     * Compares the name of two users and returns a number based on
     * the number of people follow them
     *
     * @param a
     *    the first object to be compared.
     * @param b
     *    the second object to be compared.
     * @return
     *    <0 if a has more followers than b
     *    0 if a has the same followers as b
     *    >0 if a has fewer followers than b
     */
    public int compare(User a, User b){
        int AFollower = graph.findFollowers(a);
        int BFollower = graph.findFollowers(b);
        int difference = -1 * Integer.compare(AFollower, BFollower);
        if(difference == 0){
            NameComparator tieBreaker = new NameComparator();
            return tieBreaker.compare(a, b);
        }
        return difference;
    }
}
