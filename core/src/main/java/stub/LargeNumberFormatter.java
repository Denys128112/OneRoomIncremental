package stub;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Formats idle-game values without converting them to overflow-prone primitives. */
public final class LargeNumberFormatter {
    private static final BigDecimal THOUSAND = new BigDecimal("1000");
    private static final String[] SUFFIXES = {
        "", "K", "M", "B", "T", "Qa", "Qi", "Sx", "Sp", "Oc", "No", "Dc"
    };

    private LargeNumberFormatter() {
    }

    public static String format(BigDecimal value) {
        BigDecimal display = value.abs();
        int suffix = 0;
        while (display.compareTo(THOUSAND) >= 0 && suffix < SUFFIXES.length - 1) {
            display = display.divide(THOUSAND, 6, RoundingMode.DOWN);
            suffix++;
        }
        int scale = display.compareTo(new BigDecimal("100")) >= 0 ? 0 : 2;
        display = display.setScale(scale, RoundingMode.DOWN).stripTrailingZeros();
        return (value.signum() < 0 ? "-" : "") + display.toPlainString() + SUFFIXES[suffix];
    }
}
