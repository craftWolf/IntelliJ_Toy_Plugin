package output;

import statistics.MethodStatistic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Class responsible for creating an HTML template and
 * saving the statistics to an HTML file.
 *
 */
public class HtmlTemplate {

    private HtmlTemplate() {
    }

    public static final String OUTPUT_DIR = "/statisticReport/";
    public static final String OUTPUT_FILE_NAME = "methodStatistics.html";

    private static final String HTML_TEMPLATE_OPENING = "<html> <head> "
            + "<meta http-equiv=\"Content-Type\" content=\"text/html; "
            + "charset=UTF-8\"> <title> Statistic Report"
            + "</title> </head> <body> <table align = \"center\" width=\"100%\" "
            + "style=\"text-align:center\" > <tr> <th>Method</th> <th>CC</th> "
            + "<th>Number of comments</th> <th>Number of statements</th> <th>Number of calls</th>"
            + "<th>Number of lines</th> <th>Lines of code</th> "
            + "<th>Has JavaDoc comment</th> <th>Does override something?</th><th>Super class</th></tr>";
    private static final String HTML_TEMPLATE_CLOSING = "</table></body></html>";


    /**
     * The method that generates a text for HTML file based on provided statistics.
     *
     * @param methodStatistics All the gathered statistics that goes to report.
     * @return the generated HTML file containing the stats.
     */
    private static String generateHTML(List<MethodStatistic> methodStatistics) {
        StringBuilder text = new StringBuilder(HTML_TEMPLATE_OPENING);

        for (MethodStatistic methStatistics : methodStatistics) {
            text.append("<tr> <td>").append(methStatistics.getName()).append("</td>");
            text.append("<td>").append(methStatistics.getCycloComplexity()).append("</td>");
            text.append("<td>").append(methStatistics.getCommentsCount()).append("</td>");
            text.append("<td>").append(methStatistics.getStatementsCount()).append("</td>");
            text.append("<td>").append(methStatistics.getNumOfCalls()).append("</td>");
            text.append("<td>").append(methStatistics.getLines()).append("</td>");
            text.append("<td>").append(methStatistics.getLinesOfCode()).append("</td>");
            text.append("<td>").append(methStatistics.hasJavaDoc()).append("</td>");
            text.append("<td>").append(methStatistics.doesOverride()).append("</td>");
            text.append("<td>").append(methStatistics.getSuperClass()).append("</td>");
            text.append("</tr>");
        }
        text.append(HTML_TEMPLATE_CLOSING);
        return text.toString();
    }

    /**
     * The method that writes all the information to HTML file and saves it to the created directory.
     *
     * @param path             Path to the root folder of the current project
     * @param methodStatistics All the gathered statistics that goes to report
     */
    public static void writeToFile(String path,
                                   List<MethodStatistic> methodStatistics) throws IOException {
        File file = new File(path + OUTPUT_DIR);
        file.mkdir();
        String text = generateHTML(methodStatistics);
        File newHtmlFile = new File(path + OUTPUT_DIR + OUTPUT_FILE_NAME);

        FileWriter f2 = new FileWriter(newHtmlFile, false);
        f2.write(text);
        f2.close();
    }

}
