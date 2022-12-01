package uz.ulugbek.awsdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.ulugbek.awsdemo.entity.ImageMetadata;

public interface ImageMetadataRepository extends JpaRepository<ImageMetadata, Long> {
}
