package uz.ulugbek.awsdemo.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uz.ulugbek.awsdemo.entity.ImageMetadata;
import uz.ulugbek.awsdemo.repository.ImageMetadataRepository;

@Slf4j
@Service
@AllArgsConstructor
public class ImageMetadataService {

    private ImageMetadataRepository imageMetadataRepository;

    public Page<ImageMetadata> findAll(Pageable pageable) {
        return imageMetadataRepository.findAll(pageable);
    }

    public ImageMetadata findById(Long id) throws Exception {
        return imageMetadataRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Request Not Found that include the ImageMetadata")
                );
    }

    public ImageMetadata save(ImageMetadata imageMetadata) {
        return imageMetadataRepository.save(imageMetadata);
    }

    public void delete(Long id) {
        imageMetadataRepository.deleteById(id);
    }
}
