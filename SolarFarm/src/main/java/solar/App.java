package solar;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import solar.data.DataException;
import solar.data.PanelFileRepository;
import solar.domain.PanelResult;
import solar.domain.PanelService;
import solar.models.Panel;
import solar.ui.Controller;
import solar.ui.View;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;

public class App {

    // Instantiate all required classes with valid arguments, dependency injection. run controller
    public static void main(String[] args) throws DataException {
        //Path path = Paths.get("./data/test.csv");

        //PanelFileRepository repository = new PanelFileRepository();

        //PanelFileRepository repository = new PanelFileRepository("/Users/sadibanoor/Desktop/java-async-work/assessments/module03-assessment-solarfarm-sadibanur/SolarFarm/data/panels.csv");

        //PanelService service = new PanelService(repository);
        //View view = new View();
        //Controller controller = new Controller(view, service);
        //controller.run();


        //List<Panel> panels = repository.findAll();

        //for (Panel panel : panels) {
        //    System.out.println(panel);
        //}
        configureWithXMLAndRun();
    }

    private static void configureWithXMLAndRun() {
        ApplicationContext container = new ClassPathXmlApplicationContext("dependency-config.xml");
        Controller controller = container.getBean(Controller.class);
        controller.run();
    }

    private static void configureManuallyAndRun() {

        PanelFileRepository repository = new PanelFileRepository("/Users/sadibanoor/Desktop/java-async-work/assessments/module03-assessment-solarfarm-sadibanur/SolarFarm/data/panels.csv");

        PanelService service = new PanelService(repository);
        View view = new View();
        Controller controller = new Controller(view, service);
        controller.run();
    }

}
