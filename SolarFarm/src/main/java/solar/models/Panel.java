package solar.models;

import java.util.Objects;

public class Panel {
    private int id;
    private String section;
    private int row;
    private int column;
    private int year;
    private Material material;
    private boolean tracking;

    public Panel(int id, String section, int row, int column, int year, Material material, boolean tracking) {
        this.id = id;
        this.section = section;
        this.row = row;
        this.column = column;
        this.year = year;
        this.material = material;
        this.tracking = tracking;
    }

    public Panel() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public boolean tracking() {
        return tracking;
    }

    public void setTracking(boolean tracking) {
        this.tracking = tracking;
    }

    @Override
    public String toString() {
        return "SolarPanel{" +
                "id ='" + id + '\'' +
                "section='" + section + '\'' +
                ", row=" + row +
                ", column=" + column +
                ", year=" + year +
                ", material=" + material +
                ", tracking=" + tracking +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Panel panel = (Panel) o;
        return row == panel.row && column == panel.column &&
                Objects.equals(section, panel.section);
    }

    @Override
    public int hashCode() {
        return Objects.hash(section, row, column);
    }
}
