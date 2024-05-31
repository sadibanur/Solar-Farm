package solar.domain;

import solar.models.Panel;

import java.util.ArrayList;
import java.util.List;

public class PanelResult {
    private final ArrayList<String> messages = new ArrayList<>();

    private Panel panel;

    public Panel getPanel() {
        return panel;
    }

    public void setPanel(Panel panel) {
        this.panel = panel;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void addMessage(String message) {
        messages.add(message);
    }

    public boolean isSuccess() {
        return messages.isEmpty();
    }
}
