package MethodUtils;

import com.intellij.psi.*;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Query;

import java.util.Hashtable;

/**
 * Finds and Calculates the TotalNumber of Calls of a specific Method.
 */
public final class CallsLookupUtil {

    /**
     * Default Constructor
     */
    CallsLookupUtil() {
    }

    /**
     * Calculates based on Pairs table and numbersOfCalls fields.
     *
     * @param pairs HashTable of (Class,NumberOfCalls) to work with.
     * @return Total number of calls given the Table
     */
    public static int getNumberOfCalls(Hashtable<String, Integer> pairs) {
        int numberOfCalls = 0;
        for (Integer v : pairs.values()) {
            numberOfCalls += v;
        }
        return numberOfCalls;
    }

    /**
     * Calculate the Table of Pairs (Class,NumberOfCalls)
     *
     * @param method method to work with.
     * @return table of (Class,NumberOfCalls) pairs
     */
    public static Hashtable<String, Integer> getPairs(PsiMethod method) {
        return inClassCalls(method);
    }

    /**
     * Method to Query the project in search for method calls.
     *
     * @param psiMethod method for which calls to look for
     * @return table fo pairs
     */
    private static Hashtable<String, Integer> inClassCalls(PsiMethod psiMethod) {
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
