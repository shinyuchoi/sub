import java.io.*;


public class Sub
{
    public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException
    {
        String path = "C:\\Users\\\\Chois\\Desktop";
        String fileName = "test.smi";
        String file = path+"\\"+fileName;
        String outputFile = "output.dat";

        FileInputStream fis = null;
        
        fis = new FileInputStream(file);
        InputStreamReader streamReader = new InputStreamReader(fis, "euc-kr");
        
        
        LineNumberReader reader = null;

        try
        {
            reader = new LineNumberReader(streamReader);
            String line = null;
            try(FileWriter fileWriter =
                        new FileWriter(outputFile) )
            {
                while ((line = reader.readLine()) != null)
                {
                	fileWriter.write("Line Number " + reader.getLineNumber() + " : " + line+"\n");

                }


            }

        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (reader != null) reader.close();
            } catch (IOException e)
            {
            }
        }


    }
}


