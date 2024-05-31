package solar.models;

public enum Material {
    MULTICRYSTALLINE_SILICON("MULTICRYSTALLINE_SILICON"),
    MONOCRYSTALLINE_SILICON("MONOCRYSTALLINE_SILICON"),
    AMORPHOUS_SILICON("AMORPHOUS_SILICON"),
    CADMIUM_TELLURIDE("CADMIUM_TELLURIDE"),
    COPPER_INDIUM_GALLIUM_SELENIDE("COPPER_INDIUM_GALLIUM_SELENIDE");

    private String displayText;

    Material(String displayText) {

        this.displayText = displayText;
    }

    public String getDisplayText() {

        return displayText;
    }
}
