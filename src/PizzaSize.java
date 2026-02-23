public enum PizzaSize {
    SMALL(1.0),
    MEDIUM(1.2),
    LARGE(1.5);

    private final double coefficient;

    PizzaSize(double coefficient) {
        this.coefficient = coefficient;
    }

    public double getCoefficient() {
        return coefficient;
    }
}