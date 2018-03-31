package cobweb3d.plugins.exchange.utility;

import cobwebutil.io.*;

public class UtilityFunctionParam implements ParameterSerializable {
    private static final long serialVersionUID = 1;

    @ConfDisplayName("a")
    @ConfXMLTag("a")
    public float varA = 0.5f;

    @ConfDisplayName("b")
    @ConfXMLTag("b")
    public float varB = 0.5f;

    @ConfDisplayName("Formula")
    @ConfXMLTag("formula")
    public Formula formula = Formula.NONE;

    @Override
    public UtilityFunctionParam clone() {
        try {
            UtilityFunctionParam copy = (UtilityFunctionParam) super.clone();
            CloneHelper.resetMutatable(copy);
            return copy;
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
    }

    public enum Formula implements ParameterChoice, Calculation {
        NONE("0", "None", ((x, y, A, B) -> 0)),
        SQRTxy("1", "\u221A(x * y)", ((x, y, A, B) -> (float) Math.sqrt(x * y))),
        MINxy("2", "min(x, y)", ((x, y, A, B) -> x < y ? x : y)),
        xPaMyPb("3", "x^A * y^B", ((x, y, A, B) -> (float) (Math.pow(x, A) * Math.pow(y, B)))),
        axAby("4", "Ax + By", ((x, y, A, B) -> ((A * x) + (B * y))));

        private final String id;
        private final String name;
        private final Calculation calculation;

        Formula(String id, String name, Calculation calculation) {
            this.id = id;
            this.name = name;
            this.calculation = calculation;
        }

        /**
         * This is implemented for loading as ParameterSerializable.
         *
         * @param string input string value for this enum.
         * @return Enum value that corresponds with the given string.
         */
        public static UtilityFunctionParam.Formula fromString(String string) {
            for (Formula formula : Formula.values()) if (formula.getName().equals(string)) return formula;
            return NONE;
        }

        @Override
        public String getIdentifier() {
            return id;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return getName();
        }

        @Override
        public float calculateU(float x, float y, float A, float B) {
            return calculation.calculateU(x, y, A, B);
        }
    }

    public static class FormulaChoiceCatalogue extends ChoiceCatalog {
        public FormulaChoiceCatalogue() {
            super();
            for (Formula formula : Formula.values()) addChoice(Formula.class, formula);
        }
    }
}
