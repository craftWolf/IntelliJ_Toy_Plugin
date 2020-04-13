import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.PsiCommentImpl;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.Collection;

public class LineMetrics {

    private final int allLines;
    private final int linesOfCode;

    LineMetrics(PsiMethod method){

        PsiCodeBlock pBlock = (PsiCodeBlock) method.getBody().copy();
        Collection<PsiCommentImpl> comments = PsiTreeUtil.collectElementsOfType(pBlock, PsiCommentImpl.class);
        if (!comments.isEmpty()) {
            for (PsiCommentImpl comment : comments) {
                comment.delete();
            }
        }

        allLines = countLines(method.getBody().textToCharArray())[0];
        linesOfCode = countLines(pBlock.textToCharArray())[1];
    }

    private int[] countLines(char[] lines){
        int[] lCount = new int[2];
        char prev = '\t';
        for (char c: lines) {
            if (c=='\n') {
                lCount[0]++;
                if(prev!='\n' ){ //previous line was "NotEmpty"
                    lCount[1]++;
                }
            }
            prev = c;
        }
        return lCount;
    }

    public int getAllLines() {
        return allLines;
    }

    public int getLinesOfCode() {
        return linesOfCode;
    }

}
