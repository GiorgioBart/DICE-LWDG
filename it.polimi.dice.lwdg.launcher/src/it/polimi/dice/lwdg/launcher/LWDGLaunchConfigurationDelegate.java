package it.polimi.dice.lwdg.launcher;

import java.io.IOException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;




public class LWDGLaunchConfigurationDelegate extends LaunchConfigurationDelegate{

	
	@Override
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
		throws CoreException {
		//DICER-SERVICE URL
		String restURL = configuration.getAttribute(
				LWDGLaunchConfigurationAttributes.SERVICE_URL, "http://localhost:8176");
		//INPUT XMI
		String fileToConvert = configuration.getAttribute(
				LWDGLaunchConfigurationAttributes.FILE_TO_CONVERT, "someInfile");
		//OUT TOSCA
		String convertedFile = configuration.getAttribute(
				LWDGLaunchConfigurationAttributes.CONVERTED_FILE, "someOutfile");
		
		//Log vital data to console
		//System.out.println(restURL+' '+fileToConvert+' '+convertedFile);
        
   
        try {       	 
			LWDGHttpclient.PostXmi(restURL,fileToConvert,convertedFile );		
			} catch (IOException e) {
				
			}              
		}
	}
