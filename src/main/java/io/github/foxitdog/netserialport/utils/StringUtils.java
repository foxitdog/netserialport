package io.github.foxitdog.netserialport.utils;

import org.apache.commons.io.output.StringBuilderWriter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

/**
 * 字符串工具
 *
 * @author sudy-liangyun
 */
public class StringUtils extends Converter {

    /**
     * 26字母+0-9
     */
    final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

    /**
     * 获取随机字符
     *
     * @param num 个数
     * @return
     */
    public static String getRandomString(int num) {
        StringBuffer sb = new StringBuffer(num);
        Random r = new Random();
        for (int i = num; i > 0; i--) {
            sb.append(digits[r.nextInt(36)]);
        }
        return sb.toString();
    }

    /**
     * 字节转为十六进制字符串
     *
     * @return 十六进制字符串
     * @param字节
     */
    public static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder(b.length*2);
        String stmp = "";
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs .append( "0" + stmp);
            } else {
                hs.append(stmp);
            }
        }
        return hs.toString();
    }

    /**
     * 字节转为十六进制字符串c格式
     *
     * @return 十六进制字符串
     * @param字节
     */
    public static String byte2hex4c(byte[] b) {
        StringBuilder hs = new StringBuilder(b.length*4);;
        String stmp = "";
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs .append("\\x0" + stmp);
            } else {

                hs .append("\\x" + stmp);
            }
        }
        return hs.toString();
    }

    /**
     * 十六进制字符转为字节
     *
     * @return 字节
     */
    public static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0){
            throw new IllegalArgumentException("byte length is not correct");
        }
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    /**
     * 字符串转换成十六进制值
     *
     * @param bin String 我们看到的要转换成十六进制的字符串
     * @return
     */
    public static String bin2hex(String bin) {
        char[] digital = "0123456789ABCDEF".toCharArray();
        StringBuffer sb = new StringBuffer("");
        byte[] bs = bin.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(digital[bit]);
            bit = bs[i] & 0x0f;
            sb.append(digital[bit]);
        }
        return sb.toString();
    }

    /**
     * 字符串是否为空
     *
     * @param s
     * @return
     */
    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static String pingUrlParam(String url, Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        sb.append(url + "?");
        Set<String> keySet = map.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        boolean isFirst = true;
        for (String k : keyArray) {
            if (map.get(k) == null) {
                continue;
            }
            if (!isFirst) {
                sb.append("&");
            }
            sb.append(k).append("=").append(map.get(k));
            isFirst = false;
        }
        return sb.toString();
    }

    /**
     * 获取随机字符串 Nonce Str
     *
     * @return String 随机字符串
     */
    public static String generateNonceStr() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }

    public static String replaceEnter(String str) {
        return str.replace("\r", "").replace("\n", "");
    }

    /**
     * 获取错误栈字符串
     * 
     * @param e exception
     * @return 错误栈字符串
     */
    public static String getExceptionStack(Throwable e) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(baos));
        return baos.toString();
    }

    /**
     * 参数替换<br/>
     * e.g. 模版 + 参数 --> 结果 ;<br/>
     * {}xxx + 1,2,3 --> 1xxx ;<br/>
     * xxxx + 1,2,3 --> xxxx ;<br/>
     * x{}xx + 1,2,3 -->x1xx ;
     *
     * @param obj    信息内容
     * @param params 替换的参数
     * @return 替换后的内容
     */
    public static String paramReplace(String ParamPattern, Object obj, Object... params) {
        if (ParamPattern == null) {
            return "";
        }
        if (obj == null) {
            return "";
        }
        String message = obj.toString();
        if (params == null) {
            return message;
        }
        StringBuilder sb = new StringBuilder();
        int start = 0;
        int end = 0;
        int index = 0;
        while (true) {
            if (index >= params.length) {
                sb.append(message.substring(start));
                break;
            }
            end = message.indexOf(ParamPattern, start);
            if (end == -1) {
                sb.append(message.substring(start));
                break;
            }
            if (end == message.length() - ParamPattern.length()) {
                sb.append(message.substring(start, end));
                sb.append(params[index]);
                break;
            }
            if (start == end) {
                sb.append(params[index++]);
            } else {
                sb.append(message.substring(start, end));
                sb.append(params[index++]);
            }
            start = end + ParamPattern.length();
        }
        return sb.toString();
    }

    public static String printStackTrace(Exception e) {
        StringBuilderWriter sbw = new StringBuilderWriter();
        PrintWriter pw = new PrintWriter(sbw);
        e.printStackTrace(pw);
        return sbw.getBuilder().toString();
    }

    /**
     * 获取方法名 0 = 调用该方法的方法名 i = 调用该方法的上i级方法
     * 
     * @param i
     * @return
     */
    public static String getMethodName(int i) {
        i = 3 + i;
        StackTraceElement[] traces = Thread.currentThread().getStackTrace();
        if (traces.length > i) {
            return traces[i].getMethodName();
        } else {
            System.err.println("请求的堆栈大于现在的堆栈");
            return "";
        }
    }

    /**
     * 版本号比较 x.x.x
     * 
     * @param v1 版本1
     * @param v2 版本2
     * @return 0 相等，>0 v1>v2 <0 v1<v2
     */
    public static int compareVersion(String v1, String v2) {
        String[] v1a = v1.split("\\.");
        String[] v2a = v2.split("\\.");
        int k = 0;
        if (v2a.length >= v1a.length) {
            k = v1a.length;
        } else {
            k = v2a.length;
        }
        try {

            for (int i = 0; i < k; i++) {
                String v1c = v1a[i];
                String v2c = v2a[i];
                if (Integer.parseInt(v1c) > Integer.parseInt(v2c)) {
                    return 1;
                } else if (Integer.parseInt(v1c) < Integer.parseInt(v2c)) {
                    return -1;
                }
            }
            return v1a.length - v2a.length;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
