package me.zhengjie.utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class TemplateUtils {

    public static List<Map<String, Object>> getImgSrc(String htmlContent) {
        List<Map<String, Object>> srcList = new ArrayList<>(); //用来存储获取到的地址
        Map<String, Object> map = null;
        Pattern p = Pattern.compile("<(img|IMG)(.*?)(>|></img>|/>)");//匹配字符串中的img标签
        Matcher matcher = p.matcher(htmlContent);
        boolean hasPic = matcher.find();
        if (hasPic == true)//判断是否含有图片
        {
            while (hasPic) //如果含有图片，那么持续进行查找，直到匹配不到
            {
                String group = matcher.group(2);//获取第二个分组的内容，也就是 (.*?)匹配到的
                Pattern srcText = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')");//匹配图片的地址
                Matcher matcher2 = srcText.matcher(group);
                if (matcher2.find()) {
                    map = new HashMap<>();
                    map.put("imgResourcePath", matcher2.group(3));
                    srcList.add(map);//把获取到的图片地址添加到列表中
                    map = null;
                }
                hasPic = matcher.find();//判断是否还有img标签
            }
        }
        return srcList;
    }

    public static List<Map<String, Object>> getVideoSrc(String htmlContent) {
        List<Map<String, Object>> srcList = new ArrayList<>(); //用来存储获取到的视频地址
        Map<String, Object> map = null;
        Pattern p = Pattern.compile("<(video|VIDEO)(.*?)(>|></video>|/>)");//匹配字符串中的video标签
        Matcher matcher = p.matcher(htmlContent);
        boolean hasPic = matcher.find();
        if (hasPic == true)//判断是否含有视频
        {
            while (hasPic) //如果含有视频，那么持续进行查找，直到匹配不到
            {
                String group = matcher.group(2);//获取第二个分组的内容，也就是 (.*?)匹配到的
                Pattern srcText = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')");//匹配视频的地址
                Matcher matcher2 = srcText.matcher(group);
                if (matcher2.find()) {
                    map = new HashMap<>();
                    map.put("videoResourcePath", matcher2.group(3));
                    srcList.add(map);//把获取到的视频地址添加到列表中
                    map = null;
                }
                hasPic = matcher.find();//判断是否还有video标签
            }
        }
        return srcList;
    }

    public static List<Map<String, Object>> getAhref(String htmlContent) {
        List<Map<String, Object>> srcList = new ArrayList<>(); //用来存储获取到的超链接地址
        Map<String, Object> map = null;
        Pattern p = Pattern.compile("<(a|A)(.*?)(>|></a>|/>)");//匹配字符串中的a标签
        Matcher matcher = p.matcher(htmlContent);
        boolean hasPic = matcher.find();
        if (hasPic == true)//判断是否含有超链接
        {
            while (hasPic) //如果含有超链接，那么持续进行查找，直到匹配不到
            {
                String group = matcher.group(2);//获取第二个分组的内容，也就是 (.*?)匹配到的
                Pattern srcText = Pattern.compile("(href|HREF)=(\"|\')(.*?)(\"|\')");//匹配超链接的地址
                Matcher matcher2 = srcText.matcher(group);
                if (matcher2.find()) {
                    map = new HashMap<>();
                    map.put("aResourcePath", matcher2.group(3));
                    srcList.add(map);//把获取到的超链接地址添加到列表中
                    map = null;
                }
                hasPic = matcher.find();//判断是否还有a标签
            }
        }
        return srcList;
    }
}
