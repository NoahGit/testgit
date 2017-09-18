package sec;

import java.io.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Date;


public class YunxinTest   //ע���Լ���������Ӧ�ã�
{
    public static void main(String[] args) throws Exception
    {
        System.out.println(sendMsg());


    }


    /**
     * ����POST����������
     * 
     * @return ������Զ����Դ����Ӧ���
     */
    public static String sendMsg()
    {
        String appKey = "e8aa565eb1b054c4e708742f7ef589c1";
        String appSecret = "40bb82e1063a";
        String nonce = "baoluo"; // ���������󳤶�128���ַ���
        String curTime = String.valueOf((new Date()).getTime() / 1000L); // ��ǰUTCʱ���
        System.out.println("curTime: " + curTime);


        String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce, curTime);
        System.out.println("checkSum: " + checkSum);


        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try
        {
            String url = "https://api.netease.im/sms/sendtemplate.action"; //��ַ���Բ��޸�
            String encStr1 = URLEncoder.encode("Tom", "utf-8");
            String encStr2 = URLEncoder.encode("name", "utf-8"); // url���룻��ֹ��ʶ������
            String params = "templateid=3033519&mobiles=[\"13310945568\"]" 
                            + "&params=" + "[\"" + encStr1 + "\",\""+ encStr2 + "\"]";
            System.out.println("params��" + params);


            URL realUrl = new URL(url);
            // �򿪺�URL֮�������
            URLConnection conn = realUrl.openConnection();
            // ����ͨ�õ���������
            conn.setRequestProperty("AppKey", appKey);
            conn.setRequestProperty("CheckSum", checkSum);
            conn.setRequestProperty("CurTime", curTime);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            conn.setRequestProperty("Nonce", nonce);


            // ����POST�������������������
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // ��ȡURLConnection�����Ӧ�������
            out = new PrintWriter(conn.getOutputStream());
     


            // �����������
            out.print(params);
            // flush������Ļ���
            out.flush();
            // ����BufferedReader����������ȡURL����Ӧ
            System.out.println(conn);
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            System.out.println("in"+in);
            String line;
            while ((line = in.readLine()) != null)
            {
                result += line;
            }
        } catch (Exception e)
        {
            System.out.println("���� POST ��������쳣��\n" + e);
            e.printStackTrace();
        }
        // ʹ��finally�����ر��������������
        finally
        {
            try
            {
                if (out != null)
                {
                    out.close();
                }
                if (in != null)
                {
                    in.close();
                }
            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
        return result;
    }
    
}
class CheckSumBuilder {
    // ���㲢��ȡCheckSum
    public static String getCheckSum(String appSecret, String nonce, String curTime) {
        return encode("sha1", appSecret + nonce + curTime);
    }


    // ���㲢��ȡmd5ֵ
    public static String getMD5(String requestBody) {
        return encode("md5", requestBody);
    }


    private static String encode(String algorithm, String value) {
        if (value == null) {
            return null;
        }
        try {
            MessageDigest messageDigest
                    = MessageDigest.getInstance(algorithm);
            messageDigest.update(value.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }
    private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
}