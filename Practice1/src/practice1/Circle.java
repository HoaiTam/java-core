package practice1;

/**
 * Hình tròn kế thừa Shape. Width và height của Shape đều là đường kính.
 */
public final class Circle extends Shape {
    private static final double PI = 3.14;

    public Circle(double radius) {
        super(
                requirePositive(radius, "Bán kính") * 2,
                requirePositive(radius, "Bán kính") * 2);
    }

    public double getRadius() {
        return getWidth() / 2;
    }

    public double getDiameter() {
        return getWidth();
    }

    public double getArea() {
        return PI * getRadius() * getRadius();
    }

    public double getCircumference() {
        return getDiameter() * PI;
    }

    @Override
    public String toString() {
        return "Circle{radius=%.2f, diameter=%.2f, area=%.2f, circumference=%.2f}"
                .formatted(getRadius(), getDiameter(), getArea(), getCircumference());
    }
}
