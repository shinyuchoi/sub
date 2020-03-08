import org.mozilla.universalchardet.UniversalDetector;

import java.io.*;
import java.util.ArrayList;


public class FileIO {


    static private ArrayList<String> sub;
    static private ArrayList<Integer> timeStamp;

    //파일을 읽어서 한줄로 만들어줌
    static String fileToOneLine(String filePath) throws Exception {

        //file path
        File file = new File(filePath);
        //Charset of subtitle
        String subCharset = new UniversalDetector().detectCharset(file);
        System.out.println(subCharset);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), subCharset));

        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            line.replaceAll("(\r\n|\r|\n|\n\r)", " ");
            result += line;
        }
        bufferedReader.close();

        return result;
    }


    static void smiToString(String path_win) throws Exception
    {
        sub = new ArrayList<String>();
        timeStamp = new ArrayList<Integer>();

        String str = fileToOneLine(path_win);
        int startPoint = str.toUpperCase().indexOf("<BODY>");

        int pointerOne = str.indexOf("SYNC Start", startPoint);
        while (pointerOne != -1)
        {
            pointerOne = str.indexOf("=", pointerOne);
            int pointerTwo = str.indexOf(">", pointerOne);


            String tmp = str.substring(pointerOne + 1, pointerTwo);
            tmp = tmp.replace(" ", "");

            timeStamp.add(Integer.parseInt(tmp));


            pointerOne = str.indexOf("SYNC Start", pointerTwo);

            if (pointerOne != -1)
                sub.add("<html><strong><font size =80 color=white>"+ str.substring(pointerTwo + 1, pointerOne - 1)+ "</font> </html>");
            else
                sub.add("<html><strong><font size =80 color=white>"+str.substring(pointerTwo + 1, str.indexOf("</BODY>"))+" </font> </html>");
        }
    }

    static void test() throws Exception {
        File[] files = new File("sample\\").listFiles();
        for (File f : files) {
            smiToString(f.getAbsolutePath());
        }

    }

    public static void main(String[] args) throws Exception {
        String fP = "2.smi";
        smiToString(fP);
        System.out.println(sub.size());
        System.out.println(timeStamp.size());
        test();


    }
}
