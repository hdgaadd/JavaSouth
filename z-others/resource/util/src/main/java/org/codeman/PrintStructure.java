package org.codeman;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hdgaadd
 * Created on 2022/05/02
 *
 * design ideas: 递归
 *
 * Description: 循环遍历文件，打印项目结构
 */
public class PrintStructure {
    /**
     * 忽略文件夹
     */
    private static final List<String> IGNORE_DIRECTORY = new ArrayList<String>(){{
        add(".git");
        add(".idea");
        add("target");
        add("src");
    }};
    /**
     * 需加深遍历层数的模块
     */
    private static final List<String> AGAIN_LOOP = new ArrayList<String>(){{
        add("galaxy");
    }};
    /**
     * 遍历内容为src下的模块
     */
    private static final List<String> IS_SRC = new ArrayList<String>(){{
        add("earth");
        add("design-patterns");
    }};
    /**
     * 遍历层数限制
     */
    private static final int LAYERS = 2; // ['leɪəz]

    private static final String FIRST_DIRECTION_PARENT = "├── ";

    private static final String FIRST_DIRECTION_CHILD = "└── ";

    private static final String SECOND_DIRECTION_PARENT = "     ├── ";

    private static final String SECOND_DIRECTION_CHILD = "     └── ";

    public static void main(String[] args) {
        // 项目文件对象
        File file = new File(System.getProperty("user.dir"));
        System.out.println(file.getName());
        loopTraverse(file, FIRST_DIRECTION_PARENT, FIRST_DIRECTION_CHILD, 1, false);
    }

    /**
     * 循环遍历每一级别[trəˈvɜːs]
     *
     * @param isAgainLoop 是否加深遍历层数
     */
    private static void loopTraverse(File file, String directionParent, String directionChild, int fileIndex, boolean isAgainLoop) {
        File[] files = file.listFiles();
        // 文件夹尾端下标
        int endIndex = getDirectoryLen(file);
        for (int i = 0; i < files.length; i++) {
            File curFile = files[i];
            String curFileName = curFile.getName();;
            if (!IGNORE_DIRECTORY.contains(curFileName) && curFile.isDirectory()) {
                // 1. 获取装饰品├──
                String firstDecoration = (i == endIndex) ? directionChild : directionParent;
                // 若加深遍历层数 && 层数为2
                if (isAgainLoop && fileIndex == 2) {
                    firstDecoration = "     " + firstDecoration;
                }

                // 2. 模块描述
                String firstDescription = getDescription(curFile);
                System.out.println(firstDecoration + curFileName + firstDescription);

                // 3. 处理需要遍历内容为src下的模块
                if (IS_SRC.contains(curFileName)) {
                    curFile = new File(file.toString() + "\\" + curFileName + "\\src\\main\\java\\org\\codeman");
                }
                // 4. 循环遍历下一层
                if (fileIndex < LAYERS) {
                    if (AGAIN_LOOP.contains(curFileName)) { // 加深遍历层数，由于LAYERS限制，index必须取消 + 1
                        loopTraverse(curFile, SECOND_DIRECTION_PARENT, SECOND_DIRECTION_CHILD, fileIndex, true);
                    } else {
                        loopTraverse(curFile, SECOND_DIRECTION_PARENT, SECOND_DIRECTION_CHILD, fileIndex + 1, isAgainLoop);
                    }
                }
            }
        }
    }

    /**
     * 获取模块描述
     */
    private static String getDescription(File file) {
        for (File curFile : file.listFiles()) {
            if (curFile.isFile()) {
                String description = curFile.getName();
                if (!description.contains(".")) {
                    return " -- " + description;
                }
            }
        }
        return "";
    }

    /**
     * 获取某级别文件夹尾端下标，方便添加前缀："└── "
     */
    private static int getDirectoryLen(File file) {
        int index = -1;
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            File curFile = files[i];
            if (!IGNORE_DIRECTORY.contains(curFile.getName()) && curFile.isDirectory()) {
                index = i;
            }
        }
        return index;
    }
}
