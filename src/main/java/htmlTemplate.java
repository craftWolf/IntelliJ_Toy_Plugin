import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class htmlTemplate {
    public static String getStr(List<String> names, List<Integer> values, List<Boolean> javaDoc){
       StringBuilder text = new StringBuilder( "<html> <head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"> <title> Statistic Report"+
               "</title> </head> <body> <table> <tr> <th>Method</th> <th>CC</th> " +
               "<th>Number of comments</th> <th>Number of statements</th> <th>Has JavaDoc comment</th> </tr>");
       if (names.size() == 0) return "No methods in the file!";
       for (int i = 0; i< names.size(); i++){
           text.append("<tr> <td>").append(names.get(i)).append("</td>");
           for (int j = 0; j< values.size(); j++){
               text.append(" <td>").append(values.get(j + i * values.size())).append("</td>");
           }
           text.append(" <td>").append(javaDoc.get(i )).append("</td> </tr>");
       }
       text.append("</table>  </body> </html>");

       return text.toString();
    }

    public static void writeToHTML(String path) throws IOException {
        File file = new File(path + "/Statistic report");
        file.mkdir();
        String text = getStr(StatisticReport.getNames(), StatisticReport.getValues(), StatisticReport.getJavadoc());
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
