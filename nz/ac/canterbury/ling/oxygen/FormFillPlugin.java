//
// Copyright 2015-2016 New Zealand Institute of Language, Brain and Behaviour, 
// University of Canterbury
// Written by Robert Fromont - robert.fromont@canterbury.ac.nz
//
//    This file is part of nzilbb/oxygen.
//
//    nzilbb/oxygen is free software; you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation; either version 2 of the License, or
//    (at your option) any later version.
//
//    nzilbb/oxygen is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with nzilbb/oxygen; if not, write to the Free Software
//    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
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
