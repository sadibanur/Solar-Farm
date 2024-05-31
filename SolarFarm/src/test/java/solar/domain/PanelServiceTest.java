package solar.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import solar.data.DataException;
import solar.data.PanelRepository;
import solar.data.PanelRepositoryDouble;
import solar.models.Material;
import solar.models.Panel;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PanelServiceTest {
    PanelRepository repository = new PanelRepositoryDouble();

    PanelService service = new PanelService(repository);


    @Test
    public void shouldCreatePanel() throws DataException {
        PanelResult actual = service.add(new Panel(0, "Section", 5, 8, 2015,
                Material.AMORPHOUS_SILICON, true));

        assertNotNull(actual.getPanel());
        assertTrue(actual.isSuccess());

        assertEquals(99, actual.getPanel().getId());
    }

    @Test
    public void shouldNotAddNullTask() throws DataException {
        PanelResult actual = service.add(null);

        assertFalse(actual.isSuccess());
        assertNull(actual.getPanel());
        assertEquals("Panel cannot be null.", actual.getMessages().get(0));

    }

    @Test
    public void shouldNotAddWithoutSection() throws DataException {
        PanelResult actual = service.add(new Panel(0, null, 5, 8, 2015,
                Material.AMORPHOUS_SILICON, true));

        assertFalse(actual.isSuccess());
        assertNull(actual.getPanel());
        assertEquals("Section is required.", actual.getMessages().get(0));
    }

    @Test
    public void shouldNotAddWithWrongYear() throws DataException {
        PanelResult actual = service.add(new Panel(0, "Section", 5, 8, 2030,
                Material.AMORPHOUS_SILICON, true));

        assertFalse(actual.isSuccess());
        assertNull(actual.getPanel());
        assertEquals("The year cannot be unreal", actual.getMessages().get(0));
    }

    @Test
    public void shouldNotAddWithNegativeRow() throws DataException {
        PanelResult actual = service.add(new Panel(0, "Section", -5, 8, 2015,
                Material.AMORPHOUS_SILICON, true));

        assertFalse(actual.isSuccess());
        assertNull(actual.getPanel());
        assertEquals("Row or column cannot be negative or zero.", actual.getMessages().get(0));
    }

    @Test
    public void shouldNotAddWithNegativeColumn() throws DataException {
        PanelResult actual = service.add(new Panel(0, "Section", 5, -8, 2015,
                Material.AMORPHOUS_SILICON, true));

        assertFalse(actual.isSuccess());
        assertNull(actual.getPanel());
        assertEquals("Row or column cannot be negative or zero.", actual.getMessages().get(0));
    }

    @Test
    public void shouldNotAddWithoutMaterial() throws DataException {
        PanelResult actual = service.add(new Panel(0, "Section", 5, 8, 2015,
                null, true));

        assertFalse(actual.isSuccess());
        assertNull(actual.getPanel());
        assertEquals("Material is required.", actual.getMessages().get(0));
    }

    @Test
    public void shouldNotAddDuplicatePanel() throws DataException {
        Panel duplicatePanel1 = new Panel(4, "Test", 1, 23, 2020,
                Material.MONOCRYSTALLINE_SILICON,true);

        PanelResult actual1 = service.add(duplicatePanel1);

        assertFalse(actual1.isSuccess());
        assertEquals("Duplicate panel found in the same location.", actual1.getMessages().get(0));

        Panel duplicatePanel2 = new Panel(6, "Test2", 2, 9, 2014,
                Material.CADMIUM_TELLURIDE,false);

        PanelResult actual2 = service.add(duplicatePanel2);
        assertFalse(actual2.isSuccess());
        assertEquals("Duplicate panel found in the same location.", actual2.getMessages().get(0));
    }

    @Test
    public void shouldFindExistingSection() throws DataException {
        List<Panel> sections = service.findBySection("Test");

        Panel panel = sections.get(0);
        assertEquals(1, panel.getId());
        assertEquals("Test", panel.getSection());
        assertEquals(1, panel.getRow());
        assertEquals(23, panel.getColumn());
        assertEquals(2020, panel.getYear());
        assertEquals(Material.MONOCRYSTALLINE_SILICON, panel.getMaterial());
        assertTrue(panel.tracking());
    }

    @Test
    public void shouldFindNonExistingSection() throws DataException {
        List<Panel> actual = service.findBySection("Section");
        assertEquals(0, actual.size());
    }

    @Test
    public void shouldUpdateExistingPanel() throws DataException {
        List<Panel> sections = service.findBySection("Test2");
        Panel toUpdate = sections.get(0);
        toUpdate.setYear(2017);

        PanelResult actual = service.update(toUpdate);
        assertTrue(actual.isSuccess());
        assertEquals(0, actual.getMessages().size());
        assertEquals(2017, sections.get(0).getYear());
    }


    @Test
    public void shouldNotUpdateNullPanel() throws DataException {
        PanelResult actual = service.update(null);

        assertFalse(actual.isSuccess());
        assertEquals(1, actual.getMessages().size());
        assertEquals("Panel cannot be null.", actual.getMessages().get(0));
    }

    @Test
    public void shouldDeleteExistingPanel() throws DataException {
        PanelResult actual = service.deleteById(1);

        assertTrue(actual.isSuccess());
    }


    @Test
    public void shouldNotDeleteNonExistingPanel() throws DataException {
        PanelResult actual = service.deleteById(999);
        assertFalse(actual.isSuccess());
    }

}