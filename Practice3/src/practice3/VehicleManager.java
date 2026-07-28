package practice3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class VehicleManager {
    private final List<Vehicle> vehicles = new ArrayList<>();
    private final Map<String, Owner> ownersById = new HashMap<>();

    /**
     * @return true nếu thêm thành công; false nếu biển số xe đã tồn tại.
     */
    public boolean addVehicle(Vehicle vehicle) {
        Objects.requireNonNull(vehicle, "Phương tiện không được null.");
        if (searchByVehicleNumber(vehicle.getVehicleNumber()).isPresent()) {
            return false;
        }

        Owner owner = vehicle.getOwner();
        Owner registeredOwner = ownersById.get(owner.getIdNumber());
        if (registeredOwner != null && !registeredOwner.equals(owner)) {
            throw new IllegalArgumentException(
                    "Số CMND/CCCD đã thuộc về một hồ sơ chủ phương tiện khác.");
        }

        ownersById.putIfAbsent(owner.getIdNumber(), owner);
        vehicles.add(vehicle);
        return true;
    }

    public Optional<Vehicle> searchByVehicleNumber(String vehicleNumber) {
        if (vehicleNumber == null) {
            return Optional.empty();
        }
        String value = vehicleNumber.trim();
        return vehicles.stream()
                .filter(vehicle -> vehicle.getVehicleNumber().equalsIgnoreCase(value))
                .findFirst();
    }

    public List<Vehicle> findByOwnerId(String ownerId) {
        if (ownerId == null) {
            return List.of();
        }
        String value = ownerId.trim();
        return vehicles.stream()
                .filter(vehicle -> vehicle.getOwner().getIdNumber().equals(value))
                .toList();
    }

    public int deleteByManufacturer(Manufacturer manufacturer) {
        Objects.requireNonNull(manufacturer, "Hãng sản xuất không được null.");
        int originalSize = vehicles.size();
        vehicles.removeIf(vehicle -> vehicle.getManufacturer() == manufacturer);
        rebuildOwnerIndex();
        return originalSize - vehicles.size();
    }

    /**
     * Trả về tất cả hãng đứng đầu để xử lý đúng trường hợp đồng hạng.
     */
    public List<Manufacturer> findManufacturersWithMostVehicles() {
        if (vehicles.isEmpty()) {
            return List.of();
        }

        Map<Manufacturer, Integer> counts = new EnumMap<>(Manufacturer.class);
        for (Vehicle vehicle : vehicles) {
            counts.merge(vehicle.getManufacturer(), 1, Integer::sum);
        }
        int highestCount = counts.values().stream()
                .max(Integer::compareTo)
                .orElse(0);

        return counts.entrySet().stream()
                .filter(entry -> entry.getValue() == highestCount)
                .map(Map.Entry::getKey)
                .toList();
    }

    public List<Vehicle> sortByVehicleNumberDescending() {
        vehicles.sort(Comparator.comparing(Vehicle::getVehicleNumber).reversed());
        return getAllVehicles();
    }

    public Map<VehicleType, Integer> statisticsByType() {
        Map<VehicleType, Integer> statistics = new EnumMap<>(VehicleType.class);
        for (VehicleType type : VehicleType.values()) {
            statistics.put(type, 0);
        }
        for (Vehicle vehicle : vehicles) {
            statistics.merge(vehicle.getVehicleType(), 1, Integer::sum);
        }
        return Map.copyOf(statistics);
    }

    public List<Vehicle> getAllVehicles() {
        return List.copyOf(vehicles);
    }

    private void rebuildOwnerIndex() {
        ownersById.clear();
        for (Vehicle vehicle : vehicles) {
            Owner owner = vehicle.getOwner();
            ownersById.put(owner.getIdNumber(), owner);
        }
    }
}
