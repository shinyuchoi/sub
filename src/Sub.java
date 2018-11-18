import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Sub
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




    private void srtToString(String path_win) throws Exception
    {
        sub = new ArrayList<String>();
        timeStamp = new ArrayList<Integer>();


    }

    public static void main(String[] args) throws Exception
    {
        System.out.println(dataIntoaOneLine("ttesst.srt"));

    }

    static String dataIntoaOneLine(String filePath) throws Exception
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

