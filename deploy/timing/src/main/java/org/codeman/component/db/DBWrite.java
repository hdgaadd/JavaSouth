package org.codeman.component.db;

import org.codeman.component.repository.Clock;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author hdgaadd
 * created on 2022/10/03
 */
@Component
public class DBWrite {
    public void write(Clock clock) throws IOException {
        String fileName = System.getProperty("user.dir") + "\\src\\main\\resources\\" + "db";

        try (RandomAccessFile file = new RandomAccessFile(fileName, "rw")){
            // 指针指到文件末尾
            file.seek(file.length());

            file.writeBytes("\n" + clock.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}