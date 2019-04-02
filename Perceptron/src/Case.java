public class Case {
    private double [] attributes;
    private String decision;

    public Case(String string,boolean knownDecision){
        if (knownDecision == false) string+=",undefined";
        String[] arr = string.split(",");
        decision = arr[arr.length - 1];
        attributes = new double[arr.length-1];
        for (int i = 0; i <arr.length-1; i++) {
            attributes[i] = Double.parseDouble(arr[i]);
        }
    }


    public double[] getAttributes() {
        return attributes;
    }

    public String getDecision() {
        return decision;
    }

    @Override
    public String toString() {
        String atr = "[" +attributes[0];
        for (int i = 1; i < attributes.length; i++) {
            atr += "," + attributes[i];
        }
        atr += "]";
        return  atr + "  " + decision;
    }
}
