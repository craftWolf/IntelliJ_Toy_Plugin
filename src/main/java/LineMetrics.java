import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.PsiCommentImpl;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.Collection;

public class LineMetrics {
    /**
     * Field to contain the Number of All the lines in a method.
     * All lines in a method include:
     * - lines of code
     * - lines of comments
     * - empty lines
     */
    private final int allLines;

    /**
     * Field for the Number of CODE lines present in a method.
     */
    private final int linesOfCode;

    /**
     * LineMetrics.class constructor.
     * Initializes the fields by:
     * - preprocessing the input
     * - calling the respective CountLines.method
     *
     * @param psiMethod The PsiMethod to calculate Line metrics for.
     */
    LineMetrics(PsiMethod psiMethod) {

        PsiCodeBlock pBlock = (PsiCodeBlock) psiMethod.getBody().copy();
        Collection<PsiCommentImpl> comments = PsiTreeUtil.collectElementsOfType(pBlock, PsiCommentImpl.class);
        if (!comments.isEmpty()) {
            for (PsiCommentImpl comment : comments) {
                comment.delete();
            }
        }

        allLines = countLines(psiMethod.getBody().textToCharArray())[0];
        linesOfCode = countLines(pBlock.textToCharArray())[1] - 1;
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
    private int[] countLines(char[] lines) {
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

    /**
     * Getter for all Lines.
     *
     * @return number of lines a method has.
     */
    public int getAllLines() {
        return allLines;
    }

    /**
     * Getter for all Code containing Lines in a method
     *
     * @return number of Code Lines
     */
    public int getLinesOfCode() {
        return linesOfCode;
    }

}
