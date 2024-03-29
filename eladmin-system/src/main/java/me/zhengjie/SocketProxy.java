package me.zhengjie;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import me.zhengjie.modules.app.domain.po.AppDynamicResult;
import me.zhengjie.modules.app.service.AppDynamicAnalyseService;
import me.zhengjie.modules.app.service.AppDynamicService;
import me.zhengjie.modules.app.service.impl.AppDynamicAnalyseServiceImpl;
import me.zhengjie.modules.app.service.impl.AppDynamicServiceImpl;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class HttpProxy extends Thread {
    static public int CONNECT_RETRIES = 5;
    static public int CONNECT_PAUSE = 5;
    static public int TIMEOUT = 50;
    static public int BUFSIZ = 1024;
    static public boolean logging = false;
    static public OutputStream log = null;
    // 传入数据用的Socket
    static public Socket socket;
    // 上级代理服务器，可选
    static private String parent = null;
    static private int parentPort = -1;

    static public void setParentProxy(String name, int pport) {
        parent = name;
        parentPort = pport;
    }

    // 在给定Socket上创建一个代理线程。
    public HttpProxy(Socket s) {
        socket = s;
        start();
    }

    public void writeLog(int c, boolean browser) throws IOException {
        log.write(c);
    }

    public void writeLog(byte[] bytes, int offset, int len, boolean browser)
            throws IOException {
        for (int i = 0; i < len; i++)
            writeLog((int) bytes[offset + i], browser);
    }

    // 默认情况下，日志信息输出到
    // 标准输出设备
    // 派生类可以覆盖它
    public String processHostName(String url, String host, int port, Socket sock) {
        java.text.DateFormat cal = java.text.DateFormat.getDateTimeInstance();
        System.out.println(cal.format(new java.util.Date()) + " - " + url + " "
                + sock.getInetAddress() + "\n");
        return host;
    }

    // 执行操作的线程
    public void run() {
        String line;
        String host;
        int port = 80;
        Socket outbound = null;
        try {
            socket.setSoTimeout(TIMEOUT);
            InputStream is = socket.getInputStream();
            OutputStream os = null;
            try {
                // 获取请求行的内容
                line = "";
                host = "";
                int state = 0;
                boolean space;
                while (true) {
                    int c = is.read();
                    if (c == -1)
                        break;
                    if (logging)
                        writeLog(c, true);
                    space = Character.isWhitespace((char) c);
                    switch (state) {
                        case 0:
                            if (space)
                                continue;
                            state = 1;
                        case 1:
                            if (space) {
                                state = 2;
                                continue;
                            }
                            line = line + (char) c;
                            break;
                        case 2:
                            if (space)
                                continue; // 跳过多个空白字符
                            state = 3;
                        case 3:
                            if (space) {
                                state = 4;
                                // 只取出主机名称部分
                                String host0 = host;
                                int n;
                                n = host.indexOf("//");
                                if (n != -1)
                                    host = host.substring(n + 2);
                                n = host.indexOf('/');
                                if (n != -1)
                                    host = host.substring(0, n);
                                // 分析可能存在的端口号
                                n = host.indexOf(":");
                                if (n != -1) {
                                    port = Integer.parseInt(host.substring(n + 1));
                                    host = host.substring(0, n);
                                }
                                host = processHostName(host0, host, port, socket);
                                if (parent != null) {
                                    host = parent;
                                    port = parentPort;
                                }
                                int retry = CONNECT_RETRIES;
                                while (retry-- != 0) {
                                    try {
                                        outbound = new Socket(host, port);
                                        break;
                                    } catch (Exception e) {
                                    }
                                    // 等待
                                    Thread.sleep(CONNECT_PAUSE);
                                }
                                if (outbound == null)
                                    break;
                                outbound.setSoTimeout(TIMEOUT);
                                os = outbound.getOutputStream();
                                os.write(line.getBytes());
                                os.write(' ');
                                os.write(host0.getBytes());
                                os.write(' ');
                                pipe(is, outbound.getInputStream(), os,
                                        socket.getOutputStream());
                                break;
                            }
                            host = host + (char) c;
                            break;
                    }
                }
            } catch (IOException e) {
            }

        } catch (Exception e) {
        } finally {
            try {
                socket.close();
            } catch (Exception e1) {
            }
            try {
                outbound.close();
            } catch (Exception e2) {
            }
        }
    }

    void pipe(InputStream is0, InputStream is1, OutputStream os0,
              OutputStream os1) throws IOException {
        try {
            int ir;
            byte bytes[] = new byte[BUFSIZ];
            while (true) {
                try {
                    if ((ir = is0.read(bytes)) > 0) {
                        os0.write(bytes, 0, ir);
                        if (logging)
                            writeLog(bytes, 0, ir, true);
                    } else if (ir < 0)
                        break;
                } catch (InterruptedIOException e) {
                }
                try {
                    if ((ir = is1.read(bytes)) > 0) {
                        os1.write(bytes, 0, ir);
                        if (logging)
                            writeLog(bytes, 0, ir, false);
                    } else if (ir < 0)
                        break;
                } catch (InterruptedIOException e) {
                }
            }
        } catch (Exception e0) {
            System.out.println("Pipe异常: " + e0);
        }
    }

    static public void startProxy(int port, Class clobj) {
        ServerSocket ssock;
        try {
            ssock = new ServerSocket(port);
            while (true) {
                Class[] sarg = new Class[1];
                Object[] arg = new Object[1];
                sarg[0] = Socket.class;
                try {
                    java.lang.reflect.Constructor cons = clobj
                            .getDeclaredConstructor(sarg);
                    arg[0] = ssock.accept();
                    cons.newInstance(arg); // 创建HttpProxy或其派生类的实例
                } catch (Exception e) {
                    Socket esock = (Socket) arg[0];
                    try {
                        esock.close();
                    } catch (Exception ec) {
                    }
                }
            }
        } catch (IOException e) {
        }
    }
    static public void stopProxy(){
        try {
            if(null!=HttpProxy.socket)
                HttpProxy.socket.close();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    // 测试用的简单main方法
    static public void main(String args[]) {
    //        System.out.println("在端口808启动代理服务器\n");
    //        HttpProxy.log = System.out;
    //        HttpProxy.logging = false;
    //        HttpProxy.startProxy(8888, HttpProxy.class);
       // AppDynamicService appDynamicService = new AppDynamicServiceImpl();
        //appDynamicService.installApp("D:\\apkdownload\\toutiao_unsigned_signed.apk","http://127.0.0.1:4723/wd/hub","192.168.0.101:5555");
//        appDynamicService.uninstallApp("com.wbiao.wbapp","http://127.0.0.1:4723/wd/hub","192.168.0.101:5555");

       String url = "http://115.231.37.143/dd.myapp.com/16891/apk/0BDF21363EDB4790E2962E85E7D57882.apk?mkey=604e388274eb2c57&f=24c3&fsname=http://126.com/com.neusoft.ebpp_2.18.0_21800.apk&cip=116.235.10.162&proto=http";

//        String url ="https://01.android2019-phone.s-e-m.cn:8090/ZhongXinYiYuanGuShi_yxdown.com.apk";
        String regex = "(ht|f)tp(s?)\\:\\/\\/[0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*(:(0-9)*)*(\\/?)([a-zA-Z0-9\\-\\.\\?\\,\\'\\/\\\\&%\\+\\$#_=]*)?";


        String pattern = "/(?<=http:\")([^\"]*?)(?=\")/g";
// 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);
// 现在创建 matcher 对象
        Matcher m = r.matcher(url);
        if(m.find()){
            String file = m.group();
            System.out.println(file);
        }
    }



}