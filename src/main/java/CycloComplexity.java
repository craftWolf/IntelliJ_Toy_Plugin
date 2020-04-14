import com.intellij.psi.*;

/**
 * Class responsible for calculating
 * Cyclomatic Complexity of the provided Method.
 */
class CycloComplexity {

    /**
     * Retrieves the CC of the given PsiMethod.
     *
     * @param element PsiMethod for which CC is to be calculated
     * @return the CC value
     */
    public static int getComplexityLvl(PsiMethod element) {
        if (element == null) {
            return 1;
        }
        ComplexityVisitor cVisitor = new ComplexityVisitor();
        element.accept(cVisitor);
        return cVisitor.getComplexity();
    }

    /**
     * Class for Recursive Visit and CC calculation
     * of the PsiMethod components
     * <p>
     * Extend {@see JavaRecursiveElementVisitor}
     * <p>
     * Supports:
     * - If Statements
     * - For Statements
     * - ForEach Statements
     * - While Statements
     * - Conditional Expressions {@code (a > b) ? a : b;}
     */
    private static class ComplexityVisitor extends JavaRecursiveElementWalkingVisitor {

        /**
         * Default CC for any PsiMethod
         */
        private int complexity = 1;

        /**
         * Getter of the CC
         * @return complexity value
         */
        private int getComplexity() {
            return complexity;
        }

        @Override
        public void visitIfStatement(PsiIfStatement statement) {
            super.visitIfStatement(statement);
            complexity++;
        }

        @Override
        public void visitForStatement(PsiForStatement statement) {
            super.visitForStatement(statement);
            complexity++;
        }

        @Override
        public void visitForeachStatement(PsiForeachStatement statement) {
            super.visitForeachStatement(statement);
            complexity++;
        }

        @Override
        public void visitWhileStatement(PsiWhileStatement statement) {
            super.visitWhileStatement(statement);
            complexity++;
        }

        @Override
        public void visitConditionalExpression(PsiConditionalExpression expression) {
            super.visitConditionalExpression(expression);
            complexity++;
        }

        @Override
        public void visitSwitchStatement(PsiSwitchStatement statement) {
            super.visitSwitchStatement(statement);
            final PsiCodeBlock body = statement.getBody();
            if (body == null) {
                return;
            }
            final PsiStatement[] statements = body.getStatements();
            //TODO
        }

    }

}
