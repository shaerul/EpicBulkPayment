/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paymenteditor;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author ShaerulH
 */
public class FileChooser {

    synchronized public File GetFile() {

        File selectedFile = null;

        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        FileFilter filt = new FileNameExtensionFilter("CSV File", "*csv",".csv","csv");
        jfc.setFileFilter(filt);
        jfc.addChoosableFileFilter(filt);
        jfc.setDialogTitle("Select Payment CSV file to Open");
        
        String[] filterExtensions = new String[]{"csv"};
        String filterPath = "E:\\";
        
               
        int returnValue = jfc.showOpenDialog(null);
        // int returnValue = jfc.showSaveDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = jfc.getSelectedFile();
            System.out.println(selectedFile.getAbsolutePath());
        }

        return selectedFile;

    }

}
