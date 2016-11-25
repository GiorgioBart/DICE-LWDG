package it.polimi.dice.lwdg.loaddemo;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class getRepoPreference extends Dialog{
    private Text modelsField;
    private Text metamodelsField;
    private String modelsString;
    private String metamodelsString;

    public getRepoPreference(Shell parentShell) {
        super(parentShell);
    }

    @Override
    protected void configureShell(Shell newShell)
    {
        super.configureShell(newShell);
        newShell.setText("Please insert URL");
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite comp = (Composite) super.createDialogArea(parent);

        GridLayout layout = (GridLayout) comp.getLayout();
        layout.numColumns = 2;

        Label modelsLabel = new Label(comp, SWT.RIGHT);
        modelsLabel.setText("URL for models: ");
        modelsField = new Text(comp, SWT.SINGLE | SWT.BORDER);
        modelsField.setText("https://github.com/dice-project/DICER/releases/download/v0.1.0/models.zip");
        	

        
        Label metamodelsLabel = new Label(comp, SWT.RIGHT);
        metamodelsLabel.setText("URL for metamodels: ");
        metamodelsField = new Text(comp, SWT.SINGLE | SWT.BORDER);
        metamodelsField.setText("https://github.com/dice-project/DICER/releases/download/v0.1.0/metamodels.zip");
        
        
        GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false);
        modelsField.setLayoutData(data);
        metamodelsField.setLayoutData(data);

        return comp;
    }

    @Override
    protected void okPressed()
    {
        modelsString = modelsField.getText();
        metamodelsString = metamodelsField.getText();
        super.okPressed();
    }

    @Override
    protected void cancelPressed()
    {
//        modelsField.setText("");
//        metamodelsField.setText("");
        super.cancelPressed();
    }

    public String getModels()
    {
        return modelsString;
    }
    public String getMetamodels()
    {
        return metamodelsString;
    }
}