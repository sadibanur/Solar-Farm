package solar.domain;

import solar.data.DataException;
import solar.data.PanelRepository;
import solar.models.Panel;

import java.util.List;

public class PanelService {
    private final PanelRepository repository;

    public PanelService(PanelRepository repository) {
        this.repository = repository;
    }

    public List<Panel> findBySection(String section) throws DataException {
        return repository.findBySection(section);
    }

    public PanelResult add(Panel panel) throws DataException {
        PanelResult result = validate(panel);

        if (!result.isSuccess()) {
            return result;
        }

        if (panel == null) {
            result.addMessage("Panel cannot be null.");
            return result;
        }


        panel = repository.add(panel);
        result.setPanel(panel);
        return result;
    }


    public PanelResult update(Panel panel) throws DataException {
        PanelResult result = validate(panel);

        if (!result.isSuccess()) {
            return result;
        }

        boolean updated = repository.update(panel);

        if (!updated) {
            result.addMessage(String.format("Panel with id: %s does not exist.", panel.getId()));
        }

        return result;
    }


    public PanelResult deleteById(int id) throws DataException {
        PanelResult result = new PanelResult();

        if (!repository.deleteById(id)) {
            result.addMessage(String.format("Panel with id: %s does not exist.",id));
        }
        return result;
    }


    private PanelResult validate(Panel panel) throws DataException {
        PanelResult result = new PanelResult();

        if (panel == null) {
            result.addMessage("Panel cannot be null.");
            return result;
        }

        if (panel.getSection() == null) {
            result.addMessage("Section is required.");
            return result;
        }

        if (panel.getYear() < 0 || panel.getYear() > 2024) {
            result.addMessage("The year cannot be unreal");
            return result;
        }

        if (panel.getRow() <= 0 || panel.getColumn() <= 0) {
            result.addMessage("Row or column cannot be negative or zero.");
            return result;
        }

        if (panel.getRow() > 250 || panel.getColumn() > 250) {
            result.addMessage("Row or column cannot be more than 250.");
            return result;
        }

        if (panel.getMaterial() == null) {
            result.addMessage("Material is required.");
            return result;
        }

        List<Panel> panelsInSection = findBySection(panel.getSection());

        for (Panel existingPanel : panelsInSection) {
            if (existingPanel.getRow() == panel.getRow() &&
                    existingPanel.getColumn() == panel.getColumn()) {
                result.addMessage("Duplicate panel found in the same location.");
            }
        }

        return result;
    }
}
