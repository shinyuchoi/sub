import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class test
{
    public static void main(String[] args)
    {
        String st1 = "<SYNC START=645188> 이건 <font color=yellow>'스팅'</font>이야 \n";
        String st = "aabbccddee";
        String pattern = "^SYNC([\\S]|[\\s])*$";
        String pattern2 = "^([\\S]|[\\s])*>([\\S]|[\\s])*$";
        String pt = "aa";

        StringTokenizer stk = new StringTokenizer(st1, "<");
        while (stk.hasMoreTokens())
        {
            String tmp = stk.nextToken();
            if(tmp.matches(pattern2))
                System.out.println(tmp);
        }

//        System.out.println(st1.replaceAll(pattern,""));
        //   System.out.println(Pattern.matches(pattern,st1));
    }
}
