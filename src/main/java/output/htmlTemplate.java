package output;

import statistics.MethodStatistic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class htmlTemplate {

    public static final String OUTPUT_DIR = "/Statistic report/new.html";

    public static String getStr(List<MethodStatistic> methodStatistics) {
        StringBuilder text = new StringBuilder("<html> <head> " +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; " +
                "charset=UTF-8\"> <title> Statistic Report" +
                "</title> </head> <body> <table align = \"center\" width=\"100%\" " +
                "style=\"text-align:center\" > <tr> <th>Method</th> <th>CC</th> " +
                "<th>Number of comments</th> <th>Number of statements</th> <th>Number of calls</th>" +
                "<th>Number of lines</th> <th>Lines of code</th> " +
                "<th>Has JavaDoc comment</th> <th>Does override something?</th></tr>");

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
            text.append("</tr>");
        }
        text.append("</table>  </body> </html>");

        return text.toString();
    }

    public static void writeToHTML(String path,
                                   List<MethodStatistic> methodStatistics) throws IOException {
        File file = new File(path + "/Statistic report");
        file.mkdir();
        String text = getStr(methodStatistics);
        File newHtmlFile = new File(path + "/Statistic report/new.html");
        try {
            FileWriter f2 = new FileWriter(newHtmlFile, false);
            f2.write(text);
            f2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
