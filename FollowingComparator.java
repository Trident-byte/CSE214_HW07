import java.util.Comparator;

/**
 * The <code>FollowingComparator</code> compares users based on number
 * of people followed
 *
 * @author Brian Chau
 *    email brian.chau@stonybrook.edu
 *    Stony Brook ID: 116125954
 *    Recitation: 02
 **/
public class FollowingComparator implements Comparator<User>{
    private FollowerGraph graph;

    /**
     * Creates a new FollowingComparator with a graph to check connections
     *
     * @param graph
     *    The network of users following and being followers
     */
    public FollowingComparator(FollowerGraph graph){
        super();
        this.graph = graph;
    }

    /**
     * Compares the name of two users and returns a number based on
     * the number of people they follow
     *
     * @param a
     *    the first object to be compared.
     * @param b
     *    the second object to be compared.
     * @return
     *    <0 if a follows more people than b
     *    0 if a follows the same number of people as b
     *    >0 if a follows fewer people than b
     */
    public int compare(User a, User b){
        int AFollowing = graph.findFollowing(a);
        int BFollowing = graph.findFollowing(b);
        return Integer.compare(AFollowing, BFollowing);
    }
}
