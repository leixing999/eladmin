package me.zhengjie.utils;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.util.*;

public class ParseXML {
    /**
     * @FieldName: ARRAY_PROPERTY
     * @FieldType: 数组节点
     * @Description: 数组节点
     */
    private static final List<String> ARRAY_PROPERTY = Arrays.asList(new String[]{"CustList","SMCust"});
    /**
     *
     * @param xml
     * @return
     * @throws Exception
     */
    public Set parserXml(String xml) throws Exception {
        StringReader reader = new StringReader(xml);
        // 创建一个新的SAXBuilder
        SAXReader sb = new SAXReader();
        //
        Map<String, Object> result = new HashMap<String, Object>();
        Set<String> rst = new HashSet<>();
        //
        try {
            // 通过输入源构造一个Document
            Document doc = sb.read(reader);
            // 取的根元素
            Element root = doc.getRootElement();

            this.getNodes(root,rst);
            //List children = root.elements();
            //解析xml
           // result = toMap(children,null);
        } catch (Exception e) {
            System.err.print(e.getMessage());
            throw e;
        } finally {
            if(reader != null) {
                reader.close();
            }
        }
        return rst;
    }

    /**
     * 递归解析xml，实现N层解析
     * @param elements
     * @param list
     */
    private Map<String, Object> toMap(List<Element> elements,List<Map<String,Object>> list){
        //
        Element el = null;
        String name = "";
        //
        Map<String, Object> map = new HashMap<String, Object>();
        //
        for(int i=0; i<elements.size();i++){
            el = (Element) elements.get(i);
            name = el.getName();



            //如果是定义成数组
            if(el.elements().size()>0) {
                //继续递归循环
                List<Map<String,Object>> sublist = new ArrayList<Map<String,Object>>();
                //
                Map<String,Object> subMap  = this.toMap(el.elements(),sublist);
                //根据key获取是否已经存在
                Object object = map.get(name);
                //如果存在,合并
                if(object !=null ){
                    List<Map<String,Object>> olist = (List<Map<String,Object>>)object;
                    olist.add(subMap);//
                    map.put(name, olist);
                }else{//否则直接存入map
                    map.put(name, sublist);
                }
            }else {//单个值存入map
                map.put(name,el.getTextTrim());
            }
        }
        //存入list中
        if(list!=null)
            list.add(map);
        //返回结果集合
        return map;
    }

    public void getNodes(Element node,Set<String> rst){
        //当前节点所有属性的list
        List<Attribute> list = node.attributes();
        //遍历当前节点的所有属性
        for (Attribute attribute : list) {
            rst.add(attribute.getValue());
          //  System.out.println("属性名称:"+attribute.getName()+"属性值:"+attribute.getValue());
        }

        //递归遍历当前节点所有的子节点
        List<Element> listElement = node.elements();//所有一级子节点的list
        for (Element e : listElement) {//遍历所有一级子节点
            this.getNodes(e,rst);//递归
        }
    }
    /**
     * 读取文本文件里的xml
     * @param xmlPath
     * @return
     * @throws Exception
     */
    private static String readXML(String xmlPath) throws Exception{
        //
        StringBuilder sb = new StringBuilder();
        //
        File file = new File(xmlPath);
        //
        if(file.exists() && file.isFile()){
            InputStreamReader rd = new InputStreamReader(new FileInputStream(file), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(rd);
            String str="";
            while((str = bufferedReader.readLine()) != null){
                sb.append(str);
            }
            rd.close();
        }
        //
        return sb.toString();
    }

    /**
     * main 方法
     * @param args
     */
    public static void main(String[] args) {
        //实例化对象
        ParseXML parseXML = new ParseXML();
        //获取xml
        String xml = new String();
        try {
            String xmlPath = "D:\\apkpath\\xmds07\\resources\\AndroidManifest.xml";
            xml = parseXML.readXML(xmlPath);

            //解析xml
            Set<String> set =  parseXML.parserXml(xml);

            Iterator<String> iterator = set.iterator();

            while(iterator.hasNext()){
                System.out.println(iterator.next());
            }

        } catch (Exception e) {
            //
            System.err.print(e.getMessage());
        }
    }
}
