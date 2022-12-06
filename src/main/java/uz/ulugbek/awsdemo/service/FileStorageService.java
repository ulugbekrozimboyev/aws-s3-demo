package uz.ulugbek.awsdemo.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.ulugbek.awsdemo.entity.ImageMetadata;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@Service
public class FileStorageService {

    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private ImageMetadataService imageMetadataService;

    @Autowired
    private ImageMetadataSQSPublisherService imageMetadataSQSPublisherService;

    public ImageMetadata uploadFile(MultipartFile file){
        // save temporary file
        File fileObj = convertMultipartToFile(file);

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        PutObjectResult result = amazonS3.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
        log.info(result.getVersionId());

        // delete temp file
        fileObj.delete();

        // save to DB
        ImageMetadata imageMetadata = ImageMetadata.builder()
                                            .name(fileName)
                                            .contentType(file.getContentType())
                                            .originalName(file.getOriginalFilename())
                                            .fileSize(file.getSize())
                                        .build();

        ImageMetadata dbImageMetadata = imageMetadataService.save(imageMetadata);
        imageMetadataSQSPublisherService.publish(imageMetadata);

        return dbImageMetadata;
    }

    @SneakyThrows
    public byte[] downloadFile(Long id) {
        ImageMetadata imageMetadata = imageMetadataService.findById(id);
        return downloadFile(imageMetadata.getName());
    }

    @SneakyThrows
    public byte[] downloadFile(String filename) {
        S3Object s3Object = amazonS3.getObject(bucketName, filename);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteFile(String filename) {
        amazonS3.deleteObject(bucketName, filename);
    }

    private File convertMultipartToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipart to file", e);
        }

        return convertedFile;
    }

    public void deleteFileById(Long id) throws Exception {
        ImageMetadata imageMetadata = imageMetadataService.findById(id);
        deleteFile(imageMetadata.getName());
        imageMetadataService.delete(id);
    }
}
