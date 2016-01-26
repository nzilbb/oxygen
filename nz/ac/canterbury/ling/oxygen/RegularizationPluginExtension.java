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
