/**
 * 
 */
package com.hz.fine.master.core.common.utils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.util.List;

/**
 * @author yunfu.wang
 *
 */
public class FtpUtils {

  private Logger logger    = LoggerFactory.getLogger(FtpUtils.class);

  // ftp服务器地址
  public String    hostname  = "192.168.1.249";

  // ftp服务器端口号默认为21
  public Integer   port      = 21;

  // ftp登录账号
  public String    username  = "root";

  // ftp登录密码
  public String    password  = "123";

  public FTPClient ftpClient = null;

  public FtpUtils() {

  }

  public FtpUtils(String hostname, Integer port, String username, String password) {
    this.hostname = hostname;
    this.port = port;
    this.username = username;
    this.password = password;
    initFtpClient();
  }

  /**
   * 初始化ftp服务器
   */
  private void initFtpClient() {
    ftpClient = new FTPClient();
    ftpClient.setControlEncoding("utf-8");
    try {
      logger.info("connecting...ftp服务器:" + this.hostname + ":" + this.port);
      ftpClient.connect(hostname, port); // 连接ftp服务器
      // 登录ftp服务器
      if (ftpClient.login(username, password)) {
        int replyCode = ftpClient.getReplyCode(); // 是否成功登录服务器
        if (FTPReply.isPositiveCompletion(replyCode)) {
          logger.info("connect successfu...ftp服务器:" + this.hostname + ":" + this.port);
          return;
        }
      }

      logger.info("connect failed...ftp服务器:" + this.hostname + ":" + this.port);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 上传所有文件
   * 
   * @param pathname
   *          ftp服务保存地址
   * @param originfilename
   *          待上传文件或目录的名称（绝对地址） *
   * @return
   */
  public boolean uploadAllFile(String pathname, String originfilename) {
    boolean flag = false;
    InputStream inputStream = null;
    try {
      logger.info("开始上传文件");
      File file = new File(originfilename);
      if (file.exists()) {
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
        if (file.isDirectory()) {
          List<String> fileNameList = FileUtills.getAllFileNames(originfilename);
          for (String uploadFileName : fileNameList) {
            inputStream = new FileInputStream(new File(originfilename + File.separator + uploadFileName));
            logger.info("正在上传文件：" + originfilename + File.separator + uploadFileName);
            if (ftpClient.storeFile(uploadFileName, inputStream)) {
              logger.info("上传文件成功:" + originfilename + File.separator + uploadFileName);
              inputStream.close();
            } else {
              logger.info("上传文件失败:" + originfilename + File.separator + uploadFileName);
            }
          }
        } else {
          inputStream = new FileInputStream(new File(originfilename));
          String[] str = originfilename.split(File.separator);
          logger.info("正在上传文件：" + originfilename);
          if (ftpClient.storeFile(str[str.length - 1], inputStream)) {
            logger.info("上传文件成功:" + originfilename);
            inputStream.close();
          } else {
            logger.info("上传文件失败:" + originfilename);
          }
        }
        flag = true;
      }
    } catch (Exception e) {
      logger.info("上传文件失败");
      e.printStackTrace();
      return flag;
    } finally {
      if (null != inputStream) {
        try {
          inputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return flag;
  }

  /**
   * 上传文件
   * 
   * @param pathname
   *          ftp服务保存地址
   * @param fileName
   *          上传到ftp的文件名
   * @param inputStream
   *          输入文件流
   * @return
   */
  public boolean uploadFile(String pathname, String fileName, InputStream inputStream) {
    boolean flag = false;
    try {
      logger.info("开始上传文件");
      ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
      createDirecroty(pathname);
      ftpClient.makeDirectory(pathname);
      ftpClient.changeWorkingDirectory(pathname);
      if (ftpClient.storeFile(fileName, inputStream)) {
        inputStream.close();
      }
      flag = true;
      logger.info("上传文件成功");
    } catch (Exception e) {
      logger.info("上传文件失败");
      e.printStackTrace();
      return flag;
    } finally {
      if (null != inputStream) {
        try {
          inputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return flag;
  }

  // 改变目录路径
  public boolean changeWorkingDirectory(String directory) {
    boolean flag = true;
    try {
      flag = ftpClient.changeWorkingDirectory(directory);
      if (flag) {
        logger.info("进入文件夹" + directory + " 成功！");
      } else {
        logger.info("进入文件夹" + directory + " 失败！开始创建文件夹");
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    return flag;
  }

  // 创建多层目录文件，如果有ftp服务器已存在该文件，则不创建，如果无，则创建
  public boolean createDirecroty(String remote) throws IOException {
    boolean success = true;
    String directory = remote + "/";
    // 如果远程目录不存在，则递归创建远程服务器目录
    if (!directory.equalsIgnoreCase("/") && !changeWorkingDirectory(new String(directory))) {
      int start = 0;
      int end = 0;
      if (directory.startsWith("/")) {
        start = 1;
      } else {
        start = 0;
      }
      end = directory.indexOf("/", start);
      String path = "";
      String paths = "";
      while (true) {
        String subDirectory = new String(remote.substring(start, end).getBytes("GBK"), "iso-8859-1");
        path = path + "/" + subDirectory;
        if (!existFile(path)) {
          if (makeDirectory(subDirectory)) {
            changeWorkingDirectory(subDirectory);
          } else {
            logger.info("创建目录[" + subDirectory + "]失败");
            changeWorkingDirectory(subDirectory);
          }
        } else {
          changeWorkingDirectory(subDirectory);
        }
        paths = paths + "/" + subDirectory;
        start = end + 1;
        end = directory.indexOf("/", start);
        // 检查所有目录是否创建完毕
        if (end <= start) {
          break;
        }
      }
    }
    return success;
  }

  // 判断ftp服务器文件是否存在
  public boolean existFile(String path) throws IOException {
    boolean flag = false;
    FTPFile[] ftpFileArr = ftpClient.listFiles(path);
    if (ftpFileArr.length > 0) {
      flag = true;
    }
    return flag;
  }

  // 创建目录
  public boolean makeDirectory(String dir) {
    boolean flag = true;
    try {
      flag = ftpClient.makeDirectory(dir);
      if (flag) {
        logger.info("创建文件夹" + dir + " 成功！");
      } else {
        logger.info("创建文件夹" + dir + " 失败！");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return flag;
  }

  /**
   * * 下载文件 *
   * 
   * @param pathname
   *          FTP服务器文件目录 *
   * @param filename
   *          文件名称 *
   * @param localpath
   *          下载后的文件路径 *
   * @return
   */
  public boolean downloadFile(String pathname, String filename, String localpath) {
    boolean flag = false;
    OutputStream os = null;
    try {
      logger.info("开始下载文件");
      // 切换FTP目录
      ftpClient.changeWorkingDirectory(pathname);
      FTPFile[] ftpFiles = ftpClient.listFiles();
      for (FTPFile file : ftpFiles) {
        if (filename.equalsIgnoreCase(file.getName())) {
          File localFile = new File(localpath + "/" + file.getName());
          os = new FileOutputStream(localFile);
          ftpClient.retrieveFile(file.getName(), os);
          os.close();
        }
      }
      flag = true;
      logger.info("下载文件成功");
    } catch (Exception e) {
      logger.info("下载文件失败");
      e.printStackTrace();
    } finally {
      if (null != os) {
        try {
          os.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return flag;
  }

  /**
   * * 删除文件 *
   * 
   * @param pathname
   *          FTP服务器保存目录 *
   * @param filename
   *          要删除的文件名称 *
   * @return
   */
  public boolean deleteFile(String pathname, String filename) {
    boolean flag = false;
    try {
      logger.info("开始删除文件");
      // 切换FTP目录
      ftpClient.changeWorkingDirectory(pathname);
      ftpClient.dele(filename);
      flag = true;
      logger.info("删除文件成功");
    } catch (Exception e) {
      logger.info("删除文件失败");
      e.printStackTrace();
    }
    return flag;
  }

  /**
   * 删除全部文件
   */
  public void deleteAllFile() {
    try {
      ftpClient.changeToParentDirectory();

      ftpClient.enterLocalPassiveMode();
      String[] fTPFileStrs = ftpClient.listNames();
      if (fTPFileStrs == null || fTPFileStrs.length == 0) {
        return;
      }
      for (String fTPFile : fTPFileStrs) {
        logger.info("=== >> delete file Name : " + fTPFile);
        if (ftpClient.deleteFile(fTPFile)) {
          logger.info("删除文件成功 :" + fTPFile);
        }
      }

    } catch (Exception e) {
      logger.info("删除文件失败");
      e.printStackTrace();
    }
  }

  /**
   * 重命名远程FTP文件
   */
  public void renameFile(String beforeSuffixName, String afterSuffixName) {
    try {

      ftpClient.changeToParentDirectory();

      ftpClient.enterLocalPassiveMode();
      String[] ftpFileStrs = ftpClient.listNames();
      if (ftpFileStrs == null || ftpFileStrs.length == 0) {
        logger.info("== >> renameFile is empty");
        return;
      }
      for (String fileName : ftpFileStrs) {
        String beforeFileName = fileName;
        logger.info("== >> renameFile:" + beforeFileName);
        if (beforeSuffixName.equalsIgnoreCase(beforeFileName.split("\\.")[1])) {
          if (ftpClient.rename(beforeFileName, beforeFileName.split("\\.")[0] + "." + afterSuffixName)) {
            logger.info("=== >> rename " + beforeFileName + " success!");
            ftpClient.dele(beforeFileName);
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public void close() {
    try {
      if (ftpClient != null) {
        if (ftpClient.isConnected()) {
          ftpClient.logout();
          ftpClient.disconnect();
          ftpClient = null;
          logger.info("ftp服务器:" + this.hostname + ":" + this.port + "已断开");
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public static void main(String[] args) {
    FtpUtils ftp = new FtpUtils();
    // ftp.uploadFile("ftpFile/data", "123.docx", "E://123.docx");
    // ftp.downloadFile("ftpFile/data", "123.docx", "F://");
    ftp.deleteFile("ftpFile/data", "123.docx");
    System.err.println("ok");
  }
}
