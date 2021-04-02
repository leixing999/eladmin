package me.zhengjie.modules.app.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.app.domain.po.AppDynamicResult;
import me.zhengjie.modules.app.service.AppDynamicAnalyseService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.zhengjie.utils.HtmlUtils;

import me.zhengjie.utils.ParseXML;
import org.springframework.stereotype.Service;

@Service
public class AppDynamicAnalyseServiceImpl implements AppDynamicAnalyseService {

    /***
     * 动态将文本的内容解析为记录集
     * @param buffer
     * @return
     */
    @Override
    public Set getAppDynamicParseRecord(StringBuffer buffer) {
        String regex = "<xinglei>(.*?)</xinglei>";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(buffer.toString());
        HashSet<String> hashSet = new HashSet<>();

        while (m.find()) {
            int i = 1;
            String context = m.group(i);
            hashSet.add(context);
        }
        return hashSet;

    }

    /****
     * 按照动态解析将日志文件内容读取到字符串缓冲区里
     * @param logPath
     * @return
     */
    @Override
    public StringBuffer getAppDynamicParseLog(String logPath) {
        StringBuffer buffer = new StringBuffer();
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(new FileInputStream(new File(logPath)), StandardCharsets.UTF_16LE);
            BufferedReader br = new BufferedReader(reader);
            String line = "";

            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }

        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            try {
                reader.close();
                reader = null;
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
        return buffer;
    }

    @Override
    public StringBuffer getAppDynamicParseLog(String logPath, String charset) {
        StringBuffer buffer = new StringBuffer();
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(new FileInputStream(new File(logPath)), charset);
            BufferedReader br = new BufferedReader(reader);
            String line = "";

            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }

        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            try {
                reader.close();
                reader = null;
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
        return buffer;
    }

