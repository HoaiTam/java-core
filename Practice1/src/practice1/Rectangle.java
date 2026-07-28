package practice1;

/**
 * Hình chữ nhật kế thừa chiều rộng và chiều cao từ Shape.
 */
public final class Rectangle extends Shape {

    public Rectangle(double width, double height) {
        super(width, height);
    }

    public double getArea() {
        return getWidth() * getHeight();
    }

    public double getPerimeter() {
        return 2 * (getWidth() + getHeight());
    }

    @Override
    public String toString() {
        return "Rectangle{width=%.2f, height=%.2f, area=%.2f, perimeter=%.2f}"
                .formatted(getWidth(), getHeight(), getArea(), getPerimeter());
    }
}
