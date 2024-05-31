package solar.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import solar.models.Material;
import solar.models.Panel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PanelFileRepositoryTest {
    private static final String SEED_FILE_PATH = "./data/panels-seed.csv";
    private static final String TEST_FILE_PATH = "./data/panels-test.csv";

    private final PanelFileRepository repository = new PanelFileRepository(TEST_FILE_PATH);

    @BeforeEach
    public void setup() throws IOException {
        Path seedPath = Paths.get(SEED_FILE_PATH);
        Path testPath = Paths.get(TEST_FILE_PATH);

        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    public void shouldFindByExistingSection() throws DataException {
        List<Panel> section = repository.findBySection("The Ridge");

        assertNotNull(section);
        assertEquals(2, section.size());
        assertEquals(2014, section.get(0).getYear());
        assertEquals(2015, section.get(1).getYear());
        assertEquals(Material.COPPER_INDIUM_GALLIUM_SELENIDE, section.get(0).getMaterial());
    }

    @Test
    public void shouldNotFindNonExistingSection() throws DataException {
        List<Panel> section = repository.findBySection("Section1");
        assertEquals(0, section.size());
    }

    @Test
    public void shouldAdd() throws DataException {
        Panel panel = new Panel(
                0,
                "Flats",
                3,
                4,
                2017,
                Material.AMORPHOUS_SILICON,
                false
        );

        Panel actual = repository.add(panel);

        assertEquals(5, actual.getId());
        assertEquals("Flats", actual.getSection());
        assertEquals(3, actual.getRow());
        assertEquals(4, actual.getColumn());
        assertEquals(2017, actual.getYear());
        assertEquals(Material.AMORPHOUS_SILICON, actual.getMaterial());
        assertFalse(actual.tracking());
    }

    @Test
    public void shouldUpdate() throws DataException {
        List<Panel> panel = repository.findBySection("Treeline");
        panel.get(0).setMaterial(Material.MONOCRYSTALLINE_SILICON);
        panel.get(0).setRow(8);

        boolean result = repository.update(panel.get(0));

        assertTrue(result);
        assertNotNull(panel);

        assertEquals(3, panel.get(0).getId());
        assertEquals("Treeline", panel.get(0).getSection());
        assertEquals(8, panel.get(0).getRow());
        assertEquals(5, panel.get(0).getColumn());
        assertEquals(2020, panel.get(0).getYear());
        assertEquals(Material.MONOCRYSTALLINE_SILICON, panel.get(0).getMaterial());
        assertFalse(panel.get(0).tracking());
    }

    @Test
    public void shouldNotUpdateUnknownSection() throws DataException {
        Panel panel = new Panel(0, "Section", 0, 0, 2010, Material.CADMIUM_TELLURIDE, false);
        boolean result = repository.update(panel);
        assertFalse(result);
    }

    @Test
    public void shouldDelete() throws DataException {
        boolean result = repository.deleteById(1);
        assertTrue(result);

        List<Panel> section = repository.findBySection("The Ridge");
        assertEquals(1, section.size());

    }

    @Test
    public void shouldNotDeleteUnknownSection() throws DataException {
        boolean result = repository.deleteById(999);
        assertFalse(result);
    }

}