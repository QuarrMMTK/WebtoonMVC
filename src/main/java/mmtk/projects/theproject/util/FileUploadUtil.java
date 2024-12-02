package mmtk.projects.theproject.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Utility class for handling file uploads.
 * Author: Min Myat Thu Kha
 * Created At: 29/11/2024
 * Project: WebtoonMVC
 */
public class FileUploadUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadUtil.class);

    /**
     * Saves the uploaded file to the specified directory.
     *
     * @param uploadDir The directory where the file should be saved.
     * @param fileName The name of the file to be saved.
     * @param file The file to be uploaded.
     * @throws IOException If an error occurs while saving the file.
     */
    public static void saveFile(String uploadDir, String fileName, MultipartFile file) throws IOException {
        // Sanitize the file name to avoid malicious characters
        fileName = sanitizeFileName(fileName);

        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        Files.createDirectories(uploadPath);

        // Resolve the full path for the file to be saved
        Path filePath = uploadPath.resolve(fileName);

        // Log file path and name
        logger.info("Saving file to: {}", filePath);

        try (InputStream in = file.getInputStream()) {
            // Copy the file content to the destination path
            Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
            logger.info("File saved successfully: {}", filePath);
        } catch (IOException e) {
            // Log and throw an IOException with context
            logger.error("Could not save file: {} to directory: {}", fileName, uploadDir, e);
            throw new IOException("Could not save image file " + fileName, e);
        }
    }

    /**
     * Sanitizes the file name by removing invalid characters.
     * This helps prevent directory traversal and ensures safe file names.
     *
     * @param fileName The original file name.
     * @return The sanitized file name.
     */
    public static String sanitizeFileName(String fileName) {
        // Remove any characters that could cause issues with the file system
        // For example, remove any non-alphanumeric characters (except hyphen and period)
        return fileName.replaceAll("[^a-zA-Z0-9.-]", "_");
    }
}
