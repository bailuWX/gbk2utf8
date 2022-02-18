

import java.io.*;

/*
 * 需求：复制多级文件夹 ,将文件夹里面的java文件转码成 utf -8,其他文件直接复制不处理
 *          (可以用来项目java文件转码.)
 *
 * 数据源：D:\\非uft8项目
 * 目的地：E:\\
 *
 * 分析：
 * 		A:封装数据源File
 * 		B:封装目的地File
 * 		C:判断该File是文件夹还是文件
 * 			a:是文件夹
 * 				就在目的地目录下创建该文件夹
 * 				获取该File对象下的所有文件或者文件夹File对象
 * 				遍历得到每一个File对象
 * 				回到C
 * 			b:是文件
 * 				满足要求就复制(缓冲字符流)
 *
 */
public class gbk2utf8 {
    public static void main(String[] args) throws IOException {
        // 封装数据源File
        File srcFile = new File("D:\\非uft8项目");
        // 封装目的地File
        File destFile = new File("d:\\");

        long start = System.currentTimeMillis();

        // 调用方法.
        copyFolder(srcFile, destFile);

        long end = System.currentTimeMillis();
        double getTime = (double) (end - start) / 1000;

        System.out.println("已经将目录下所有java文件转码成为 utf-8 !");
        System.out.println("总用时:" + getTime + "秒");
    }

    private static void copyFolder(File srcFile, File destFile)
            throws IOException {
        // 判断该File是文件夹还是文件
        if (srcFile.isDirectory()) {
            // 文件夹
            File newFolder = new File(destFile, srcFile.getName());
            newFolder.mkdir();

            // 获取该File对象下的所有文件或者文件夹File对象
            File[] fileArray = srcFile.listFiles();
            for (File file : fileArray) {
                copyFolder(file, newFolder);
            }
        } else if (srcFile.isFile()) {
            // 文件 // 如果式文件,那就复制文件
            File newFile = new File(destFile, srcFile.getName());
            if (srcFile.getName().endsWith(".java")) {//调用复制文件的方法
                copyFile2(srcFile, newFile); //如果是java就转码
            } else {
                copyFile(srcFile, newFile); //其他文件直接字节流复制
            }
        }
    }

    //字符转换流用来转码
    private static void copyFile2(File srcFile, File newFile) throws IOException {
        InputStreamReader isr = new InputStreamReader(new FileInputStream(srcFile), "GB2312");
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(newFile), "utf-8");

        char[] ch = new char[1024 * 8];
        int len = 0;
        while ((len = isr.read(ch)) != -1) {
            osw.write(ch, 0, len);
        }

        osw.close();
        isr.close();
    }

    //字节缓冲流可以用来复制任意类型的文件
    private static void copyFile(File srcFile, File newFile) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
                srcFile));
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(newFile));

        byte[] bys = new byte[1024 * 8];
        int len = 0;
        while ((len = bis.read(bys)) != -1) {
            bos.write(bys, 0, len);
        }

        bos.close();
        bis.close();
    }
}
