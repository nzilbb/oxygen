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

import ro.sync.exml.plugin.selection.SelectionPluginContext;
import ro.sync.exml.plugin.selection.SelectionPluginExtension;
import ro.sync.exml.plugin.selection.SelectionPluginResult;
import ro.sync.exml.plugin.selection.SelectionPluginResultImpl;

public class RegularizationPluginExtension implements SelectionPluginExtension 
{
   /**
    * Mark original text with a regularized alternative.
    *
    * @param  context  Selection context.
    * @return          Selection wrapped in an &lt;orig&gt; with &lt;reg&gt; alternative, wrappted in a &lt;choice&gt; tag.
    */
   public SelectionPluginResult process(SelectionPluginContext context) 
   {
      return new SelectionPluginResultImpl(
	 "<choice><orig>${selection}</orig><reg>${ask('Regular form', generic, '"+context.getSelection().replaceAll("'","")+"')}</reg></choice>");
   }
}
