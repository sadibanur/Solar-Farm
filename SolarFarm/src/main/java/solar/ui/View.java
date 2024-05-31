package solar.ui;

import solar.domain.PanelResult;
import solar.models.Material;
import solar.models.Panel;

import java.sql.SQLOutput;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class View {

    Scanner console = new Scanner(System.in);

    public int chooseOptionFromMenu() {
        printHeader("Main Menu");
        displayText("0. Exit");
        displayText("1. Find Panels by Section");
        displayText("2. Add a Panel");
        displayText("3. Update a Panel");
        displayText("4. Remove a Panel");

        return readInt("Select [0-4]: ", 0, 4);
    }

    public void printHeader(String header) {
        System.out.println();
        System.out.println(header);
        System.out.println("=".repeat(header.length()));
    }

    public void displayText(String line) {
        System.out.println();
        System.out.print(line);
    }

    public void printResult(PanelResult result) {
        if (result.isSuccess()) {
            displayText("[Success]");
            for (String message : result.getMessages()) {
                if (message.contains("added")) {
                    System.out.printf("Panel %s-%s-%s added.", result.getPanel().getSection(),
                            result.getPanel().getRow(), result.getPanel().getColumn());
                } else if (message.contains("updated")) {
                    System.out.printf("Panel %s-%s-%s updated.", result.getPanel().getSection(),
                            result.getPanel().getRow(), result.getPanel().getColumn());
                } else {
                    System.out.printf("Panel %s-%s-%s removed.", result.getPanel().getSection(),
                            result.getPanel().getRow(), result.getPanel().getColumn());
                }
            }

        } else {
            displayText("[Err]");
            System.out.printf("There is no Panel %s-%s-%s.",result.getPanel().getSection(),
                    result.getPanel().getRow(), result.getPanel().getColumn());
        }
    }

    public void printPanels(String sectionName, List<Panel> panels) {
        displayText("Panels in the " + sectionName);
        System.out.println();
        System.out.printf("%-3s %-4s %-4s %-6s %-30s %-8s%n", "id", "Row", "Col", "Year", "Material", "Tracking");

        for (Panel panel : panels) {
            System.out.printf("%-3s %-4d %-4d %-6d %-30s %-8s%n",
                    panel.getId(), panel.getRow(), panel.getColumn(), panel.getYear(),
                    panel.getMaterial().getDisplayText(), panel.tracking() ? "yes" : "no");
        }
    }

    public Panel choosePanel(String sectionName, List<Panel>panels) {
        printHeader("Choose a Panel in Section: " + sectionName);
        printPanels(sectionName, panels);
        int choice = readInt("Enter the Panel Id number: ");
        if (choice < 1 ) {
            choice = readInt("You must enter a valid number.");
        }

        Panel chosenPanel = null;
        for (Panel panel : panels) {
            if (panel.getId() == choice) {
                chosenPanel = panel;
            }
        }

        return chosenPanel;
    }


    public Panel makePanel() {
        Panel result = new Panel();

        result.setSection(readString("Section: "));
        result.setId(readInt("Id: "));
        result.setRow(readInt("Row: "));
        result.setColumn(readInt("Column: "));
        result.setMaterial(readMaterial());
        result.setYear(readInt("Installation: "));
        result.setTracking(readTracking("Tracked [y/n]: "));

        return result;
    }

    public Panel update(Panel panel) {
        panel.setSection(readSection("Section (" + panel.getSection() + "): "));
        panel.setRow(readInt("Row (" + panel.getRow() + "): "));
        panel.setColumn(readInt("Column (" + panel.getColumn() + "): "));
        panel.setYear(readInt("Installation Year (" + panel.getYear() + "): "));
        panel.setMaterial(readMaterial());
        panel.setTracking(readTracking("Tracked (" + (panel.tracking() ? "yes" : "no") + ") [y/n]: "));
        return panel;
    }

    public String readSection(String message)  {

        return readRequiredString("Section Name: ");
    }

    public String readString(String message) {
        System.out.print(message);
        String input = console.nextLine();
        return input;
    }

    public String readRequiredString(String prompt) {
        System.out.print(prompt);
        String string = console.nextLine();
        if (string == null || string.isBlank()) {
            displayText("You must enter a value!");
            string = readRequiredString(prompt);
        }
        return string;
    }

    private int readInt(String prompt) {
        String value = readString(prompt);
        int intValue = Integer.parseInt(value);

        return intValue;
    }

    private int readInt(String prompt, int min, int max) {
        while (true) {
            String value = readString(prompt);
            try {
                int intValue = Integer.parseInt(value);
                if (intValue < min || intValue > max) {
                    System.out.println("Sorry, that's not a valid number");
                } else {
                    return intValue;
                }
            } catch (NumberFormatException ex) {
                System.out.printf("%s is not a valid number.%n", value);
            }
        }
    }

    private Material readMaterial() {
        System.out.println("Choose from the Material List: ");
        for (Material material : Material.values()) {
            System.out.println(material.getDisplayText());
        }
        while (true) {
            String selection = readRequiredString("Material: ");
            //selection = selection.toUpperCase();
            try {
                return Material.valueOf(selection);
            } catch (IllegalArgumentException ex) {
                System.out.printf("%s is not a Material%n.", selection);
            }
        }
    }

    private boolean readTracking(String message) {
        System.out.print(message);

        String input = console.nextLine();
        boolean tracking = false;

        if (input == null || input.isBlank()) {
            displayText("You must enter yes/no");
            tracking = readTracking(message);
        }
        if (input.equals("yes")) {
            tracking = true;
        } else if (input.equals("no")) {
            tracking = false;
        }
        return tracking;
    }
}
