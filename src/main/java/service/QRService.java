package service;

public class QRService {
    public String extractFaydaId(String qrText) {
        if (qrText == null || qrText.isBlank()) {
            throw new IllegalArgumentException("QR content is empty.");
        }
        String trimmed = qrText.trim();
        if (trimmed.contains("faydaId=")) {
            return trimmed.substring(trimmed.indexOf("faydaId=") + 8).split("[&\\s]")[0];
        }
        return trimmed;
    }
}
