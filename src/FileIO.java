import java.io.*;
import java.util.StringTokenizer;

public class FileIO
{
    public static void main(String[] args) throws Exception
    {
        //File f = new File("/Volumes/Data/Sub/test.smi");

        String path = "/Volumes/Data/Sub/test.smi";
        String text = readFileAsString("test.smi");
        text = text.replaceAll("<SYNC Start=","__").replaceAll("<br>","@@").replaceAll(">","&").replaceAll("<P Class=KRCC>","");
        StringTokenizer st = new StringTokenizer(text,"__");

        while(st.hasMoreTokens())
            System.out.println(st.nextToken());

 //       System.out.println(text);
    }

    private static String readFileAsString(String filePath) throws IOException
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
            readData = readData.replaceAll("(\r\n|\r|\n|\n\r)", " ").replace("<P Class=KRCC>","").replace("&nbsp;"," . ");
            if (readData.toUpperCase().indexOf("BODY") != -1)
            {
                fileData.append(readData,readData.toUpperCase().indexOf("<SYNC START"),readData.length());
            }
            else
                fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }
}
