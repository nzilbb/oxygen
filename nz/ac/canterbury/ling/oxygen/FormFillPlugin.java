package nz.ac.canterbury.ling.oxygen;

import ro.sync.exml.plugin.Plugin;
import ro.sync.exml.plugin.PluginDescriptor;

public class FormFillPlugin extends Plugin 
{
   /**
    * Plugin instance.
    */
   private static FormFillPlugin instance = null;  
   /**
    * FormFillPlugin constructor.
    * 
    * @param descriptor Plugin descriptor object.
    */
   public FormFillPlugin(PluginDescriptor descriptor) 
   {
      super(descriptor);
      
      if (instance != null) 
      {
	 throw new IllegalStateException("Already instantiated !");
      }    
      instance = this;
   }
   
   /**
    * Get the plugin instance.
    * 
    * @return the shared plugin instance.
    */
   public static FormFillPlugin getInstance() 
   {
      return instance;
   }
}
