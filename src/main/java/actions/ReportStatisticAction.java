package actions;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import output.HtmlTemplate;
import statistics.MethodStatistic;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Class that executes the defined action
 * (Collect method statistics) and
 * saves the report to a file.
 *
 */
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
        final PsiFile psiFile = event.getData(LangDataKeys.PSI_FILE);

        // Notify user that a file has to be active
        if (psiFile == null) {
            Messages.showMessageDialog(currentProject, "Please make sure a file is selected",
                    "No File Selected", Messages.getWarningIcon());
            return;
        }
        List<MethodStatistic> methodStatistics = calculateStats(psiFile);

        // Save the report
        try {
            saveReport(psiFile, methodStatistics);
        } catch (IOException e) {
            Messages.showMessageDialog(currentProject, "Failed to Save File",
                    "Failed to Save", Messages.getErrorIcon());
            e.printStackTrace();
        }
        Messages.showMessageDialog(currentProject, "Method statistics saved in: " + HtmlTemplate.OUTPUT_DIR + " folder",
                "Statistics Saved", Messages.getInformationIcon());
    }

    /**
     * Method that calculates all the statistics for all methods.
     *
     * @param psiFile file containing the methods.
     * @return a list where statistics for each
     * method is contained.
     */
    private List<MethodStatistic> calculateStats(final PsiFile psiFile) {
        List<MethodStatistic> methodStats = new ArrayList<>();
        final PsiJavaFile psiJavaFile = (PsiJavaFile) psiFile;
        final PsiClass[] classes = psiJavaFile.getClasses();

        for (final PsiClass javaClass : classes) {
            addMethods(javaClass.getMethods(), methodStats);
            addFromInnerClass(javaClass, methodStats);
        }
        return methodStats;
    }

    /**
     * Calculates the statistics for all the methods provided
     * and appends them to the provided list.
     *
     * @param methods file containing the methods.
     * @param methodStats the list with already calculated stats.
     */
    private void addMethods(PsiMethod[] methods, List<MethodStatistic> methodStats) {
        for (final PsiMethod method : methods) {
            MethodStatistic methodStatistic = new MethodStatistic(method);
            methodStats.add(methodStatistic);
        }
    }

    /**
     * Loops through all the inner classes of a method in order
     * to calculate statistics for them.
     *
     * @param javaClass class with possibly inner classes
     * @param methodStats  the list with already calculated stats.
     */
    private void addFromInnerClass(PsiClass javaClass, List<MethodStatistic> methodStats) {
        PsiClass[] classes = javaClass.getInnerClasses();
        for (final PsiClass innerClass : classes) {
            addMethods(innerClass.getMethods(), methodStats);
        }
    }

    /**
     * Saves the report to an html file.
     *
     * @param psiFile          File on which the plugin is running
     * @param methodStatistics All the gathered statistics that goes to report
     */
    private void saveReport(final PsiFile psiFile, List<MethodStatistic> methodStatistics) throws IOException {
        String path = psiFile.getManager().getProject().getBasePath();
        HtmlTemplate.writeToFile(path, methodStatistics);
    }

}