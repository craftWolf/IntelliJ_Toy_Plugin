import com.intellij.psi.*;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Query;

import java.util.Hashtable;

/**
 * Finds and Calculates the TotalNumber of Calls of a specific Method.
 */
public class CallsLookup {

    /**
     * HashTable to contain Pairs of (Class,NumberOfCalls).
     */
    private Hashtable<String, Integer> pairs;

    /**
     * Total numbers of calls.
     */
    private int numberOfCalls;

    /**
     * Class Constructor.
     * Calculates and Initializes the Pairs table and numbersOfCalls fields.
     *
     * @param method method to work with.
     */
    public CallsLookup(PsiMethod method) {
        pairs = inClassCalls(method);
        numberOfCalls = 0;
        for (Integer v : pairs.values()) {
            numberOfCalls += v;
        }
    }

    /**
     * Getter for the Table of Pairs.
     *
     * @return table of (Class,NumberOfCalls) pairs
     */
    public Hashtable<String, Integer> getPairs() {
        return pairs;
    }

    /**
     * Getter of the Method total Number of calls in the Project.
     *
     * @return method Number of Calls
     */
    public int getNumberOfCalls() {
        return numberOfCalls;
    }

    /**
     * Method to Query the project in search for method calls.
     *
     * @param psiMethod method for which calls to look for
     * @return table fo pairs
     */
    private Hashtable<String, Integer> inClassCalls(PsiMethod psiMethod) {
        Hashtable<String, Integer> allClasses = new Hashtable<String, Integer>();
        Query<PsiReference> query = ReferencesSearch.search(psiMethod);
        for (PsiReference reference : query) {
            PsiElement element = reference.getElement();
            PsiClass elClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);

            if (elClass != null) {
                if (allClasses.containsKey(elClass.getName())) {
                    int k = allClasses.get(elClass.getName());
                    allClasses.put(elClass.getName(), k + 1);
                } else {
                    allClasses.put(elClass.getName(), 1);
                }
            }
        }
        return allClasses;
    }

}
