package com.hz.fine.master.core.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class FileUtills {

  /**
   * 获取此路径下的所有文件名
   * 
   * @param path
   * @return
   */
  public static List<String> getAllFileNames(String path) {

    List<String> fileNameList = new ArrayList<String>();
    File file = new File(path);
    if (file.exists()) {
      if (file.isDirectory()) {
        for (File subFile : file.listFiles()) {
          if (file.isFile()) {
            fileNameList.add(subFile.getName());
          } else {
            fileNameList.addAll(getAllFileNames(subFile.getAbsolutePath()));
          }
        }
      } else {
        fileNameList.add(file.getName());
      }
    }

    return fileNameList;
  }

  /**
   * 获取当前目录下的文件名
   * 
   * @param path
   * @return
   */
  public static List<String> getFileNames(String path) {

    List<String> fileNameList = new ArrayList<String>();
    File file = new File(path);
    if (file.exists()) {
      if (file.isDirectory()) {
        for (File subFile : file.listFiles()) {
          if (file.isFile()) {
            fileNameList.add(subFile.getName());
          }
        }
      } else {
        fileNameList.add(file.getName());
      }
    }

    return fileNameList;
  }

  /**
   * 获取文件的MD5值
   * 
   * @param file
   * @return
   */
  public static String getFileMd5(File file) {

    try {
      MessageDigest messageDigest = MessageDigest.getInstance("MD5");
      FileInputStream fileInputStream = new FileInputStream(file);
      byte[] curByte = new byte[8192];
      int length = 0;
      while ((length = fileInputStream.read(curByte)) != -1) {
        messageDigest.update(curByte, 0, length);
      }
      String fileMd5 = new String(HexUtil.toHexString(messageDigest.digest()));
      fileInputStream.close();
      return fileMd5;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
