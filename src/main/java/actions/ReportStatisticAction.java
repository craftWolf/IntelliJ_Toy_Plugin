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
        PsiFile psiFile = event.getData(LangDataKeys.PSI_FILE);

        if (psiFile == null) {
            String dlgTitle = event.getPresentation().getDescription();
            StringBuffer dlgMsg = new StringBuffer(event.getPresentation().getText() + " No File Selected");
            Messages.showMessageDialog(currentProject, dlgMsg.toString(), dlgTitle, Messages.getInformationIcon());
            return;
        }

        List<MethodStatistic> methodStatistics = calculateStats(psiFile);
        try {
            saveReport(psiFile, methodStatistics);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Messages.showMessageDialog(currentProject, "Method statistics saved inside: " + htmlTemplate.OUTPUT_DIR,
                "Statistics Saved", Messages.getInformationIcon());
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

    public void saveReport(final PsiFile psiFile, List<MethodStatistic> methodStatistics) throws IOException {
        String path = psiFile.getManager().getProject().getBasePath();
        htmlTemplate.writeToHTML(path, methodStatistics);
    }

}