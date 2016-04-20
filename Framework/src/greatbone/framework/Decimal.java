package greatbone.framework;

/**
 * A mutable decimal number. The presence of this class is to facilitate massive data aggregation.
 */
public class Decimal extends Number {

    long value;

    int precision;

    @Override
    public int intValue() {
        return 0;
    }

    @Override
    public long longValue() {
        return 0;
    }

    @Override
    public float floatValue() {
        return 0;
    }

    @Override
    public double doubleValue() {
        return 0;
    }

}
