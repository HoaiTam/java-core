package practice3;

public final class Truck extends Vehicle {
    private final double tonnage;

    public Truck(
            String vehicleNumber,
            Manufacturer manufacturer,
            int manufactureYear,
            String color,
            Owner owner,
            double tonnage) {
        super(vehicleNumber, manufacturer, manufactureYear, color, owner);
        if (!Double.isFinite(tonnage) || tonnage <= 0) {
            throw new IllegalArgumentException("Trọng tải phải lớn hơn 0.");
        }
        this.tonnage = tonnage;
    }

    public double getTonnage() {
        return tonnage;
    }

    @Override
    public VehicleType getVehicleType() {
        return VehicleType.TRUCK;
    }

    @Override
    public String toString() {
        return "Truck{%s, tonnage=%.2f tons}".formatted(baseInformation(), tonnage);
    }
}
