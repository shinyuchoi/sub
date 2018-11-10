import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

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
        createSubtitleAndTimestamp(filePath);

    }

    private void createSubtitleAndTimestamp(String path_win) throws Exception
    {

        //String path_win = "C:/Users/Choi/Desktop/test.smi";


        sub = new ArrayList<String>();
        timeStamp = new ArrayList<Integer>();

        String str = readFileToStringArray(path_win);
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
                sub.add(str.substring(pointerTwo + 1, pointerOne));
            else
                sub.add(str.substring(pointerTwo + 1, str.indexOf("</BODY>")));
       }
    }

        private String readFileToStringArray (String filePath) throws Exception
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


        private static String readFileAsString (String filePath) throws IOException
        {
            StringBuffer fileData = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "euc-kr"));

            char[] buf = new char[1024];
            int numRead = 0;

            //내가 추가
            //

            while ((numRead = reader.read(buf)) != -1)
            {
                String readData = String.valueOf(buf, 0, numRead);
                readData = readData.replaceAll("(\r\n|\r|\n|\n\r)", " ").replace("<P Class=KRCC>", "").replace("&nbsp;", " . ");
                if (readData.toUpperCase().indexOf("BODY") != -1)
                {
                    fileData.append(readData, readData.toUpperCase().indexOf("<SYNC START"), readData.length());
                } else
                    fileData.append(readData);
            }
            reader.close();
            return fileData.toString();
        }
    }
