package it.polimi.dice.lwdg.launcher;

import java.io.IOException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;


@SuppressWarnings("deprecation")
public class LWDGMainLaunchConfigurationTab extends AbstractLaunchConfigurationTab {

	private Text text;
	private Text inputFile;
	private Text outputFile;
	private String choseFilepath;
	private String choseoutFilepath;
	private Text deployText;

	@Override
	public void createControl(Composite parent) {
		
		Composite comp = new Group(parent, SWT.BORDER);
        setControl(comp);  
   
        PreferencesDialog PD=new PreferencesDialog();
        PD.drawDialog(comp);
        
        GridLayoutFactory.swtDefaults().numColumns(2).applyTo(comp);

        ///////////////////   FOR ENDPOINT SETTINGS   /////////////////////////
       
/*        Label label = new Label(comp, SWT.NONE);
        label.setText("dicer-service host:ip");
        GridDataFactory.swtDefaults().applyTo(label);

        text = new Text(comp, SWT.BORDER);
        GridDataFactory.fillDefaults().grab(true, false).applyTo(text);
*/        
        text=PD.getDicerURL();
        text.addModifyListener(new ModifyListener(){
            public void modifyText(ModifyEvent event2) {
                updateLaunchConfigurationDialog();
            	setDirty(true);
            	}});
      
      //Input file chose dialog
//     Button inBrowseButton = new Button(comp, SWT.PUSH);
//      inBrowseButton.setText("Select input model file");

//      inputFile = new Text(comp, SWT.BORDER);
//      GridDataFactory.fillDefaults().grab(true, false).applyTo(inputFile);
      

       inputFile=PD.getInputFile();
       	
//      PD.getInButton().addSelectionListener(new SelectionListener() {
/*       inBrowseButton.addSelectionListener(new SelectionListener() {	
         @Override
    	  public void widgetDefaultSelected(SelectionEvent inEvent) {
    	  }
    	  @Override
    	  public void widgetSelected(SelectionEvent inEvent) {
    		  FileDialog fileDialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);         
    		  fileDialog.setText("Choose xmi file");
    		  choseFilepath = fileDialog.open();
    		  if (choseFilepath != null) {
    			  inputFile.setText(Path.fromOSString(choseFilepath).makeAbsolute().toOSString());
    			//  updateLaunchConfigurationDialog();
    			  }
    	  	}
      }); 
*/
       inputFile.addModifyListener(new ModifyListener(){
           public void modifyText(ModifyEvent event3) {
               updateLaunchConfigurationDialog();
           	   setDirty(true);
           	}});

       //Output file chose dialog      
       Button outBrowseButton = new Button(comp, SWT.PUSH);
       outBrowseButton.setText("Select output model file");
      
       outputFile = new Text(comp, SWT.BORDER);
       GridDataFactory.fillDefaults().grab(true, false).applyTo(outputFile);        
          
       outBrowseButton.addSelectionListener(new SelectionListener() {
        	@Override
        	public void widgetDefaultSelected(SelectionEvent outEvent) {
        		
        	}

        	@Override
        	public void widgetSelected(SelectionEvent outEvent) {
        		FileDialog filesaveDialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);         
        		filesaveDialog.setText("Choose output file");
        		choseoutFilepath = filesaveDialog.open();
        		if (choseoutFilepath != null) {
        			outputFile.setText(Path.fromOSString(choseoutFilepath).makeAbsolute().toOSString());
                    updateLaunchConfigurationDialog();
        		  }
        	}
        });

       ///////////////////   CHECK IF SERVER IS ALIVE   ///////////////////////     

       Button checkButton = new Button(comp, SWT.PUSH);
       checkButton.setText("Check server");
       checkButton.addListener(SWT.Selection, new Listener() {
    	      public void handleEvent(Event event) {
    	    	  
    	    	  @SuppressWarnings("resource")
				  HttpClient client = new DefaultHttpClient();
    	    	  HttpGet request = new HttpGet(text.getText());
    	    	  try {
    	    		  	client.execute(request);
    	    		  	MessageBox messageBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_INFORMATION);
    	    		  	messageBox.setMessage("Server is alive");
    	    		  	messageBox.open();
    	    	  	  }catch(IOException e){
    	    	  		MessageBox messageBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_ERROR);
    	    	  		messageBox.setMessage("Connection Refused");
    	    	  		messageBox.open();
    	    	  	  }
    	    	 
    	      }
    	});
       
       new Label(comp, SWT.NONE);		//DUMMY

       ///////////////////   FOR DEPLOY-SERVICE   /////////////////////////////
       
       Button checkBox = new Button(comp,SWT.CHECK);
       checkBox.setText("Send TOSCA to Deploy-service ?");
       
       new Label(comp, SWT.NONE);		//DUMMY
       
       Label deployLabel = new Label(comp, SWT.NONE);
       deployLabel.setText("deploy-service host:ip");
       GridDataFactory.swtDefaults().applyTo(deployLabel);
       deployLabel.setEnabled(false);
       deployText = new Text(comp, SWT.BORDER);
       GridDataFactory.fillDefaults().grab(true, false).applyTo(deployText);
       deployText.setEnabled(false);
       checkBox.addSelectionListener(new SelectionAdapter() {

           @Override
           public void widgetSelected(SelectionEvent event) {
               Button btn = (Button) event.getSource();
               if(!btn.getSelection())
               {
            	   deployLabel.setEnabled(false);
            	   deployText.setEnabled(false);
               }else
               {
            	   deployLabel.setEnabled(true);
            	   deployText.setEnabled(true);
               }
           }
       });
       ////////////////////////////////////////////////////////////////////////
         
	}      

	@Override
	public String getName() {
		
		return "DICER-SERVICE launch tab";
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		
        try {
        text.setText(configuration.getAttribute(LWDGLaunchConfigurationAttributes.SERVICE_URL,"http://localhost:8176"));
        inputFile.setText(configuration.getAttribute(LWDGLaunchConfigurationAttributes.FILE_TO_CONVERT,""));
        outputFile.setText(configuration.getAttribute(LWDGLaunchConfigurationAttributes.CONVERTED_FILE,""));
    } catch (CoreException e) {
    	e.printStackTrace();
    	
    }
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		
		configuration.setAttribute(
				LWDGLaunchConfigurationAttributes.SERVICE_URL, text.getText());
		configuration.setAttribute(
				LWDGLaunchConfigurationAttributes.FILE_TO_CONVERT, inputFile.getText());
		configuration.setAttribute(
				LWDGLaunchConfigurationAttributes.CONVERTED_FILE,outputFile.getText());
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(
				LWDGLaunchConfigurationAttributes.SERVICE_URL, "http://localhost:8176");
		configuration.setAttribute(
				LWDGLaunchConfigurationAttributes.FILE_TO_CONVERT, "");
		configuration.setAttribute(
				LWDGLaunchConfigurationAttributes.CONVERTED_FILE, "");
	
	}
	

}
