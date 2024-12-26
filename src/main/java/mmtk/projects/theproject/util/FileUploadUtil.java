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
     * Saves the uploaded file to the specified directory and returns the saved file path.
     *
     * @param uploadDir The directory where the file should be saved.
     * @param fileName  The name of the file to be saved.
     * @param file      The file to be uploaded.
     * @return The path of the saved file as a string.
     * @throws IOException If an error occurs while saving the file.
     */
    public static String saveFile(String uploadDir, String fileName, MultipartFile file) throws IOException {
        // Check if the file is empty
        if (file.isEmpty()) {
            logger.warn("Attempted to save an empty file: {}", fileName);
            throw new IOException("Cannot save empty file: " + fileName);
        }

        // Create the upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            logger.info("Created upload directory: {}", uploadPath);
        }

        // Resolve the full path for the file to be saved
        Path filePath = uploadPath.resolve(fileName);

        // Log details about the file
        logger.info("Saving file: {} (Size: {} bytes) to directory: {}", fileName, file.getSize(), uploadDir);

        try (InputStream in = file.getInputStream()) {
            // Copy the file content to the destination path
            Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
            logger.info("File saved successfully at: {}", filePath);
            return filePath.toString();
        } catch (IOException e) {
            logger.error("Failed to save file: {} to directory: {}", fileName, uploadDir, e);
            throw new IOException("Could not save file: " + fileName + " to directory: " + uploadDir, e);
        }
    }
}
