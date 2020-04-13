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

import java.util.Arrays;
import java.util.Collection;


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

        if (editor==null){
            String dlgTitle = event.getPresentation().getDescription();
            StringBuffer dlgMsg = new StringBuffer(event.getPresentation().getText() + " Not in editor");
            Messages.showMessageDialog(currentProject, dlgMsg.toString(), dlgTitle, Messages.getInformationIcon());
            return;
        }

        PsiFile psiFile = event.getData(LangDataKeys.PSI_FILE);

        PsiMethod[] methods = extractMethods(psiFile);
        for (PsiMethod method : methods) {
            System.out.println(method.getName());
            int cycloComplexity = CycloComplexity.getComplexityLvl(method);
            System.out.println("\t CC"+ cycloComplexity);
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