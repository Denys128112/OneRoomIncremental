package Services.stub;

import java.math.BigDecimal;
import java.math.MathContext;

/** Temporary upgrade DTO. Replace it with the team's real progression model later. */
public class UpgradeStub {
    private static final MathContext MATH = MathContext.DECIMAL128;

    public enum Branch {
        GENERAL("ЗАГАЛЬНА", "general-icon"),
        WARRIOR("КІБЕР-САМУРАЙ", "warrior-icon"),
        RANGER("СТРІЛЕЦЬ", "ranger-icon"),
        MAGE("ПСІОНІК", "mage-icon"),
        HYBRID("ГІБРИДНА", "hybrid-icon");

        public final String title;
        public final String icon;

        Branch(String title, String icon) {
            this.title = title;
            this.icon = icon;
        }
    }

    private final String id;
    private final Branch branch;
    private final String name;
    private final String description;
    private final BigDecimal baseCost;
    private final BigDecimal growth;
    private int level;

    public UpgradeStub(String id, Branch branch, String name, String description,
                       String baseCost, String growth) {
        this.id = id;
        this.branch = branch;
        this.name = name;
        this.description = description;
        this.baseCost = new BigDecimal(baseCost);
        this.growth = new BigDecimal(growth);
    }

    public BigDecimal getCost() {
        return baseCost.multiply(growth.pow(level, MATH), MATH);
    }

    public void purchase() {
        level++;
    }

    public void reset() {
        level = 0;
    }

    public String getId() {
        return id;
    }

    public Branch getBranch() {
        return branch;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getLevel() {
        return level;
    }
}
