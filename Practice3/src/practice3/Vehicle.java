package practice3;

import java.time.Year;
import java.util.Locale;
import java.util.Objects;

public abstract class Vehicle {
    private final String vehicleNumber;
    private final Manufacturer manufacturer;
    private final int manufactureYear;
    private final String color;
    private final Owner owner;

    protected Vehicle(
            String vehicleNumber,
            Manufacturer manufacturer,
            int manufactureYear,
            String color,
            Owner owner) {
        String normalizedNumber = requireText(vehicleNumber, "Biển số xe")
                .toUpperCase(Locale.ROOT);
        if (normalizedNumber.length() != 5) {
            throw new IllegalArgumentException("Biển số xe phải có đúng 5 ký tự.");
        }
        int currentYear = Year.now().getValue();
        if (manufactureYear <= 2000 || manufactureYear > currentYear) {
            throw new IllegalArgumentException(
                    "Năm sản xuất phải lớn hơn 2000 và không vượt quá năm hiện tại.");
        }

        this.vehicleNumber = normalizedNumber;
        this.manufacturer = Objects.requireNonNull(
                manufacturer, "Hãng sản xuất không được null.");
        this.manufactureYear = manufactureYear;
        this.color = requireText(color, "Màu xe");
        this.owner = Objects.requireNonNull(owner, "Chủ phương tiện không được null.");
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public int getManufactureYear() {
        return manufactureYear;
    }

    public String getColor() {
        return color;
    }

    public Owner getOwner() {
        return owner;
    }

    public abstract VehicleType getVehicleType();

    protected String baseInformation() {
        return "vehicleNumber='%s', manufacturer='%s', manufactureYear=%d, color='%s', owner=%s"
                .formatted(
                        vehicleNumber,
                        manufacturer.getDisplayName(),
                        manufactureYear,
                        color,
                        owner);
    }

    protected static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " không được để trống.");
        }
        return value.trim();
    }
}