    /****
     * 将json数据转换为结果对象结果集
     * @param objJson
     * @param appDynamicResult
     */
    @Override
    public void analysisJson(Object objJson, AppDynamicResult appDynamicResult) {
        {
            HashMap<String, Object> map = new HashMap<>();
            //如果obj为json数组
            if (objJson instanceof JSONArray) {
                JSONArray objArray = (JSONArray) objJson;
                for (int i = 0; i < objArray.size(); i++) {
                    this.analysisJson(objArray.get(i), appDynamicResult);
                }
            }
            //如果为json对象
            else if (objJson instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) objJson;
                Iterator it = jsonObject.keySet().iterator();
                while (it.hasNext()) {
                    String key = it.next().toString();
                    Object object = jsonObject.get(key);
                    //如果得到的是数组
                    if (object instanceof JSONArray) {
                        JSONArray objArray = (JSONArray) object;
                        analysisJson(objArray, appDynamicResult);
                    }
                    //如果key中是一个json对象
                    else if (object instanceof JSONObject) {
                        analysisJson((JSONObject) object, appDynamicResult);
                    }
                    //如果key中是其他
                    else {

                        String patternZw = ".*[\u4e00-\u9fa5].*";
                        String patternWebSites = "(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&:/~\\+#]*[\\w\\-\\@?^=%&/~\\+#])?";
                        if (object != null) {
                            String objectStr = object.toString();
                            //获取动态Url
                            if (Pattern.matches(patternWebSites, objectStr)) {
                                //获取图像地址集合
                                /*if (objectStr.indexOf(".jpg") > 0 || objectStr.indexOf(".png") > 0 || objectStr.indexOf(".gif") > 0) {
                                   /// appDynamicResult.getAppUrlByImageSet().add(objectStr);
                                    String url = this.getDomain(objectStr);
                                    appDynamicResult.getAppUrlSet().add(url);
                                    continue;
                                }

                                //获取HTML地址集合
                                if (objectStr.indexOf(".html") > 0) {
                                   // appDynamicResult.getAppUrlByHtmlSet().add(objectStr);
                                    String url = this.getDomain(objectStr);
                                    appDynamicResult.getAppUrlSet().add(url);
                                } else {
                                    String url = this.getDomain(objectStr);
                                    appDynamicResult.getAppUrlSet().add(url);
                                }*/
                                String url = this.getDomain(objectStr);
                                if (url != null && url.length() > 0)
                                    appDynamicResult.getAppUrlSet().add(url);

                                continue;
                            }

                            //获取敏感词
                            objectStr = HtmlUtils.html2Str(object.toString()).trim();
                            //获取敏感词
                            if (Pattern.matches(patternZw, objectStr)) {
                                appDynamicResult.getAppSensiveSet().add(objectStr.trim());
                            }

                        }

                    }
                }
            }
        }
    }

    private String getDomain(String curl) {
        URL url = null;

        String q = "";

        try {
            url = new URL(curl);

            q = url.getHost();

        } catch (MalformedURLException e) {
        }

        url = null;

        return q;

    }

    /****
     * 将json数据转换为结果对象结果集
     * @param logPath
     * @return
     */
    @Override
    public AppDynamicResult analysisJson(String logPath) {
        HashSet hashSet = (HashSet) this.getAppDynamicParseRecord(
                this.getAppDynamicParseLog(logPath)
        );

        Iterator<String> iterator = hashSet.iterator();
        AppDynamicResult appDynamicResult = new AppDynamicResult();
        while (iterator.hasNext()) {
            String record = "";
            try {
                 record = iterator.next().trim();
                JSONObject objJson = JSONObject.parseObject(record);
                this.analysisJson(objJson, appDynamicResult);

            } catch (Exception ex) {
                //判断是否是url域名
//                String patternWebSites = "(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&:/~\\+#]*[\\w\\-\\@?^=%&/~\\+#])?";
//                String url = this.getDomain(record);
//                if (Pattern.matches(patternWebSites, record) || (url != null && url.length() > 0)) {
//                    url = this.getDomain(record);
//                    if (url != null && url.length() > 0)
//                        appDynamicResult.getAppUrlSet().add(url);
//                }

            }
        }

        return appDynamicResult;
    }

    /***
     * 将json转换成结果集合的重载方法
     * @param buffer
     * @return
     */
    @Override
    public AppDynamicResult analysisJson(StringBuffer buffer) {
        AppDynamicResult appDynamicResult = new AppDynamicResult();
        try {
            String record = buffer.toString();

            JSONObject objJson = JSONObject.parseObject(record);
            this.analysisJson(objJson, appDynamicResult);

        } catch (Exception ex) {
            System.out.println(ex);
        }

        return appDynamicResult;
    }

    /****
     * 对xml解析为set集合
     * @param buffer
     * @return
     */
    @Override
    public AppDynamicResult analysisXml(StringBuffer buffer) {
        //实例化对象
        ParseXML parseXML = new ParseXML();
        //获取xml
        String xml = new String();
        AppDynamicResult appDynamicResult = new AppDynamicResult();
        try {
            //解析xml
            Set<String> set = parseXML.parserXml(buffer.toString());
            Iterator<String> iterator = set.iterator();

            while (iterator.hasNext()) {

                String patternZw = ".*[\u4e00-\u9fa5].*";
                String patternWebSites = "(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&:/~\\+#]*[\\w\\-\\@?^=%&/~\\+#])?";
                String objectStr = iterator.next();
                //获取动态Url
                if (Pattern.matches(patternWebSites, objectStr)) {
                    String url = this.getDomain(objectStr);
                    if (url != null && url.length() > 0) {
                        appDynamicResult.getAppUrlSet().add(url);
                        continue;
                    }
                }
                //获取敏感词
                if (Pattern.matches(patternZw, objectStr)) {
                    appDynamicResult.getAppSensiveSet().add(objectStr.trim());
                }
            }

        } catch (Exception e) {
            //
            System.err.print(e.getMessage());
        }


        return appDynamicResult;
    }

    /****
     * 将response数据转换为结果对象结果集
     * @param logPath
     * @return
     */
    @Override
    public AppDynamicResult analysisResponse(String logPath) {
        HashSet hashSet = (HashSet) this.getAppDynamicParseRecord(
                this.getAppDynamicParseLog(logPath)
        );
        Iterator<String> iterator = hashSet.iterator();
        AppDynamicResult appDynamicResult = new AppDynamicResult();
        while (iterator.hasNext()) {
            try {
                String record = iterator.next().trim();
                appDynamicResult.getAppUrlSet().add(record);
            } catch (Exception ex) {
                System.out.println(ex);
            }

        }

        return appDynamicResult;
    }

    /****
     * 获取APP动态分析的结果集合
     * @param responsePath
     * @param requestPath
     * @return
     */
    @Override
    public AppDynamicResult getAppDynamicAnalysisResult(String responsePath, String requestPath) {

        AppDynamicResult requestDynamicResult = this.analysisJson(responsePath);
        AppDynamicResult responseDynamicResult = this.analysisResponse(requestPath);

        Set responseSet = responseDynamicResult.getAppUrlSet();
        Iterator<String> iterator = responseSet.iterator();
        while (iterator.hasNext()) {
            requestDynamicResult.getAppUrlSet().add(iterator.next().trim());
        }

        return requestDynamicResult;
    }
}
