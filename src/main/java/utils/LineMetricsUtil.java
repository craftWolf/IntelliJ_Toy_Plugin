package utils;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.PsiCommentImpl;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.Collection;

/**
 * Class to Calculate:
 * - the totalLine number in a Method
 * - Code-only lines.
 * - number of Comments in the Method
 * - number of Statements in the Method
 */
public final class LineMetricsUtil {

    /**
     * Default Constructor
     */
    private LineMetricsUtil() {
    }

    /**
     * Calculate Number of All the lines in a method.
     * All lines in a method include:
     * - lines of code
     * - lines of comments
     * - empty lines
     *
     * @param psiMethod The PsiMethod to calculate TotalLines metrics for.
     * @return number of lines a method has.
     */
    public static int countAllLines(final PsiMethod psiMethod) {
        PsiCodeBlock methodBody = psiMethod.getBody();
        if (methodBody == null) {
            return 0;
        }
        return countLines(methodBody.textToCharArray())[0];
    }

    /**
     * Calculate the Number of CODE lines present in a method.
     * Initializes the fields by:
     * - preprocessing the input
     * - calling the respective CountLines.method
     *
     * @param psiMethod The PsiMethod to calculate Code-Lines metrics for.
     * @return number of Code-Lines
     */
    public static int countLinesOfCode(final PsiMethod psiMethod) {
        PsiCodeBlock methodBody = psiMethod.getBody();
        if (methodBody == null) {
            return 0;
        }
        PsiCodeBlock pBlock = (PsiCodeBlock) methodBody.copy(); //create method copy
        Collection<PsiCommentImpl> comments = PsiTreeUtil.collectElementsOfType(pBlock, PsiCommentImpl.class);
        if (!comments.isEmpty()) {
            for (PsiCommentImpl comment : comments) {
                comment.delete();
            }
        }
        return countLines(pBlock.textToCharArray())[1] - 1;
    }

    /**
     * Calculate the amount of Statement
     * a certain Method contains.
     *
     * @param method method to work with
     * @return number of statements in the method
     */
    public static int statementsCount(PsiMethod method) {
        PsiCodeBlock methodBody = method.getBody();
        Collection<PsiStatement> statements = PsiTreeUtil.collectElementsOfType(methodBody, PsiStatement.class);
        return statements.size();
    }
    /**
     * Calculate the amount of Comments
     * a certain Method contains.
     *
     * <b>Note</b>: not the amount of comment-lines
     *
     * @param method method to work with
     * @return number of Comments in the method
     */
    public static int commentsCount(final PsiMethod method) {
        final PsiCodeBlock methodBody = method.getBody();
        Collection<PsiCommentImpl> comments = PsiTreeUtil.collectElementsOfType(methodBody, PsiCommentImpl.class);
        return comments.size();
    }

    /**
     * Calculates the total (1) number of lines from a given char[]
     * representation of a PsiElement, (2) non-empty lines.
     *
     * @param lines Array of char representation of a PsiElement.
     * @return An array to contain:
     * - [0] all the '\n' chars -> all Lines.
     * - [1] the non-empty lines count
     */
    private static int[] countLines(char[] lines) {
        int[] lCount = new int[2];
        char prev = '\t';
        for (char c : lines) {
            if (c == '\n') {
                lCount[0]++;
                if (prev != '\n') { //previous line was "NotEmpty"
                    lCount[1]++;
                }
            }
            prev = c;
        }
        return lCount;
    }

}
