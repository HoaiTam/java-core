package practice3;

public enum VehicleType {
    CAR("Ô tô"),
    MOTORBIKE("Xe máy"),
    TRUCK("Xe tải");

    private final String displayName;

    VehicleType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
