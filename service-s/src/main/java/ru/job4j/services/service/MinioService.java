package ru.job4j.services.service;

import io.minio.*;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class MinioService {

    private static final Logger log = LoggerFactory.getLogger(MinioService.class);

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @PostConstruct
    public void init() {
        initBucket();
        uploadPlaceholder("photos/ivanov.jpg", "Ivanov");
        uploadPlaceholder("photos/petrova.jpg", "Petrova");
    }

    public void initBucket() {
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
                log.info("Bucket '{}' created", bucket);
            }
        } catch (Exception e) {
            log.error("Failed to initialize bucket '{}'", bucket, e);
        }
    }

    public byte[] getPhoto(String objectName) {
        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder().bucket(bucket).object(objectName).build())) {
            return stream.readAllBytes();
        } catch (Exception e) {
            log.error("Failed to get object '{}' from bucket '{}'", objectName, bucket, e);
            throw new RuntimeException("Failed to get photo: " + objectName, e);
        }
    }

    public void uploadPhoto(String objectName, InputStream stream, String contentType) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .stream(stream, -1, 10 * 1024 * 1024)
                    .contentType(contentType)
                    .build());
            log.info("Uploaded '{}' to bucket '{}'", objectName, bucket);
        } catch (Exception e) {
            log.error("Failed to upload object '{}' to bucket '{}'", objectName, bucket, e);
            throw new RuntimeException("Failed to upload photo: " + objectName, e);
        }
    }

    private void uploadPlaceholder(String objectName, String name) {
        try {
            minioClient.statObject(StatObjectArgs.builder().bucket(bucket).object(objectName).build());
            log.info("Object '{}' already exists, skipping", objectName);
        } catch (Exception e) {
            byte[] png = generatePlaceholderPng(name);
            uploadPhoto(objectName, new ByteArrayInputStream(png), "image/jpeg");
            log.info("Uploaded placeholder for '{}'", objectName);
        }
    }

    private byte[] generatePlaceholderPng(String name) {
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(new Color(100, 149, 237));
        g.fillRect(0, 0, 200, 200);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        FontMetrics fm = g.getFontMetrics();
        int x = (200 - fm.stringWidth(name)) / 2;
        int y = (200 - fm.getHeight()) / 2 + fm.getAscent();
        g.drawString(name, x, y);
        g.dispose();
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", out);
            return out.toByteArray();
        } catch (IOException ex) {
            throw new RuntimeException("Failed to generate placeholder image", ex);
        }
    }
}
