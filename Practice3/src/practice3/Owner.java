package practice3;

import java.util.Objects;
import java.util.regex.Pattern;

public final class Owner {
    private static final Pattern ID_PATTERN = Pattern.compile("\\d{12}");
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private final String idNumber;
    private final String fullName;
    private final String email;

    public Owner(String idNumber, String fullName, String email) {
        String normalizedId = requireText(idNumber, "Số CMND/CCCD");
        if (!ID_PATTERN.matcher(normalizedId).matches()) {
            throw new IllegalArgumentException("Số CMND/CCCD phải có đúng 12 chữ số.");
        }

        String normalizedEmail = requireText(email, "Email");
        if (!EMAIL_PATTERN.matcher(normalizedEmail).matches()) {
            throw new IllegalArgumentException("Email không đúng định dạng.");
        }

        this.idNumber = normalizedId;
        this.fullName = requireText(fullName, "Họ và tên");
        this.email = normalizedEmail;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " không được để trống.");
        }
        return value.trim();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Owner owner)) {
            return false;
        }
        return idNumber.equals(owner.idNumber)
                && fullName.equals(owner.fullName)
                && email.equalsIgnoreCase(owner.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idNumber, fullName, email.toLowerCase());
    }

    @Override
    public String toString() {
        return "Owner{idNumber='%s', fullName='%s', email='%s'}"
                .formatted(idNumber, fullName, email);
    }
}
