package com.github.huoyu820125.idstar.file;

import com.github.huoyu820125.idstar.error.RClassify;
import com.github.huoyu820125.idstar.stream.ReadStream;
import com.github.huoyu820125.idstar.stream.WriteStream;
import org.apache.commons.io.FileUtils;

import java.io.*;

/**
 * 磁盘文件
 * @author SunQian
 * @version 2.0
 */
public class DiskFile {
    private File file;

    public DiskFile(String filePathName) {
        file = new File(filePathName);
    }

    public DiskFile(File file) {
        file = file;
    }

    public String path() {
        String path = file.getPath();

        //删除末尾的斜杠
        char ws = '/';
        char ls = '\\';
        char last = path.charAt(path.length() -  1);
        if (last == ws || last == ls) {
            path.substring(0, path.length() - 1);
        }

        return path;
    }

    public String name() {
        return file.getName();
    }

    public Boolean exists() {
        return file.exists();
    }

    public void create() {
        try {
            FileUtils.touch(file);
        } catch (IOException e) {
            throw RClassify.bug.exception("创建文件异常", e);
        }
    }

    /**
     * 文件大小
     * @author: SunQian
     * @return todo
     */
    public Long size() {
        Long fileSize = null;
        ReadStream reader = null;
        try {
            if (!exists()) {
                throw RClassify.refused.exception("文件:" + path() + File.separator + name() + "不存在");
            }
            FileInputStream inputStream = new FileInputStream(file);
            fileSize = inputStream.getChannel().size();
            reader = new ReadStream(inputStream, fileSize, true);
        } catch (FileNotFoundException e) {
            throw RClassify.refused.exception("文件不存在" + e.getMessage());
        } catch (IOException e) {
            throw RClassify.refused.exception("文件不可读" + e.getMessage());
        }
        finally {
            if (null != reader) {
                reader.close();
            }
        }

        return fileSize;
    }

    /**
     * 开始读文件
     * @author: SunQian
     * @param authClose
     * @return
    */
    public ReadStream startRead(boolean authClose) {
        FileInputStream inputStream = null;
        Long fileSize = null;
        try {
            if (!exists()) {
                throw RClassify.refused.exception("文件:" + path() + File.separator + name() + "不存在");
            }
            inputStream = new FileInputStream(file);
            fileSize = inputStream.getChannel().size();
        } catch (FileNotFoundException e) {
            throw RClassify.refused.exception("文件不存在" + e.getMessage());
        } catch (IOException e) {
            throw RClassify.refused.exception("文件不可读" + e.getMessage());
        }

        return new ReadStream(inputStream, fileSize, authClose);
    }

    /**
     * startWrite开始写入
     * @author: SunQian
     * @return todo
    */
    public WriteStream startWrite() {
        return startWrite(true, false);
    }

    /**
     * 开始写入
     * @author: SunQian
     * @param autoCreate    文件不存在时，自动创建
     * @param override      是否覆盖写入：true是
     * @return
    */
    public WriteStream startWrite(Boolean autoCreate, Boolean override) {
        if (!exists()) {
            create();
        }

        FileOutputStream outputStream = null;
        try {
            if (!exists()) {
                throw RClassify.refused.exception("文件:" + path() + File.separator + name() + "不存在");
            }
            outputStream = new FileOutputStream(file, !override);
        } catch (FileNotFoundException e) {
            throw RClassify.refused.exception("文件不存在" + e.getMessage());
        } catch (IOException e) {
            throw RClassify.refused.exception("文件不可读" + e.getMessage());
        }

        return new WriteStream(outputStream);
    }
}
