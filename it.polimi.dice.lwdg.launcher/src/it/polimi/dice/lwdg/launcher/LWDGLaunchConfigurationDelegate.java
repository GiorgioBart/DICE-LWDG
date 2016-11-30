package it.polimi.dice.lwdg.launcher;

import java.io.IOException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;


/**
 * This is the core class of the DICE-LWDG Launcher.
 * Take preferences setting and execute the POST to dicer-service url
 * @author Giorgio Bartoccioni
 *
 */

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
  
        try {       	 
			LWDGHttpclient.PostXmi(restURL,fileToConvert,convertedFile );		
			} catch (IOException e) {
				
			}              
		}
	}
