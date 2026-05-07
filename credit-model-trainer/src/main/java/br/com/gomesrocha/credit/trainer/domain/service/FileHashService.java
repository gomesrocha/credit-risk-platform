package br.com.gomesrocha.credit.trainer.domain.service;

import jakarta.enterprise.context.ApplicationScoped;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;

@ApplicationScoped
public class FileHashService {

    public String sha256(Path path) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            try (InputStream inputStream = Files.newInputStream(path)) {
                byte[] buffer = new byte[8192];
                int read;

                while ((read = inputStream.read(buffer)) != -1) {
                    digest.update(buffer, 0, read);
                }
            }

            byte[] hash = digest.digest();
            StringBuilder result = new StringBuilder();

            for (byte b : hash) {
                result.append(String.format("%02x", b));
            }

            return "sha256:" + result;

        } catch (Exception e) {
            throw new IllegalStateException("Erro ao calcular hash SHA-256 do arquivo: " + path, e);
        }
    }
}