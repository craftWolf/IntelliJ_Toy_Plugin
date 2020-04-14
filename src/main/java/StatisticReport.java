import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.pom.Navigatable;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.PsiCommentImpl;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class StatisticReport extends AnAction {

    @Override
    public void update(AnActionEvent e) {
        // Set the availability based on whether a project is open
        Project project = e.getProject();
        e.getPresentation().setEnabledAndVisible(project != null);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project currentProject = event.getProject();
        Editor editor = event.getData(CommonDataKeys.EDITOR);

        if (editor == null) {
            String dlgTitle = event.getPresentation().getDescription();
            StringBuffer dlgMsg = new StringBuffer(event.getPresentation().getText() + " Not in editor");
            Messages.showMessageDialog(currentProject, dlgMsg.toString(), dlgTitle, Messages.getInformationIcon());
            return;
        }

        PsiFile psiFile = event.getData(LangDataKeys.PSI_FILE);

        PsiMethod[] methods = extractMethods(psiFile);
        for (final PsiMethod method : methods) {

            System.out.println("----" + method.getName() + "----");
            int cycloComplexity = CycloComplexity.getComplexityLvl(method);
            System.out.println("\t CC" + cycloComplexity);

            LineMetrics lM = new LineMetrics(method);
            System.out.println("\t Lines " + lM.getAllLines() + " " + lM.getLinesOfCode());

            CallsLookup clu = new CallsLookup(method);
            System.out.println("\t Calls " + clu.getNumberOfCalls());

            System.out.println("\t Ovverides? " + doesOverride(method));

            int commentsCount = commentsCount(method);
            int statementsCount = statementsCount(method);
            boolean hasJavaDoc = hasJavaDoc(method);

            /* TODO:
             *   Lines of code
             *   Effective Lines of code
             *   Cyclomatic complexity
             *   Number of times called
             *   Which/How many methods override it (if any)
             */
        }

        StringBuffer dlgMsg = new StringBuffer(event.getPresentation().getText() + " Selected!");
        String dlgTitle = event.getPresentation().getDescription();
        // If an element is selected in the editor, add info about it.
        Navigatable nav = event.getData(CommonDataKeys.NAVIGATABLE);
        if (nav != null) {
            dlgMsg.append(String.format("\nSelected Element: %s", nav.toString()));
        }
        Messages.showMessageDialog(currentProject, dlgMsg.toString(), dlgTitle, Messages.getInformationIcon());

    }

    /**
     * Check if the method does an Override.
     *
     * @param psiMethod method to check for Override
     * @return does it Override?
     */
    public boolean doesOverride(PsiMethod psiMethod) {
        final List<Boolean> b = new ArrayList<>();
        boolean isOverride = false;
        PsiAnnotation[] annotations = psiMethod.getAnnotations();
        if (annotations.length != 0) {
            for (@NotNull PsiAnnotation anno : annotations) {
                if (anno.getQualifiedName().equals("Override")) isOverride = true;
            }
        }

        PsiMethod[] superMethods = psiMethod.findDeepestSuperMethods();
        return isOverride || (superMethods.length != 0);
    }

    /**
     * Retrieves the Name of SuperClasses
     * in case the given method Overrides.
     *
     * @param psiMethod method for which SuperClass is to be found
     * @return String that denotes all the SuperClasses.
     */
    public String mySuper(PsiMethod psiMethod) {
        if (doesOverride(psiMethod)) {
            List<String> listOfClasses = new ArrayList<>();
            PsiMethod[] superMethods = psiMethod.findDeepestSuperMethods();
            if (superMethods.length != 0) {
                for (PsiMethod sM : superMethods) {
                    listOfClasses.add(PsiTreeUtil.getParentOfType(sM, PsiClass.class).getName());
                }
            } else return "SuperMethod Not part of the Project";

            return listOfClasses.toString();
        } else return "NoSuperMethod";
    }

    public int statementsCount(PsiMethod method) {
        PsiCodeBlock methodBody = method.getBody();
        Collection<PsiStatement> statements = PsiTreeUtil.collectElementsOfType(methodBody, PsiStatement.class);
        return statements.size();
    }

    public int commentsCount(PsiMethod method) {
        PsiCodeBlock methodBody = method.getBody();
        Collection<PsiCommentImpl> comments = PsiTreeUtil.collectElementsOfType(methodBody, PsiCommentImpl.class);
        return comments.size();
    }

    public boolean hasJavaDoc(PsiMethod method) {
        PsiDocComment comment = method.getDocComment();
        return comment != null;
    }

    // doesn't take into account inner classes!
    public PsiMethod[] extractMethods(PsiFile psiFile) {
        PsiJavaFile psiJavaFile = (PsiJavaFile) psiFile;
        final PsiClass[] classes = psiJavaFile.getClasses();

        PsiMethod[] methods = new PsiMethod[0];
        for (PsiClass javaClass : classes) {
            methods = concat(methods, javaClass.getMethods());
        }
        return methods;
    }

    public static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

}