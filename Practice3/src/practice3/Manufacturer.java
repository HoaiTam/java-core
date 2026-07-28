package practice3;

import java.util.Locale;

public enum Manufacturer {
    HONDA("Honda"),
    YAMAHA("Yamaha"),
    TOYOTA("Toyota"),
    SUZUKI("Suzuki");

    private final String displayName;

    Manufacturer(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Manufacturer from(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Hãng sản xuất không được null.");
        }
        try {
            return Manufacturer.valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    "Hãng sản xuất chỉ gồm Honda, Yamaha, Toyota hoặc Suzuki.");
        }
    }
}
