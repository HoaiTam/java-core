package practice1;

/**
 * Hình cơ bản có chiều rộng và chiều cao.
 */
public class Shape {
    private final double width;
    private final double height;

    public Shape(double width, double height) {
        this.width = requirePositive(width, "Chiều rộng");
        this.height = requirePositive(height, "Chiều cao");
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    protected static double requirePositive(double value, String fieldName) {
        if (!Double.isFinite(value) || value <= 0) {
            throw new IllegalArgumentException(fieldName + " phải là số dương.");
        }
        return value;
    }

    @Override
    public String toString() {
        return "Shape{width=%.2f, height=%.2f}".formatted(width, height);
    }
}
