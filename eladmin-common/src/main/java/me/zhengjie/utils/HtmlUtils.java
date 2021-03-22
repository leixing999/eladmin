package me.zhengjie.utils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import java.util.List;
public class HtmlUtils {
    private static String extractText(Node node){
        /* TextNode直接返回结果 */
        if(node instanceof TextNode){
            return ((TextNode) node).text();
        }
        /* 非TextNode的Node，遍历其孩子Node */
        List<Node> children = node.childNodes();
        StringBuffer buffer = new StringBuffer();
        for (Node child: children) {
            buffer.append(extractText(child));
        }
        return buffer.toString();
    }
    /* 使用jsoup解析html并转化为提取字符串*/
    public static String html2Str(String html){
        Document doc = Jsoup.parse(html);
        return extractText(doc);
    }
}
