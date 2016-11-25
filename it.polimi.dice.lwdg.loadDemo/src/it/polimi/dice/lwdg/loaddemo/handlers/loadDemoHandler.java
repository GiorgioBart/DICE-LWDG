package it.polimi.dice.lwdg.loaddemo.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;
import org.eclipse.ui.wizards.datatransfer.ZipFileStructureProvider;

import it.polimi.dice.lwdg.loaddemo.getRepoPreference;

import org.eclipse.jface.dialogs.MessageDialog;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.zip.ZipFile;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.commons.io.IOUtils;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.BasicExtendedMetaData;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;

/**
 * This class create a demo project to show the power of DICER !
 * After create the project in the workspace, a complete collection of ready to use model for some DIA will be available in the Workspace.
 * Also the ecore metamodels is downloaded and registered on Eclipse EMF.  
 * 
 * @author Giorgio Bartoccioni
 *
 */

@SuppressWarnings("deprecation")
public class loadDemoHandler extends AbstractHandler {
	
	/**
	 * Create a project in workspace and return a boolean
	 * @param projectName The name of the project to be created
	 * @return Boolean
	 */
	private Boolean createProject(String projectName)
	{
		IProgressMonitor progressMonitor = new NullProgressMonitor();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(projectName);
		try {
		project.create(progressMonitor);
		return true;
		} catch (CoreException e) {
		return false;
		}
	}
	
	/**
	 * Download content from given URL (repo) 
	 * @param repo  Where to download Models and Metamodels
	 * @return This method return an InputStream 
	 * @throws Exception 
	 */
    private InputStream getGithub(String repo) throws Exception{
    	if(repo != null && !repo.isEmpty()){
    			@SuppressWarnings({ "resource" })
    			HttpClient client = new DefaultHttpClient();
    			HttpGet request = new HttpGet(repo);
    			HttpResponse response = client.execute(request);
    			HttpEntity outentity = response.getEntity();
    			InputStream is = outentity.getContent();
    			return is;  		
    	}else{
    		throw new Exception("Repo not valid");
    	}

    }
    
    /**
     * Write a temp file from InputStream given as parameter
     * @param is The InputStrteam to write to temp file
     * @return absolute path of the temp file
     */
	private String writeTemp (InputStream is)
    {

    	try{		
    		File targetFile = File.createTempFile("models", ".tmp");
    	    OutputStream outStream = new FileOutputStream(targetFile);
    	    byte[] buffer = new byte[8 * 1024];
    	    int bytesRead;
    	    while ((bytesRead = is.read(buffer)) != -1) {
    	        outStream.write(buffer, 0, bytesRead);
    	    }
    	    IOUtils.closeQuietly(is);
    	    IOUtils.closeQuietly(outStream);
    	    return targetFile.getAbsolutePath();
    	}catch(IOException e){
    		return null;
    	}
    }
	
	/**
	 * Registers given ecore file in EcoreResourceFactory
	 * @param workspaceEcorePath Path of the ecore file relative to the workspace
	 * @return Boolean
	 */
	private Boolean registerEcore(String workspaceEcorePath){

		try{
		Path path = new Path(workspaceEcorePath);
	    IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
	    String osfile = file.getRawLocation().toOSString();
	    
	    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new EcoreResourceFactoryImpl());
    	ResourceSet rs = new ResourceSetImpl();

    	final ExtendedMetaData extendedMetaData = new BasicExtendedMetaData(rs.getPackageRegistry());
    	rs.getLoadOptions().put(XMLResource.OPTION_EXTENDED_META_DATA,extendedMetaData);
	    	
    	URI fileURI=URI.createFileURI(osfile);
    	Resource r = rs.getResource(fileURI, true);
    	EObject eObject = r.getContents().get(0);
    	if (eObject instanceof EPackage) {
	    	    EPackage p = (EPackage)eObject;
	    	    EPackage.Registry.INSTANCE.put(p.getNsURI(), p);
    		}
    	return true;
		}catch (Exception e){
			return false;
		}
	}
	
	/**
	 * Import a ZIP file in the project.
	 * @param myfilepath Absolute path of the Zip file to import 
	 * @param myproject Project name
	 * @return Boolean
	 */
	private Boolean importZip(String myfilepath,String myproject){
		
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(myproject);
		IProgressMonitor progressMonitor = new NullProgressMonitor();
		try {
			project.open(progressMonitor);
			ZipFile zipFile = new ZipFile(myfilepath);
			IOverwriteQuery overwriteQuery = new IOverwriteQuery() {
			    public String queryOverwrite(String file) { return ALL; }
			};
			
			ZipFileStructureProvider structureProvider=	new ZipFileStructureProvider(zipFile);
			ImportOperation op= new ImportOperation(project.getFullPath(), structureProvider.getRoot(), structureProvider, overwriteQuery);
			op.run(new NullProgressMonitor());
			return true;
			} catch (CoreException | IOException | InvocationTargetException | InterruptedException e) {
				return false;
			}

	}

	/**
	  * {@inheritDoc}
	  */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		
		getRepoPreference dialog = new getRepoPreference(window.getShell());
	    dialog.open();
	    
	    boolean confirm =MessageDialog.openConfirm(
	    		window.getShell(),"DICE-LWDG",
	    		"A sample DICER project will be created\n"
	    		+ "This will take a while...");    

		if (confirm){
			
			String dicerProject="Dicer_demo_project";
			String dicerEcore="/Dicer_demo_project/metamodels/ddsm.ecore";
			String dicermodel=dialog.getModels();
			String dicermetamodel=dialog.getMetamodels();
			
				try{
				InputStream ismodel=getGithub(dicermodel);
				
				String models=writeTemp(ismodel);
				
				InputStream ismetamodel=getGithub(dicermetamodel);
				
				String metamodels=writeTemp(ismetamodel);
				
				createProject(dicerProject);
				
				importZip(models,dicerProject);
				
				importZip(metamodels,dicerProject);
				
				registerEcore(dicerEcore);
				
				MessageDialog.openInformation(window.getShell(),"DICE-LWDG","Ready to go!");  
				} catch (Exception e){
				
					String message = e.getMessage() != null ? e.getMessage() : "Ops! Generic error";
					
					MessageDialog.openError(window.getShell(),"DICE-LWDG",message);
					}
		}
		return null;
	}
}
