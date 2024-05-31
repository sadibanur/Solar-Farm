package solar.data;

import solar.models.Material;
import solar.models.Panel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PanelFileRepository implements PanelRepository {
    private final String filePath;

    private static final String DELIMITER = ",";
    private static final String DELIMITER_REPLACEMENT = "@@@";

    public PanelFileRepository(String filePath) {
        this.filePath = filePath;
    }


    // Finds all Panels in the data source (file)
    public List<Panel> findAll() throws DataException{
        List<Panel> result = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine();

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                Panel panel = LineToPanel(line);
                result.add(panel);
            }
        } catch (FileNotFoundException ex) {
            throw new DataException("File not found: " + filePath, ex);
        } catch (IOException ex) {
            throw new DataException("Could not open file path: " + filePath);
        }
        return result;
    }


    // Convert a String into a Panel
    private Panel LineToPanel(String line) {
        String[] fields = line.split(DELIMITER);
        if (fields.length != 7) {
            return null;
        }

        Panel panel = new Panel(
                Integer.parseInt(fields[0]),
                restore(fields[1]),
                Integer.parseInt(fields[2]),
                Integer.parseInt(fields[3]),
                Integer.parseInt(fields[4]),
                Material.valueOf(fields[5]),
                Boolean.parseBoolean(fields[6])
        );

        return panel;
    }

    // Take care of the delimiter in the csv file
    private String restore(String value) {
        return value.replace(DELIMITER_REPLACEMENT, DELIMITER);
    }


    // Write the changes to the file
    private void writeToFile(List<Panel> all) throws DataException {
        try (PrintWriter writer = new PrintWriter(filePath)) {
            writer.println("id,section,row,column,year,material,isTracking");

            for (Panel panel : all) {
                String line = panelToLine(panel);
                writer.println(line);
            }
        } catch (IOException ex) {
            throw new DataException("Could ot write filepath: " + filePath);
        }
    }

    // Convert a Panel into a String (a line) in the file
    private String panelToLine(Panel panel) {
        StringBuilder buffer = new StringBuilder(200);

        buffer.append(panel.getId()).append(DELIMITER);
        buffer.append(clean(panel.getSection())).append(DELIMITER);
        buffer.append(panel.getRow()).append(DELIMITER);
        buffer.append(panel.getColumn()).append(DELIMITER);
        buffer.append(panel.getYear()).append(DELIMITER);
        buffer.append(panel.getMaterial()).append(DELIMITER);
        buffer.append(panel.tracking());

        return buffer.toString();
    }

    // Clean the data
    private String clean(String section) {
        return section.replace(DELIMITER, DELIMITER_REPLACEMENT);
    }


    // Get next id
    private int getNextId(List<Panel> all) {
        int maxId = 0;
        for (Panel panel: all) {
            if (maxId < panel.getId()) {
                maxId = panel.getId();
            }
        }
        return maxId + 1;
    }


    // Finds all Panels in a section, uses the private findAll method
    @Override
    public List<Panel> findBySection(String section) throws DataException {
        List<Panel> all = findAll();
        List<Panel> panelsOfSection = new ArrayList<>();

        for (Panel panel : all) {
            if (panel.getSection().equals(section)) {
                panelsOfSection.add(panel);
            }
        }

        return panelsOfSection;
    }


    // Create a Panel
    @Override
    public Panel add(Panel panel) throws DataException {
        List<Panel> all = findAll();
        int nextId = getNextId(all);
        panel.setId(nextId);

        all.add(panel);
        writeToFile(all);

        return panel;
    }


    //Update a Panel
    @Override
    public boolean update(Panel panel) throws DataException {
        List<Panel> all = findAll();

        for (int i = 0; i < all.size(); i ++) {
            if (all.get(i).getId() == panel.getId()) {
                all.set(i, panel);
                writeToFile(all);
                return true;
            }
        }
        return false;
    }


    // Delete the Panel with specific id
    @Override
    public boolean deleteById(int id) throws DataException {
        List<Panel> all = findAll();

        for (int i = 0; i < all.size(); i ++) {
            if (all.get(i).getId() == id) {
                all.remove(i);
                writeToFile(all);
                return true;
            }
        }
        return false;
    }
}
