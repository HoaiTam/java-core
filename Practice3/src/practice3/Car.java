package practice3;

public final class Car extends Vehicle {
    private final int numberOfSeats;
    private final String engineType;

    public Car(
            String vehicleNumber,
            Manufacturer manufacturer,
            int manufactureYear,
            String color,
            Owner owner,
            int numberOfSeats,
            String engineType) {
        super(vehicleNumber, manufacturer, manufactureYear, color, owner);
        if (numberOfSeats <= 0) {
            throw new IllegalArgumentException("Số chỗ ngồi phải lớn hơn 0.");
        }
        this.numberOfSeats = numberOfSeats;
        this.engineType = requireText(engineType, "Loại động cơ");
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public String getEngineType() {
        return engineType;
    }

    @Override
    public VehicleType getVehicleType() {
        return VehicleType.CAR;
    }

    @Override
    public String toString() {
        return "Car{%s, numberOfSeats=%d, engineType='%s'}"
                .formatted(baseInformation(), numberOfSeats, engineType);
    }
}
