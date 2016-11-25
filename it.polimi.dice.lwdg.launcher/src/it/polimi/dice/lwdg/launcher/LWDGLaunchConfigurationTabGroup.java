package it.polimi.dice.lwdg.launcher;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

public class LWDGLaunchConfigurationTabGroup extends AbstractLaunchConfigurationTabGroup{

	@Override
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		
		setTabs(new ILaunchConfigurationTab[] {new LWDGMainLaunchConfigurationTab(), new CommonTab() });
	}

}
