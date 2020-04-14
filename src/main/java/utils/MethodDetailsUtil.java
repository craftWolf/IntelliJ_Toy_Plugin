package utils;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to check method details like:
 * - whether it overrides another class.
 * - if it overrides a method, find the respective superclass.
 * - whether the method contains a JavaDoc.
 */
public final class MethodDetailsUtil {

    private MethodDetailsUtil() {
    }

    /**
     * Check if the method does an Override.
     *
     * @param psiMethod method to check for Override.
     * @return whether the method overrides another one.
     */
    public static boolean doesOverride(final PsiMethod psiMethod) {
        boolean isOverride = false;
        final PsiAnnotation[] annotations = psiMethod.getAnnotations();
        if (annotations.length != 0) {
            for (@NotNull final PsiAnnotation annotation : annotations) {
                if (annotation.getQualifiedName().equals("Override")) {
                    isOverride = true;
                }
            }
        }

        final PsiMethod[] superMethods = psiMethod.findDeepestSuperMethods();
        return isOverride || (superMethods.length != 0);
    }

    /**
     * Retrieves the Name of SuperClasses
     * in case the given method Overrides.
     *
     * @param psiMethod method for which SuperClass is to be found
     * @return String that denotes all the SuperClasses.
     */
    public static String mySuper(final PsiMethod psiMethod) {
        if (doesOverride(psiMethod)) {
            List<String> listOfClasses = new ArrayList<>();
            PsiMethod[] superMethods = psiMethod.findDeepestSuperMethods();
            if (superMethods.length != 0) {
                for (PsiMethod sM : superMethods) {
                    listOfClasses.add(PsiTreeUtil.getParentOfType(sM, PsiClass.class).getName());
                }
            } else {
                return "SuperMethod Not part of the Project";
            }
            return listOfClasses.toString();
        } else {
            return "NoSuperMethod";
        }
    }

    /**
     * Check if the method contains a JavaDoc comment.
     * @param method to check for JavaDoc comment.
     * @return whether method contains JavaDoc comment.
     */
    public static boolean hasJavaDoc(final PsiMethod method) {
        final PsiDocComment comment = method.getDocComment();
        return comment != null;
    }

}
