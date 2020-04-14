import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.PsiCommentImpl;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Query;
import com.siyeh.ig.psiutils.TestUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

public class CallsLookupUtil {

    private static Hashtable<String,Integer> pairs;
    private static int numberOfCalls;

    public CallsLookupUtil(PsiMethod method) {
        pairs = inClassCalls(method);
        numberOfCalls = pairs.size();
        //System.out.println("Hashtable for method: "+method.getName()+"\n"+pairs);
    }

    public Hashtable<String,Integer> getPairs() {
        return pairs;
    }

    public int getNumberOfCalls() {
        return numberOfCalls;
    }

    private static Hashtable<String,Integer> inClassCalls(PsiMethod psiMethod) {
        Hashtable<String,Integer> allClasses =new Hashtable<String,Integer>();
        Query<PsiReference> query = ReferencesSearch.search(psiMethod);
        for (PsiReference reference : query) {
            PsiElement element = reference.getElement();
            PsiClass elClass = PsiTreeUtil.getParentOfType(element,PsiClass.class);

            if(elClass!=null) {
                if (allClasses.containsKey(elClass.getName())){
                    int k = allClasses.get(elClass.getName());
                    allClasses.put(elClass.getName(),k+1);
                }else {
                    allClasses.put(elClass.getName(),1);
                }
            }
        }
        return allClasses;
    }

}
