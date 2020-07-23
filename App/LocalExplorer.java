/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App;

import java.io.File;
import java.util.Arrays;
import ListPackage.List;

/**
 *
 * @author mth
 */
public class LocalExplorer {

    private String currentPath = new String();

    public List listLocalDrives() {
//        System.out.println("f.listRoots(): " + Arrays.toString(File.listRoots()));

        List driveLetter = new List();
        File[] rootDrive;

        rootDrive = File.listRoots();

        for (int i = 0; i < rootDrive.length; i++) {
//            System.out.println("Adding: " + rootDrive[i].toString());
            driveLetter.add(rootDrive[i].toString());
        }
        return driveLetter;
    }

    public String[] localFileList(String path) {
        File f = new File(path);
        String[] itemNames;
//        System.out.println("f.getAbsolutePath(): " + f.getAbsolutePath());
//        System.out.println("f.listRoots(): " + Arrays.toString(f.listRoots()));
        currentPath = f.getAbsoluteFile().toString();
        itemNames = f.list();

        return itemNames;
    }

    public String getParentFolder(String path) {
        String s = new String();
        int i = path.length() - 1;
        System.out.println("Recu path: " + path);
        if (path.charAt(i) != '\\') {
            return getParentFolder(path.substring(0, i));
        } else {
            s = path;
            System.out.println("Recu s: " + s);
            return s;
        }
    }

    public String getCurrentPath() {
        return currentPath;
    }

    public void removeLocalFile(List l, String localPath) {
        for (int i = 1; i <= l.size(); i++) {
            File file = new File(localPath + "\\" + l.get(i));
            if (file.delete()) {
                System.out.println("File deleted successfully");
            } else {
                System.out.println("Failed to delete the file");
            }
        }
    }

}
