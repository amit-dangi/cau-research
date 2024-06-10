package com.sits.common;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileCopyExample {
    public static void main(String[] args) {
        // Define the source and destination file paths
        Path sourcePath = Paths.get("D:/CAU-FILES/RSRCH/MOU/1/101_Instructor Fleetwise Report.pdf");
        Path destinationPath = Paths.get("D:/CAU-FILES/RSRCH/MOU/test/101_Instructor Fleetwise Report.pdf");

        try {
            // Use the Files.copy method to copy the file
            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File copied successfully.");
        } catch (IOException e) {
            System.err.println("Error copying the file: " + e.getMessage());
        }
    }
}

