package org.codeman;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hdgaadd
 * created on 2022/05/02
 *
 * design: 递归
 *
 * description: 循环遍历文件，打印项目结构
 */
@SuppressWarnings("ConstantConditions")
public class PrintStructure {
    /**
     * 忽略文件夹
     */
    private static final List<String> IGNORE_DIRECTORY = new ArrayList<String>() {{
        add(".git");
        add(".idea");
        add("target");
        add("src");
        add("out");
    }};
    /**
     * 需加深遍历层数的模块
     */
    private static final List<String> INCREASE_LOOP = new ArrayList<String>() {{
        add("galaxy");
        add("SpringBoot");
    }};
    /**
     * 需减少遍历层数的模块
     */
    private static final List<String> REDUCE_LOOP = new ArrayList<String>() {{
    }};
    /**
     * 遍历内容为src下的模块
     */
    private static final List<String> IS_SRC = new ArrayList<String>() {{
        add("jdk");
        add("design-patterns");
        add("concurrency");
    }};
    /**
     * 遍历层数限制
     */
    private static final int LAYERS = 2;

    private static final String FIRST_DIRECTION_PARENT = "├── ";

    private static final String FIRST_DIRECTION_CHILD = "└── ";

    private static final String SECOND_DIRECTION_PARENT = "     ├── ";

    private static final String SECOND_DIRECTION_CHILD = "     └── ";

    public static void main(String[] args) {
        File file = new File(System.getProperty("user.dir"));
        System.out.println(file.getName());
        loopTraverse(file, FIRST_DIRECTION_PARENT, FIRST_DIRECTION_CHILD, 1, false);
    }

    /**
     * 循环遍历每一级别
     *
     * @param file 文件对象
     * @param directionParent 父级装饰品
     * @param directionChild  子级装饰品
     * @param fileIndex 文件索引
     * @param isIncreaseLoop 是否加深遍历层数
     */
    private static void loopTraverse(File file, String directionParent, String directionChild, int fileIndex, boolean isIncreaseLoop) {
        File[] files = file.listFiles();
        // 文件夹尾端下标
        int endIndex = getDirectoryLen(file);
        for (int i = 0; i < files.length; i++) {
            File curFile = files[i];
            String curFileName = curFile.getName();
            if (fileIndex <= LAYERS && !IGNORE_DIRECTORY.contains(curFileName) && curFile.isDirectory()) {
                // 1. 获取装饰品├──
                String firstDecoration = (i == endIndex) ? directionChild : directionParent;
                // 若加深遍历层数 && 层数为2
                if (isIncreaseLoop && fileIndex == 2) {
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
                if (INCREASE_LOOP.contains(curFileName)) { // 加深遍历层数
                    loopTraverse(curFile, SECOND_DIRECTION_PARENT, SECOND_DIRECTION_CHILD, fileIndex, true);
                } else if (REDUCE_LOOP.contains(curFileName)) { // 减少遍历层数
                    loopTraverse(curFile, SECOND_DIRECTION_PARENT, SECOND_DIRECTION_CHILD, fileIndex + 2, true);
                } else { // normal遍历
                    loopTraverse(curFile, SECOND_DIRECTION_PARENT, SECOND_DIRECTION_CHILD, fileIndex + 1, isIncreaseLoop);
                }
            }
        }
    }

    /**
     * @param file 文件对象
     * @return 获取模块描述
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
     * @param file 文件对象
     * @return 某级别文件夹尾端下标，方便添加前缀: "└── "
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
