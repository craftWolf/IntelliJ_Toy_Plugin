package statistics;

import com.intellij.psi.PsiMethod;
import com.sun.istack.NotNull;
import utils.CallsLookupUtil;
import utils.CycloComplexityUtil;
import utils.LineMetricsUtil;
import utils.MethodDetailsUtil;

import java.util.Hashtable;

public class MethodStatistic {

    private final PsiMethod psiMethod;

    private int lines;
    private int linesOfCode;
    private int cycloComplexity;
    private int commentsCount;
    private int statementsCount;
    private int numOfCalls;

    private boolean hasJavaDoc;
    private boolean doesOverride;

    private String name;
    private String superClass;
    private Hashtable<String, Integer> callsPerClass;


    public MethodStatistic(@NotNull PsiMethod psiMethod) {
        this.psiMethod = psiMethod;
        this.name = psiMethod.getName();
        calculateAllStats();
    }

    private void calculateAllStats() {
        calculateLineStats();
        calculateCComplexity();
        calculateCalls();
        findMethodDetails();
    }

    private void calculateLineStats() {
        this.lines = LineMetricsUtil.countAllLines(psiMethod);
        this.linesOfCode = LineMetricsUtil.countLinesOfCode(psiMethod);
        this.statementsCount = LineMetricsUtil.statementsCount(psiMethod);
        this.commentsCount = LineMetricsUtil.commentsCount(psiMethod);
    }

    private void calculateCComplexity() {
        this.cycloComplexity = CycloComplexityUtil.findComplexityLvl(psiMethod);
    }

    private void calculateCalls() {
        this.callsPerClass = CallsLookupUtil.getPairs(psiMethod);
        this.numOfCalls = CallsLookupUtil.countNumberOfCalls(callsPerClass);
    }

    private void findMethodDetails() {
        this.hasJavaDoc = MethodDetailsUtil.hasJavaDoc(psiMethod);
        this.doesOverride = MethodDetailsUtil.doesOverride(psiMethod);
        this.superClass = MethodDetailsUtil.mySuper(psiMethod);
    }

}
