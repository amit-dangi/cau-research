//@ AUTHOR amit dangi 25 Oct file for create the zip folder of given path. 
package com.sits.common;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

    private List <String> fileList;
    public ZipUtils() {
        fileList = new ArrayList < String > ();
    }

    public void zipIt(String zipFile,String SOURCE_FOLDER) {
        byte[] buffer = new byte[1024];
        String source = new File(SOURCE_FOLDER).getName();
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(fos);

            System.out.println("Output to Zip : " + zipFile);
            FileInputStream in = null;

            for (String file: this.fileList) {
                System.out.println("File Added : " + file);
                ZipEntry ze = new ZipEntry(source + File.separator + file);
                zos.putNextEntry(ze);
                try {
                    in = new FileInputStream(SOURCE_FOLDER + File.separator + file);
                    int len;
                    while ((len = in .read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                } finally {
                    in.close();
                }
            }

            zos.closeEntry();
            //System.out.println("Folder successfully compressed !");

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                zos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void generateFileList(File node, String SOURCE_FOLDER) {
        // add file only
        if (node.isFile()) {
            fileList.add(generateZipEntry(node.toString(),SOURCE_FOLDER));
        }

        if (node.isDirectory()) {
            String[] subNote = node.list();
            for (String filename: subNote) {
                generateFileList(new File(node, filename),SOURCE_FOLDER);
            }
        }
    }

    private String generateZipEntry(String file,String SOURCE_FOLDER) {
        return file.substring(SOURCE_FOLDER.length() + 1, file.length());
    }
    
    
   public void deleteSubfolders(File folder) {
        if (folder.isDirectory()) {
        	//System.out.println("Deleting the Folder from "+folder);
            for (File file : folder.listFiles()) {
                if (file.isDirectory()) {
                    deleteFolder(file);
                }
            }
            //System.out.println("Delete successfully! ");
        }
    }

   public void deleteFolder(File folder) {
	   //System.out.println("Deleting the Folder from "+folder);
        if (folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                deleteFolder(file);
            }
        }
        folder.delete();
        //System.out.println("Folder Delete successfully! ");
    }
    
}