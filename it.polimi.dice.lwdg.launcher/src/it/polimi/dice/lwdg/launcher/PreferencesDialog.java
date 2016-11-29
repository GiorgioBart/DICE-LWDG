package it.polimi.dice.lwdg.launcher;

import org.eclipse.core.runtime.Path;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

public class PreferencesDialog {
	private Text URL;
	private Text inputFile;
	private Text outputFile;
//	private String choseFilepath;
	private String choseoutFilepath;
	private Text deployText;
	private Button inBrowseButton;
	
	public void drawDialog(Composite comp){
                
        GridLayoutFactory.swtDefaults().numColumns(2).applyTo(comp);
        ///////////////////   ENDPOINT SETTINGS   /////////////////////////
        
        Label label = new Label(comp, SWT.NONE);
        label.setText("dicer-service host:ip");
        GridDataFactory.swtDefaults().applyTo(label);

        URL = new Text(comp, SWT.BORDER);
        GridDataFactory.fillDefaults().grab(true, false).applyTo(URL);     

        //Input file chose dialog
        inBrowseButton = new Button(comp, SWT.PUSH);
        inBrowseButton.setText("Select input model file");

        inputFile = new Text(comp, SWT.BORDER);
        GridDataFactory.fillDefaults().grab(true, false).applyTo(inputFile);	
        inBrowseButton.addSelectionListener(new SelectionListener() {	
            @Override
       	  public void widgetDefaultSelected(SelectionEvent inEvent) {
       	  }
       	  @Override
       	  public void widgetSelected(SelectionEvent inEvent) {
       		  FileDialog fileDialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);         
       		  fileDialog.setText("Choose xmi file");
       		  String choseFilepath = fileDialog.open();
       		  if (choseFilepath != null) {
       			  inputFile.setText(Path.fromOSString(choseFilepath).makeAbsolute().toOSString());
//       			  updateLaunchConfigurationDialog();
       			  }
       	  	}
         }); 
	}
	
	
	
    public Text getDicerURL()
    {
        return URL;
    }
    
    public Button getInButton()
    {
        return inBrowseButton;
    }
    
    public Text getInputFile()
    {
        return inputFile;
    }
}
