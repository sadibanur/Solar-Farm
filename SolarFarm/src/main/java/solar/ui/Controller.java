package solar.ui;

import solar.data.DataException;
import solar.domain.PanelResult;
import solar.domain.PanelService;
import solar.models.Panel;

import java.util.List;

public class Controller {
    private final View view;
    private final PanelService panelService;

    public Controller(View view, PanelService panelService) {
        this.view = view;
        this.panelService = panelService;
    }

    public void run() {
        view.printHeader("Welcome to Solar Farm");

        try {
            runMenu();
        } catch (Exception ex) {
            view.displayText("Something went wrong.");
            view.displayText(ex.getMessage());
        }
        view.displayText("Goodbye");
    }

    private void runMenu() throws DataException {
        boolean exit = false;

        while(!exit) {
            int selection = view.chooseOptionFromMenu();
            switch (selection) {
                case 0:
                    exit = true;
                    break;
                case 1:
                    viewBySection();
                    break;
                case 2:
                    addPanel();
                    break;
                case 3:
                    updatePanel();
                    break;
                case 4:
                    deletePanel();
                    break;
            }
        }
    }

    private void viewBySection() throws DataException {
        view.printHeader("Find Panels by Section");
        System.out.println();

        String section = view.readRequiredString("Section Name: ");

        List<Panel> panels = panelService.findBySection(section);

        if (panels.isEmpty()) {
            System.out.println("The section does not exist. Enter a valid section.");
        }

        view.printPanels(section, panels);

    }

    private void addPanel() throws DataException {
        Panel panel = view.makePanel();
        PanelResult result = panelService.add(panel);

        if (result.isSuccess()) {
            System.out.println("[Success]");
            System.out.printf("Panel %s-%s-%s added.", panel.getSection(),
                    panel.getRow(), panel.getColumn());
        } else {
            System.out.println(result.getMessages().get(0));
        }
    }

    private void updatePanel() throws DataException {
        view.printHeader("Update a Panel");

        String sectionName = view.readSection("Section: ");
        List<Panel> panels = panelService.findBySection(sectionName);
        System.out.println("Editing Treeline");
        if (!panels.isEmpty()) {
            Panel panelToUpdate = view.choosePanel(sectionName, panels);
            if (panelToUpdate != null) {
                Panel updatedPanel = view.update(panelToUpdate);
                PanelResult updateResult = panelService.update(updatedPanel);
                if (updateResult.isSuccess()) {
                    System.out.println("[Success]");
                    System.out.printf("Panel %s-%s-%s updated.", updatedPanel.getSection(),
                            updatedPanel.getRow(), updatedPanel.getColumn());
                } else {
                    System.out.printf("There is no panel %s-%s-%s", updatedPanel.getSection(),
                            updatedPanel.getRow(), updatedPanel.getColumn());
                }

            }
        }
    }

    private void deletePanel() throws DataException {
        view.printHeader("Remove a Panel");
        System.out.println();
        String sectionName = view.readSection("Section: ");
        List<Panel> panels = panelService.findBySection(sectionName);
        if (!panels.isEmpty()) {
            Panel panelToDelete = view.choosePanel(sectionName, panels);
            if (panelToDelete != null) {
                int id = panelToDelete.getId();
                PanelResult removeResult = panelService.deleteById(id);
                if (removeResult.isSuccess()) {
                    System.out.println("[Success]");
                    System.out.printf("Panel %s-%s-%s removed.", panelToDelete.getSection(),
                            panelToDelete.getRow(), panelToDelete.getColumn());
                } else {
                    System.out.printf("There is no panel %s-%s-%s", panelToDelete.getSection(),
                            panelToDelete.getRow(), panelToDelete.getColumn());
                }
            }
        }
    }
}
