package com.hdw.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description 多线程读文件
 * @Author TuMinglong
 * @Date 2018/11/14 11:48
 */
public class ReadFileThread {
    // 控制线程数，最优选择是处理器线程数*3，本机处理器是4线程
    private final static int THREAD_COUNT = 12;
    // 线程共享数据，保存所有的type文件
    private ArrayList<File> papList = new ArrayList<File>();
    // 保存文件附加信息
    private ArrayList<String> contenList = new ArrayList<String>();
    // 当前文件或者目录
    private File file;

    //所需的文件类型
    private String type;

    public ReadFileThread() {
        super();
    }

    /**
     * @param filePath 文件夹路径
     * @param type     所需文件类型
     */
    public ReadFileThread(String filePath, String type) {
        super();
        this.file = new File(filePath);
        this.type = type;
    }

    public ArrayList<String> getContenList() {
        return contenList;
    }

    // 内部类继承runnable接口，实现多线程
    class FileThread implements Runnable {
        private File file;
        private String type;

        public FileThread(File file, String type) {
            super();
            this.file = file;
            this.type = type;
        }

        public FileThread() {
            super();
        }

        @Override
        public void run() {
            try {
                quickFind();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 非递归深度遍历算法
        void quickFind() throws IOException {
            // 使用栈，进行深度遍历
            Stack<File> stk = new Stack<File>();
            stk.push(this.file);
            File f;
            while (!stk.empty()) {
                f = stk.pop();
                if (f.isDirectory()) {
                    File[] fs = f.listFiles();
                    if (fs != null)
                        for (int i = 0; i < fs.length; i++) {
                            stk.push(fs[i]);
                        }
                } else {
                    if (f.getName().endsWith(type)) {
                        // 记录所需文件的信息
                        papList.add(f);
                    }
                }
            }
        }
    }

    public ArrayList<File> getPapList() {
        // 外部接口，传递遍历结果
        return papList;
    }

    /**
     * 深度遍历算法加调用线程池
     */
    void File() {
        File fl = this.file;
        ArrayList<File> flist = new ArrayList<File>();
        ArrayList<File> flist2 = new ArrayList<File>();
        ArrayList<File> tmp = null, next = null;
        flist.add(fl);
        // 广度遍历层数控制
        int loop = 0;
        while (loop++ < 3) {// 最优循环层数是3层，多次实验得出
            tmp = tmp == flist ? flist2 : flist;
            next = next == flist2 ? flist : flist2;
            for (int i = 0; i < tmp.size(); i++) {
                fl = tmp.get(i);
                if (fl != null) {
                    if (fl.isDirectory()) {
                        File[] fls = fl.listFiles();
                        if (fls != null) {
                            next.addAll(Arrays.asList(fls));
                        }
                    } else {
                        if (fl.getName().endsWith(type)) {
                            papList.add(fl);
                        }
                    }
                }
            }
            tmp.clear();
        }

        // 创建线程池，一共THREAD_COUNT个线程可以使用
        ExecutorService pool = Executors.newFixedThreadPool(THREAD_COUNT);

        for (File file : next) {
            pool.submit(new FileThread(file, type));
        }
        pool.shutdown();
        // 必须等到所有线程结束才可以让主线程退出，不然就一直阻塞
        while (!pool.isTerminated())
            ;
    }

    void info(File file) throws IOException {
        InputStream inputStream = new FileInputStream(file);
        byte[] chs = new byte[(int) file.length()];
        inputStream.read(chs);
        inputStream.close();
        String javaCode = new String(chs);
        String[] lines = javaCode.split("\n");
        int find = lines.length;// 实际代码行数
        int counts = find;// 加上注释的行数
        int zhushi = 0;
        for (int i = 0; i < lines.length; i++) {
            lines[i] = lines[i].trim();
            if (lines[i].length() == 0) {
                counts--;
                find--;
            } else if (lines[i].startsWith("//")) {
                // System.out.println("单行注释："+lines[i]);
                find--;
                zhushi++;
            } else if (lines[i].indexOf("/*") != -1) {
                find--;
                zhushi++;
                while (lines[i].indexOf("*/") == -1) {
                    // System.out.println(lines[i]);
                    find--;
                    zhushi++;
                    i++;
                }
            }
        }

        double zc = ((double) zhushi / counts) * 100;
        BigDecimal b = new BigDecimal(zc);
        double zcc = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        String s = file.getName() + "代码行数:" + find + "\t注释行数：" + zhushi + "\t 注释率：" + zcc + "%";

        contenList.add(s);
    }
}