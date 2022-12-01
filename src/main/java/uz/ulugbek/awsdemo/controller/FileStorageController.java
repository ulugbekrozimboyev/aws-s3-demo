package uz.ulugbek.awsdemo.controller;

import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.ulugbek.awsdemo.entity.ImageMetadata;
import uz.ulugbek.awsdemo.service.FileStorageService;
import uz.ulugbek.awsdemo.service.ImageMetadataService;

@RestController
@RequestMapping("/api/file")
@AllArgsConstructor
public class FileStorageController {

    private FileStorageService fileStorageService;
    private ImageMetadataService imageMetadataService;

    @GetMapping
    public ResponseEntity getAllImages(Pageable pageable) {
        Page<ImageMetadata> page = imageMetadataService.findAll(pageable);
        return ResponseEntity.ok().body(page.getContent());
    }

    @PostMapping("/upload")
    public ResponseEntity uploadFile(@RequestParam(value = "file")  MultipartFile file) {
        ImageMetadata imageMetadata = fileStorageService.uploadFile(file);

        return ResponseEntity.ok(imageMetadata);
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String filename) {
        byte[] fileContent = fileStorageService.downloadFile(filename);

        ByteArrayResource resource = new ByteArrayResource(fileContent);
        return ResponseEntity.ok()
                .contentLength(fileContent.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment: filename=\"" + filename + "\"")
                .body(resource);
    }

    @DeleteMapping("/delete-by-name/{filename}")
    public ResponseEntity deleteFile(@PathVariable String filename) {
        fileStorageService.deleteFile(filename);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteFile(@PathVariable Long id) throws Exception {
        fileStorageService.deleteFileById(id);

        return ResponseEntity.ok().body(true);
    }

}
