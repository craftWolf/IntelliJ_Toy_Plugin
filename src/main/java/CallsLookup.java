import com.intellij.psi.*;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Query;
import java.util.Hashtable;

public class CallsLookup {

    private Hashtable<String, Integer> pairs;
    private int numberOfCalls;

    public CallsLookup(PsiMethod method) {
        pairs = inClassCalls(method);
        numberOfCalls = 0;
        for (Integer v : pairs.values()) {
            numberOfCalls += v;
        }
    }

    public Hashtable<String, Integer> getPairs() {
        return pairs;
    }

    public int getNumberOfCalls() {
        return numberOfCalls;
    }

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
