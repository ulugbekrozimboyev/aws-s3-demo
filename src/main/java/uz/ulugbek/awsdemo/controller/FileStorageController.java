package uz.ulugbek.awsdemo.controller;

import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.ulugbek.awsdemo.service.FileStorageService;

@RestController
@RequestMapping("/api/file")
@AllArgsConstructor
public class FileStorageController {

    private FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity uploadFile(@RequestParam(value = "file")  MultipartFile file) {
        String fileName = fileStorageService.uploadFile(file);

        return ResponseEntity.ok(fileName);
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

    @DeleteMapping("/delete/{filename}")
    public ResponseEntity deleteFile(@PathVariable String filename) {
        fileStorageService.deleteFile(filename);

        return ResponseEntity.ok().build();
    }

}
