package solar.data;

import solar.models.Material;
import solar.models.Panel;

import java.util.ArrayList;
import java.util.List;

public class PanelRepositoryDouble implements PanelRepository{

    public List<Panel> findAll() {
        List<Panel> all = new ArrayList<>();

        all.add(new Panel(1, "Test", 1, 23, 2020,
                Material.MONOCRYSTALLINE_SILICON,true));
        all.add(new Panel(2, "Test2", 2, 9, 2014,
                Material.CADMIUM_TELLURIDE,false));
        all.add(new Panel(3, "Test3", 3, 5, 2016,
                Material.AMORPHOUS_SILICON, true));

        return all;
    }

    @Override
    public List<Panel> findBySection(String section) throws DataException {
        List<Panel> sections = new ArrayList<>();

        for (Panel panel : findAll()) {
            if (panel.getSection().equals(section)) {
                sections.add(panel);
            }
        }
        return sections;
    }

    @Override
    public Panel add(Panel panel) throws DataException {
        panel.setId(99);
        return panel;
    }

    @Override
    public boolean update(Panel panel) throws DataException {
        return panel.getId() > 0;
    }

    @Override
    public boolean deleteById(int id) throws DataException {
        return id != 999;
    }
}
