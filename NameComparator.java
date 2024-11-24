import java.util.Comparator;

/**
 * The <code>NameComparator</code> compares users based on name
 *
 * @author Brian Chau
 *    email brian.chau@stonybrook.edu
 *    Stony Brook ID: 116125954
 *    Recitation: 02
 **/
public class NameComparator implements Comparator<User>{
    /**
     * Compares the name of two users and returns a number based on
     * the order of their names alphabetically
     *
     * @param a
     *    the first object to be compared.
     * @param b
     *    the second object to be compared.
     * @return
     *    <0 if the name of a is before b alphabetically
     *    0 if the name of a is the same as b
     *    >0 if the name of a is after b alphabetically
     */
    public int compare(User a, User b){
        if(a.getUserName().equals(b.getUserName())){
            return 0;
        }
        else if(a.getUserName().compareTo(b.getUserName()) < 0){
            return -1;
        }
        else{
            return 1;
        }
    }
}