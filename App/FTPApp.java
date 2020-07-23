/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App;

import java.io.IOException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import UIPack.FTPUI;

/**
 *
 * @author mth
 */
public class FTPApp {

    boolean exit;

    public static void main(String[] args) {

        
        String serverIP = "192.168.1.14";
        int serverPort = 2221;

        String user = "anonymous";
        String password = "";

        FTPConnect ftp = new FTPConnect();
        FTPUI ui = new FTPUI(ftp);
        ui.setVisible(true);

        }
}
