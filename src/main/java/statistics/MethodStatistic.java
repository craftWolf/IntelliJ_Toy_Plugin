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

    /**
     *  A method that calls the calculation of all the statistics.
     *  @param psiMethod A psi method that contains name of the method
     */

    public MethodStatistic(@NotNull PsiMethod psiMethod) {
        this.psiMethod = psiMethod;
        this.name = psiMethod.getName();
        calculateAllStats();
    }

    /**
     * The method that calls the calculators of all the methods below.
     */

    private void calculateAllStats() {
        calculateLineStats();
        calculateCComplexity();
        calculateCalls();
        findMethodDetails();
    }


    /**
     * The method that calls the calculators of all the statistics about
     * number of lines, statements and comments.
     */

    private void calculateLineStats() {
        this.lines = LineMetricsUtil.countAllLines(psiMethod);
        this.linesOfCode = LineMetricsUtil.countLinesOfCode(psiMethod);
        this.statementsCount = LineMetricsUtil.statementsCount(psiMethod);
        this.commentsCount = LineMetricsUtil.commentsCount(psiMethod);
    }

    /**
     * The method that calls the calculators of cyclomatic complexity.
     */

    private void calculateCComplexity() {
        this.cycloComplexity = CycloComplexityUtil.findComplexityLvl(psiMethod);
    }

    /**
     * The method that calls the calculators of all the statistics about
     * calls to the method from other classes.
     */

    private void calculateCalls() {
        this.callsPerClass = CallsLookupUtil.getPairs(psiMethod);
        this.numOfCalls = CallsLookupUtil.countNumberOfCalls(callsPerClass);
    }

    /**
     * The method that calls the calculators of all the non-integer
     * statistics of the method.
     */

    private void findMethodDetails() {
        this.hasJavaDoc = MethodDetailsUtil.hasJavaDoc(psiMethod);
        this.doesOverride = MethodDetailsUtil.doesOverride(psiMethod);
        this.superClass = MethodDetailsUtil.mySuper(psiMethod);
    }

    public int getLines() {
        return lines;
    }

    public int getLinesOfCode() {
        return linesOfCode;
    }

    public int getCycloComplexity() {
        return cycloComplexity;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public int getStatementsCount() {
        return statementsCount;
    }

    public int getNumOfCalls() {
        return numOfCalls;
    }

    public boolean hasJavaDoc() {
        return hasJavaDoc;
    }

    public boolean doesOverride() {
        return doesOverride;
    }

    public String getName() {
        return name;
    }

    public String getSuperClass() {
        return superClass;
    }

    public Hashtable<String, Integer> getCallsPerClass() {
        return callsPerClass;
    }
}
