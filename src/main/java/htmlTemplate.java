public class htmlTemplate {
    public static String getStr( String name, int value1, String name2, int value2 ){
       String text =  "<html> <head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"> <title> Statistic Report"+
               "</title> </head> <body> <table> <tr> <th>Metrics</th> <th>result</th> </tr> <tr>"+
    "<td>"+ name +"</td>  <td>"+ value1 +"</td> </tr> <tr> <td>"+ name2 +"</td> <td>"+ value2 +"</td> </tr> </table>  </body> </html>";
       return text;
    }
}
