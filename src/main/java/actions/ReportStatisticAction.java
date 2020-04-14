package actions;

import output.htmlTemplate;
import statistics.MethodStatistic;
import utils.CallsLookupUtil;
import utils.CycloComplexityUtil;
import utils.LineMetricsUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.pom.Navigatable;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import utils.MethodDetailsUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ReportStatisticAction extends AnAction {

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
        List<MethodStatistic> methodStatistics = calculateStats(psiFile);

        StringBuffer dlgMsg = new StringBuffer(event.getPresentation().getText() + " Selected!");
        String dlgTitle = event.getPresentation().getDescription();
        // If an element is selected in the editor, add info about it.
        Navigatable nav = event.getData(CommonDataKeys.NAVIGATABLE);
        if (nav != null) {
            dlgMsg.append(String.format("\nSelected Element: %s", nav.toString()));
        }
        Messages.showMessageDialog(currentProject, dlgMsg.toString(), dlgTitle, Messages.getInformationIcon());

    }

    public List<MethodStatistic> calculateStats(final PsiFile psiFile) {
        List<MethodStatistic> methodStats = new ArrayList<>();
        final PsiJavaFile psiJavaFile = (PsiJavaFile) psiFile;
        final PsiClass[] classes = psiJavaFile.getClasses();

        for (final PsiClass javaClass : classes) {
            addMethods(javaClass.getMethods(), methodStats);
            addFromInnerClass(javaClass, methodStats);
        }
        return methodStats;
    }

    public void addMethods(PsiMethod[] methods, List<MethodStatistic> methodStats) {
        for (final PsiMethod method : methods) {
            MethodStatistic methodStatistic = new MethodStatistic(method);
            methodStats.add(methodStatistic);
        }
    }

    public void addFromInnerClass(PsiClass javaClass, List<MethodStatistic> methodStats) {
        PsiClass[] classes = javaClass.getInnerClasses();
        for (final PsiClass innerClass : classes) {
            addMethods(innerClass.getMethods(), methodStats);
        }
    }

    public void saveReport(final PsiFile psiFile) {
        String path = psiFile.getManager().getProject().getBasePath();
        

    }


//    // doesn't take into account inner classes!
//    public PsiMethod[] extractMethods(PsiFile psiFile) {
//        PsiJavaFile psiJavaFile = (PsiJavaFile) psiFile;
//        final PsiClass[] classes = psiJavaFile.getClasses();
//
//        PsiMethod[] methods = new PsiMethod[0];
//        for (PsiClass javaClass : classes) {
//            methods = concat(methods, javaClass.getMethods());
//        }
//        return methods;
//    }
//
//    public static <T> T[] concat(T[] first, T[] second) {
//        T[] result = Arrays.copyOf(first, first.length + second.length);
//        System.arraycopy(second, 0, result, first.length, second.length);
//        return result;
//    }
}