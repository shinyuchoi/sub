import java.io.*;
import java.util.ArrayList;

public class FileIO
{


    private ArrayList<String> sub;
    private ArrayList<Integer> timeStamp;

    public ArrayList<String> getSub()
    {
        return sub;
    }

    public ArrayList<Integer> getTimeStamp()
    {
        return timeStamp;
    }


    FileIO(String filePath) throws Exception
    {
        smiToString(filePath);
    }

    private void srtToString(String path_win) throws Exception
    {
        sub = new ArrayList<String>();
        timeStamp = new ArrayList<Integer>();

        String str = dataIntoOneLine(path_win);
    }


    /*
    *   input>> *.smi filePath
    *   output>> sub,timeStamp Array
    */

    private void smiToString(String path_win) throws Exception
    {



        sub = new ArrayList<String>();
        timeStamp = new ArrayList<Integer>();

        String str = dataIntoOneLine(path_win);
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
                sub.add( str.substring(pointerTwo + 1, pointerOne - 1));
            else
                sub.add(str.substring(pointerTwo + 1, str.indexOf("</BODY>")));
        }
    }

    private String dataIntoOneLine(String filePath) throws Exception
    {

        StringBuffer fileData = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "euc-kr"));

        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1)
        {
            String readData = String.valueOf(buf, 0, numRead);
            readData = readData.replaceAll("(\r\n|\r|\n|\n\r)", " ");

            fileData.append(readData);
        }
        reader.close();

        return fileData.toString();
    }

}
