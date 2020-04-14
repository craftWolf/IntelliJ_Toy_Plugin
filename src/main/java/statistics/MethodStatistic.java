package statistics;

import com.intellij.psi.PsiMethod;
import com.sun.istack.NotNull;
import utils.CallsLookupUtil;
import utils.CycloComplexityUtil;
import utils.LineMetricsUtil;
import utils.MethodDetailsUtil;

import java.util.Hashtable;

/**
 * Class containing all the statistics for a method, specifically:
 * - Number of lines
 * - Lines of code
 * - Cyclomatic complexity
 * - Number of comments
 * - Number of statements
 * - Number of calls in the whole project
 * - Does the method contain javaDoc
 * - Does the method override another one
 * - Method name
 * - If overrides, what is the name of superClass
 * - Number of calls in each of the project classes.
 */
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
     * Constructor that assigns the name of the method,
     * the PsiMethod and calls the calculation of all the statistics.
     *
     * @param psiMethod to collect statistics for.
     */
    public MethodStatistic(@NotNull PsiMethod psiMethod) {
        this.psiMethod = psiMethod;
        this.name = psiMethod.getName();
        calculateAllStats();
    }

    /**
     * Calls the calculators of all the methods statistics.
     */
    private void calculateAllStats() {
        calculateLineStats();
        calculateCComplexity();
        calculateCalls();
        findMethodDetails();
    }

    /**
     * Assigns number of lines, lines of code,
     * number of comments and number of statements.
     *
     */
    private void calculateLineStats() {
        this.lines = LineMetricsUtil.countAllLines(psiMethod);
        this.linesOfCode = LineMetricsUtil.countLinesOfCode(psiMethod);
        this.statementsCount = LineMetricsUtil.statementsCount(psiMethod);
        this.commentsCount = LineMetricsUtil.commentsCount(psiMethod);
    }

    /**
     * Assigns the cyclomatic complexity for the method.
     */
    private void calculateCComplexity() {
        this.cycloComplexity = CycloComplexityUtil.findComplexityLvl(psiMethod);
    }

    /**
     * Assigns how many times the method was called in each of the
     * project classes and also sums it up.
     */
    private void calculateCalls() {
        this.callsPerClass = CallsLookupUtil.getPairs(psiMethod);
        this.numOfCalls = CallsLookupUtil.countNumberOfCalls(callsPerClass);
    }

    /**
     * Assigns the values representing if the method
     * contains JavaDoc Comment, if it overrides any other method
     * and what is the class of the method it overrides.
     */
    private void findMethodDetails() {
        this.hasJavaDoc = MethodDetailsUtil.hasJavaDoc(psiMethod);
        this.doesOverride = MethodDetailsUtil.doesOverride(psiMethod);
        this.superClass = MethodDetailsUtil.mySuper(psiMethod);
    }

    /**
     * @return how many lines does the method have.
     */
    public int getLines() {
        return lines;
    }

    /**
     * @return how many lines of code does the method have.
     */
    public int getLinesOfCode() {
        return linesOfCode;
    }

    /**
     * @return the cyclomatic complexity of the method.
     */
    public int getCycloComplexity() {
        return cycloComplexity;
    }

    /**
     * @return how many comments does the method have.
     */
    public int getCommentsCount() {
        return commentsCount;
    }

    /**
     * @return how many statements does the method have.
     */
    public int getStatementsCount() {
        return statementsCount;
    }

    /**
     * @return how many times is the method called in the whole project
     */
    public int getNumOfCalls() {
        return numOfCalls;
    }

    /**
     * @return whether the method contains JavaDoc comment
     */
    public boolean hasJavaDoc() {
        return hasJavaDoc;
    }

    /**
     * @return whether the method overrides another method
     */
    public boolean doesOverride() {
        return doesOverride;
    }

    /**
     * @return the name of the method
     */
    public String getName() {
        return name;
    }

    /**
     * @return the class name of the overridden method.
     */
    public String getSuperClass() {
        return superClass;
    }

    /**
     * @return how many times in each of the project classes the method is called.
     */
    public Hashtable<String, Integer> getCallsPerClass() {
        return callsPerClass;
    }
}
