package practice3;

public final class Motorbike extends Vehicle {
    private final double cylinderCapacity;

    public Motorbike(
            String vehicleNumber,
            Manufacturer manufacturer,
            int manufactureYear,
            String color,
            Owner owner,
            double cylinderCapacity) {
        super(vehicleNumber, manufacturer, manufactureYear, color, owner);
        if (!Double.isFinite(cylinderCapacity) || cylinderCapacity <= 0) {
            throw new IllegalArgumentException("Dung tích xi-lanh phải lớn hơn 0.");
        }
        this.cylinderCapacity = cylinderCapacity;
    }

    public double getCylinderCapacity() {
        return cylinderCapacity;
    }

    @Override
    public VehicleType getVehicleType() {
        return VehicleType.MOTORBIKE;
    }

    @Override
    public String toString() {
        return "Motorbike{%s, cylinderCapacity=%.2f cc}"
                .formatted(baseInformation(), cylinderCapacity);
    }
}
