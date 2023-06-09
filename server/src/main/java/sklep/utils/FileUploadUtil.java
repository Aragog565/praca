package sklep.utils;
import java.io.*;
import java.nio.file.*;
import java.util.Base64;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.aspectj.util.FileUtil;
import org.springframework.web.multipart.MultipartFile;
import sklep.service.UserService;
import sklep.service.exception.BadRequestException;

public class FileUploadUtil {

    public static void saveFile(String uploadDir, String fileName,
                                MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }

    public static String readFile(String uploadDir, String fileName){
        File file = new File(uploadDir+fileName);
        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            return null;
        }
    }

    public static String createFile(String dir, MultipartFile file, boolean required){
        if(file == null || file.isEmpty()){
            if(required){
                throw new BadRequestException("podaj zdjęcie");
            }
            return null;
        }
        String name = String.format("%s.%s", RandomStringUtils.randomAlphanumeric(8), "jpg");
        try {
            FileUploadUtil.saveFile(dir, name, file);
        } catch (IOException e) {
            return null;
        }

        return name;
    }
    public static String updateFile(String dir, MultipartFile file, String oldFileName){
        String newName = createFile(dir,file, false);
        if(newName == null){
            return oldFileName;
        }
        if(oldFileName!=null) {
            File fileToDelete = new File(dir + oldFileName);
            fileToDelete.delete();
        }

        return newName;
    }
}
