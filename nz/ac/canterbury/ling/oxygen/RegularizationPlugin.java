package nz.ac.canterbury.ling.oxygen;

import ro.sync.exml.plugin.Plugin;
import ro.sync.exml.plugin.PluginDescriptor;

public class RegularizationPlugin extends Plugin 
{
   /**
    * Plugin instance.
    */
   private static RegularizationPlugin instance = null;  
   /**
    * RegularizationPlugin constructor.
    * 
    * @param descriptor Plugin descriptor object.
    */
   public RegularizationPlugin(PluginDescriptor descriptor) 
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
   public static RegularizationPlugin getInstance() 
   {
      return instance;
   }
}
