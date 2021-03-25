package me.zhengjie.utils;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileSearcher {
    /**
     * 递归查找文件
     *
     * @param baseDirName
     *            查找的文件夹路径
     * @param targetFileName
     *            需要查找的文件名
     *            查找到的文件集合
     */
    public static void findFiles(String baseDirName, String targetFileName, List<String> list)
    {
        /**
         * 算法简述： 从某个给定的需查找的文件夹出发，搜索该文件夹的所有子文件夹及文件，
         * 若为文件，则进行匹配，匹配成功则加入结果集，若为子文件夹，则进队列。 队列不空，重复上述操作，队列为空，程序结束，返回结果。
         */
        String tempName = null;
        // 判断目录是否存在
        File baseDir = new File(baseDirName);
        if (!baseDir.exists() || !baseDir.isDirectory())
        {
            System.out.println("文件查找失败：" + baseDirName + "不是一个目录！");
        }
        else
        {
            File[] filelist = baseDir.listFiles();
            for (int i = 0; i < filelist.length; i++)
            {
                File readfile = new File(filelist[i].toString());
                // System.out.println(readfile.getName());
                if (!readfile.isDirectory())
                {
                    tempName = readfile.getName();
                    if (FileSearcher.wildcardMatch(targetFileName, tempName))
                    {
                        // 匹配成功，将文件名添加到结果集
                        list.add(readfile.getAbsoluteFile().toString());

                    }
                }
                else if (readfile.isDirectory())
                {
                    findFiles( filelist[i].getAbsolutePath(), targetFileName,list);
                }
            }
        }

    }

    /**
     * 通配符匹配
     *
     * @param pattern
     *            通配符模式
     * @param str
     *            待匹配的字符串
     * @return 匹配成功则返回true，否则返回false
     */
    private static boolean wildcardMatch(String pattern, String str)
    {
        int patternLength = pattern.length();
        int strLength = str.length();
        int strIndex = 0;
        char ch;
        for (int patternIndex = 0; patternIndex < patternLength; patternIndex++)
        {
            ch = pattern.charAt(patternIndex);
            if (ch == '*')
            {
                // 通配符星号*表示可以匹配任意多个字符
                while (strIndex < strLength)
                {
                    if (wildcardMatch(pattern.substring(patternIndex + 1), str.substring(strIndex)))
                    {
                        return true;
                    }
                    strIndex++;
                }
            }
            else if (ch == '?')
            {
                // 通配符问号?表示匹配任意一个字符
                strIndex++;
                if (strIndex > strLength)
                {
                    // 表示str中已经没有字符匹配?了。
                    return false;
                }
            }
            else
            {
                if ((strIndex >= strLength) || (ch != str.charAt(strIndex)))
                {
                    return false;
                }
                strIndex++;
            }
        }
        return (strIndex == strLength);
    }

    public static void main(String[] paramert)
    {
        // 在此目录中找文件
        String baseDIR = "H:\\apkpath\\litetao_android_10002859\\";

        // 找扩展名为txt的文件
        String fileName = "*.json";

        List<String> list = new ArrayList<>();
        FileSearcher.findFiles(baseDIR, fileName,list);
        System.out.println(list.size());


    }
}
