package me.zhengjie;

import me.zhengjie.modules.app.domain.po.AppDynamicResult;
import me.zhengjie.modules.app.service.AppDynamicAnalyseService;
import me.zhengjie.modules.app.service.impl.AppDynamicAnalyseServiceImpl;
import me.zhengjie.utils.FileSearcher;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AppDumpAnalyse {
    public static void dumpAnalyse(){

        try {

            File file = new File("H:\\apkdownload\\litetao_android_10002859.apk");

            String cmd = "cmd /c E: && cd E:\\software\\jadx\\jadx1\\bin && jadx -d " + "H:\\apkpath" + "\\" + file.getName().substring(0, file.getName().length() - 4) + " " +
                    file.getAbsolutePath();

            //解包APK
            Process process = Runtime.getRuntime().exec(cmd);

            final InputStream in = process.getInputStream();

            int ch;

            while ((ch = in.read()) != -1) {
                System.out.print((char) ch);
            }

            //关闭进程
            process.destroy();

            //list.add(new File(f.getAbsolutePath().substring(0, f.getAbsolutePath().length() - 4)));

            //  f.renameTo(new File(f.getAbsolutePath() + ".1"));

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    public static void main(String[] args) {



    }
}
