package com.duocuc.inscripciones.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class S3Service {

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.s3.access-key}")
    private String accessKey;

    @Value("${aws.s3.secret-key}")
    private String secretKey;

    @Value("${aws.s3.session-token:}")
    private String sessionToken;

    private S3Client s3Client;
    private boolean configuracionValida = false;

    @PostConstruct
    public void init() {
        if (isPlaceholder(bucketName) || isPlaceholder(region) || isPlaceholder(accessKey) || isPlaceholder(secretKey)) {
            log.warn("AWS S3 no esta configurado. Las credenciales son placeholders. El servicio S3 no estara disponible.");
            return;
        }

        try {
            AwsCredentialsProvider credentialsProvider;
            if (sessionToken != null && !sessionToken.isBlank() && !isPlaceholder(sessionToken)) {
                credentialsProvider = StaticCredentialsProvider.create(
                    AwsSessionCredentials.create(accessKey, secretKey, sessionToken)
                );
            } else {
                credentialsProvider = StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKey, secretKey)
                );
            }

            this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(credentialsProvider)
                .build();
            this.configuracionValida = true;
            log.info("AWS S3 configurado correctamente en region {} y bucket {}", region, bucketName);
        } catch (Exception e) {
            log.error("Error al configurar AWS S3: {}", e.getMessage());
        }
    }

    private boolean isPlaceholder(String value) {
        return value == null || value.isBlank() || value.startsWith("TU_");
    }

    private void validarConfiguracion() {
        if (!configuracionValida) {
            throw new RuntimeException("AWS S3 no esta configurado. Completa las credenciales en application.properties.");
        }
    }

    public String subirArchivo(MultipartFile archivo, String carpeta) {
        validarConfiguracion();
        try {
            String nombreOriginal = archivo.getOriginalFilename();
            String extension = "";
            if (nombreOriginal != null && nombreOriginal.contains(".")) {
                extension = nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
            }
            String key = carpeta + "/" + UUID.randomUUID() + extension;

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(archivo.getContentType())
                .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(archivo.getInputStream(), archivo.getSize()));

            log.info("Archivo subido a S3: {}/{}", bucketName, key);
            return key;
        } catch (IOException e) {
            log.error("Error al subir archivo a S3", e);
            throw new RuntimeException("Error al subir archivo a S3: " + e.getMessage());
        }
    }

    public byte[] descargarArchivo(String key) {
        validarConfiguracion();
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

            ResponseInputStream<GetObjectResponse> response = s3Client.getObject(getObjectRequest);
            return response.readAllBytes();
        } catch (IOException e) {
            log.error("Error al descargar archivo de S3", e);
            throw new RuntimeException("Error al descargar archivo de S3: " + e.getMessage());
        }
    }

    public void eliminarArchivo(String key) {
        validarConfiguracion();
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build();

        s3Client.deleteObject(deleteObjectRequest);
        log.info("Archivo eliminado de S3: {}/{}", bucketName, key);
    }

    public String generarUrlPublica(String key) {
        validarConfiguracion();
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, key);
    }
}
