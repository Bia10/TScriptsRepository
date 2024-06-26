package net.runelite.client.plugins.tscripts.ui.editor.debug;

import net.runelite.client.plugins.tscripts.adapter.models.Scope.Scope;
import net.runelite.client.plugins.tscripts.adapter.Adapter;
import net.runelite.client.plugins.tscripts.runtime.Runtime;
import net.runelite.client.plugins.tscripts.ui.ScriptPanel;
import net.runelite.client.plugins.tscripts.util.Logging;
import javax.swing.*;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class DebugToolPanel extends JPanel {

    private final JPanel mainView;
    private final CardLayout cardLayout;
    private Path scriptPath;
    private final CFGVisualizer controlFlowGraphVisualizer;
    private final JList<String> toolingList = new JList<>(new String[]{"Control-Flow", "Variables", "Runtime", "Documentation"});

    public DebugToolPanel(Runtime runtime, Path scriptPath, String name) {
        setSize(800, 600);
        setLayout(new BorderLayout());
        this.scriptPath = scriptPath;

        // Create the button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        add(buttonPanel, BorderLayout.WEST);

        // Create the main view area with CardLayout
        mainView = new JPanel();
        cardLayout = new CardLayout();
        mainView.setLayout(cardLayout);
        add(mainView, BorderLayout.CENTER);

        // Add components to the main view
        controlFlowGraphVisualizer = CFGVisualizer.create(runtime, getScope(), name);
        JScrollPane controlFlowGraphScrollPane = new JScrollPane(controlFlowGraphVisualizer);
        controlFlowGraphScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        controlFlowGraphScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mainView.add(controlFlowGraphScrollPane, "ControlFlowGraph");

        VariableInspector variableInspector = VariableInspector.getInstance(runtime);
        JScrollPane variableInspectorScrollPane = new JScrollPane(variableInspector);
        variableInspectorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        variableInspectorScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mainView.add(variableInspectorScrollPane, "VariableInspector");

        RuntimeInspector runtimeInspector = RuntimeInspector.getInstance();
        JScrollPane runtimeInspectorScrollPane = new JScrollPane(runtimeInspector);
        runtimeInspectorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        runtimeInspectorScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mainView.add(runtimeInspectorScrollPane, "RuntimeInspector");

        DocumentationPanel documentationPanel = DocumentationPanel.getInstance();
        JScrollPane documentationScrollPane = new JScrollPane(documentationPanel);
        documentationScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        documentationScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mainView.add(documentationScrollPane, "DocumentationPanel");

        toolingList.addListSelectionListener(e -> {
            String selectedScript = toolingList.getSelectedValue();
            switch (selectedScript) {
                case "Control-Flow":
                    controlFlowGraphVisualizer.updateGraph(getScope());
                    cardLayout.show(mainView, "ControlFlowGraph");
                    break;
                case "Variables":
                    cardLayout.show(mainView, "VariableInspector");
                    break;
                case "Runtime":
                    cardLayout.show(mainView, "RuntimeInspector");
                    break;
                case "Documentation":
                    cardLayout.show(mainView, "DocumentationPanel");
                    break;
            }
        });

        buttonPanel.add(toolingList, BorderLayout.CENTER);
    }

    public void update(ScriptPanel panel, Path scriptPath, String name)
    {
        this.scriptPath = scriptPath;
        controlFlowGraphVisualizer.changeScript(panel.getRuntime(), name);
        controlFlowGraphVisualizer.updateGraph(getScope());
    }

    private Scope getScope()
    {
        try
        {
            return Adapter.parse(Files.readString(scriptPath));
        }
        catch (Exception ex)
        {
            Logging.errorLog(ex);
        }
        return new Scope(new HashMap<>(),null);
    }
}