/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import ListPackage.List;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.util.Arrays;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPListParseEngine;

/**
 *
 * @author mth
 */
public class FTPConnect {

    FTPClient ftp = new FTPClient();
    boolean exit = false;
    LocalExplorer le;
    List fileList;
    public List<String> UIFileList;

    public FTPConnect() {
        fileList = new List();
        UIFileList = new List();
        le = new LocalExplorer();
    }

    public void connect(String ip, int port, String user, String password) {
        try {
            ftp.connect(ip, port);
            showServerReply(ftp);
            int replyCode = ftp.getReplyCode();
            System.out.println("RESULT CONTACT: " + showServerReply(ftp));
            ftp.enterLocalPassiveMode();
            ftp.login(user, password);
            showServerReply(ftp);
            replyCode = ftp.getReplyCode();
            System.out.println("RESULT CONNECT: " + showServerReply(ftp));

        } catch (IOException e) {
            System.out.println("Error MTH: " + e);
        }
    }

    private static String showServerReply(FTPClient ftp) {
        String[] replies = ftp.getReplyStrings();
        String s = "Server: ";
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                s += aReply;
                //System.out.println("SERVER: " + aReply);
            }
        }
        return s;
    }

    public List listRemoteDir(String path) {
        try {
            List fileName = new List();
            String[] names = ftp.listNames(path);
            for (String s : names) {
//                System.out.println("adding FTP: " + s);
                fileName.add(s);
            }

            return fileName;
        } catch (IOException ex) {
            Logger.getLogger(FTPConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void listDir() throws IOException {
        try {
            FTPFile[] dir = ftp.listFiles("\\");
            for (FTPFile file : dir) {
                fileList.add(file);
            }

            String[] s = ftp.listNames("\\");
            for (String file : s) {
                UIFileList.add(file);
            }

        } catch (IOException ex) {
            Logger.getLogger(FTPConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
        UIFileList.printList();
        fileList.printList();
        System.out.println(ftp.printWorkingDirectory());

//        FTPListParseEngine engine = ftp.initiateListParsing(ftp.printWorkingDirectory());
//        for (int i = 0; i <= engine.getFiles().length; i++) {
//            System.out.println("ENGINE TEST: " + Arrays.toString(engine.getNext(i)));
//        }
    }

    public String getParentFolder(String path) {
        String s = new String();
        int i = path.length() - 1;
        System.out.println("RecuFTP path: " + path);
        if (path.charAt(i) != '\\') {
            return getParentFolder(path.substring(0, i));
        } else {
            s = path;
            System.out.println("RecuFTP s: " + s.substring(0, s.length() - 1));
            return s.substring(0, s.length() - 1);
        }
    }

    public void transferLocalToRemote(List l, String localPath, String remotePath, boolean eraseSource) {
//        for (int i = 1; i <= l.size(); i++) {
//            System.out.println("Review List: " + l.get(i));
//        }
//        System.out.println("---------");
//        l.printList();

        try {
            ftp.setFileType(FTP.BINARY_FILE_TYPE);

            for (int i = 1; i <= l.size(); i++) {

                File localFile = new File(localPath + "\\" + l.get(i));

                System.out.println("localFile to Transfer: " + localFile.toString());

                if (localFile.isDirectory()) {
                    System.out.println(localFile.toString() + " is a Directory");
                    ftp.makeDirectory(remotePath + "\\" + l.get(i));
                    String newRemotePath = remotePath + "\\" + l.get(i);
                    List dirContent = new List();
                    for (String s : localFile.list()) {
                        dirContent.add(s);
                    }
                    System.out.println("---------------Print dirContent");
                    dirContent.printList();
                    System.out.println("-------------------");

                    transferLocalToRemote(dirContent, localFile.toString(), newRemotePath, eraseSource);
                } else if (localFile.isFile()) {
                    String firstRemoteFile = remotePath + "\\" + l.get(i);
                    InputStream inputStream = new FileInputStream(localFile);
                    boolean done = ftp.storeFile(firstRemoteFile, inputStream);
                    inputStream.close();
                    if (done) {
                        System.out.println("The first file is uploaded successfully.");
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(FTPConnect.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (eraseSource) {
            le.removeLocalFile(l, localPath);
        }
    }

    public void removeRemoteFile(List l, String remotePath) {
//        for (int i = 1; i <= l.size(); i++) {
//            System.out.println("Review List: " + l.get(i));
//        }
//        System.out.println("---------");
//        l.printList();

        for (int i = 1; i <= l.size(); i++) {
            try {
                String s = remotePath + "\\" + l.get(i);
                ftp.deleteFile(s);

            } catch (IOException ex) {
                Logger.getLogger(FTPConnect.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void transferRemoteToLocal(List l, String localPath, String remotePath, boolean eraseSource) throws IOException {
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        for (int i = 1; i <= l.size(); i++) {
            OutputStream outputStream = null;

            String remoteFile = remotePath + "\\" + l.get(i);
            String[] fileInsideRemoteDir = ftp.listNames(remoteFile);
            FTPFile[] fileInsideRemoteDirFileObject = ftp.listFiles(remoteFile);

            
            
            System.out.println("Remote file listFiles: " + Arrays.toString(fileInsideRemoteDir)+" - Array length: " + fileInsideRemoteDir.length);
            
            if (fileInsideRemoteDir.length > 0) { //target is a directory
                List l1 = new List();
                String folderToCreate = localPath + "\\" + l.get(i);
                File folderToCreateFileObject = new File(folderToCreate);
                System.out.println("Creating folder: " + folderToCreate);

                if (!folderToCreateFileObject.exists()) {
                    new File(folderToCreate).mkdir();
                }
                for (String f : fileInsideRemoteDir) {
                    l1.add(f);
                }
                transferRemoteToLocal(l1, folderToCreate, remoteFile, eraseSource);
            } else { //target is a file

                String destination = localPath + "\\" + l.get(i);
                System.out.println("remoteFile: " + remoteFile + " - destination: " + destination);
                File downloadFile1 = new File(destination);
                outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile1));
                boolean success = ftp.retrieveFile(remoteFile, outputStream);
                outputStream.close();
            }
        }

        if (eraseSource) {
            removeRemoteFile(l, remotePath);
        }
    }

    /**
     *
     * @return
     */
    public List<String> getUINameList() {

        List<String> l = new List();

        return l;
    }

    public void setExit(boolean e) {
        System.out.println("EXIT is " + exit);
        this.exit = e;
        System.out.println("SET EXIT to " + exit);

    }

    public void closeConnection() {
        try {
            ftp.disconnect();
        } catch (IOException ex) {
            Logger.getLogger(FTPConnect.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
}
