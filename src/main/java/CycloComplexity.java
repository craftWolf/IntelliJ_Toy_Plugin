import com.intellij.psi.*;
import com.intellij.psi.util.PsiElementFilter;

class CycloComplexity {


    public static int getComplexityLvl(PsiElement element) {
        if (element == null) {
            return 1;
        }
        ComplexityVisitor cVisitor = new ComplexityVisitor();
        element.accept(cVisitor);
        return cVisitor.getComplexity();
    }

    private static class ComplexityVisitor extends JavaRecursiveElementWalkingVisitor {

        private int complexity = 1;

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
