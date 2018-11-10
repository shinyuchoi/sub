import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class test
{
    public static void main(String[] args) throws Exception
    {
        FileIO fi = new FileIO("C:/Users/Choi/Desktop/test.smi");

        ArrayList<Integer> timeStamp = fi.getTimeStamp();
        ArrayList<String> sub = fi.getSub();

    }
}
