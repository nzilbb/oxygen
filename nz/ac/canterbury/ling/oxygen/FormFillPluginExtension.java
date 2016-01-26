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

import ro.sync.exml.plugin.document.*;
import javax.swing.JOptionPane;
import javax.swing.text.*;
import javax.swing.*;
import java.util.regex.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class FormFillPluginExtension implements DocumentPluginExtension 
{
   /**
    * Scan document looking for form fields to present to the user, so they can add the values to the document.
    *
    * @param  context  Document context.
    * @return          Document with form field markers substituted for the values entered by the user.
    */
   public DocumentPluginResult process(DocumentPluginContext context) 
   {
      // get the document
      Document doc = context.getDocument();
      try
      {
	 String text = doc.getText(0, doc.getLength());
	 
	 // find form fields in it
	 final LinkedHashMap<String, JTextField> fields = new LinkedHashMap<String, JTextField>();
	 Pattern fieldPattern = Pattern.compile("\\[([^\\[]+)\\]");
	 Matcher fieldMatcher = fieldPattern.matcher(text);	 
	 while (fieldMatcher.find())
	 {
	    // add field to the list
	    // default value ins the field name itself, enclosed in [], so that if they don't
	    // change it, the field remains for future invocations.
	    fields.put(fieldMatcher.group(1), new JTextField(fieldMatcher.group(0)));
	 } // next match
	 
	 if (fields.size() == 0)
	 {
	    JOptionPane.showMessageDialog(
	       context.getFrame(), "There are no fields to fill in", "Set Field Values",
	       JOptionPane.INFORMATION_MESSAGE);
	 }
	 else
	 {
	    // present a form to the user
	    JPanel pnlFields = new JPanel(new GridLayout(fields.size(), 2));
	    // when the value box is focussed, we want to select all text
	    FocusListener selectAll = new FocusAdapter()
	       {
		  public void focusGained(FocusEvent e)
		  {
		     ((JTextField)e.getComponent()).selectAll();
		  }
	       };
	    for (String name : fields.keySet())
	    {
	       pnlFields.add(new JLabel(name, SwingConstants.RIGHT));
	       pnlFields.add(fields.get(name));
	       fields.get(name).addFocusListener(selectAll);
	    } // next field
	    JPanel pnlButtons = new JPanel(new FlowLayout());
	    JButton btnOk = new JButton("OK");
	    pnlButtons.add(btnOk);
	    JButton btnCancel = new JButton("Cancel");
	    pnlButtons.add(btnCancel);
	    
	    final JDialog dialog = new JDialog(context.getFrame(), "Set Field Values", true);
	    dialog.setSize(new Dimension(500, (fields.size() + 1) * 30));
	    Dimension screenSize = dialog.getToolkit().getScreenSize();
	    dialog.setLocation((screenSize.width - dialog.getWidth()) / 2, 
			       (screenSize.height - dialog.getHeight()) / 2);
	    dialog.setLayout(new BorderLayout());
	    dialog.add(pnlFields, BorderLayout.CENTER);
	    dialog.add(pnlButtons, BorderLayout.SOUTH);
	    btnOk.addActionListener(new ActionListener()
	       {
		  public void actionPerformed(ActionEvent e)
		  {
		     dialog.setVisible(false);
		  }
	       });
	    btnCancel.addActionListener(new ActionListener()
	       {
		  public void actionPerformed(ActionEvent e)
		  {
		     fields.clear();
		     dialog.setVisible(false);
		  }
	       });
	    dialog.setVisible(true);
	    
	    // alter the document to insert the values
	    fieldMatcher.reset();
	    int iTextOffset = 0;
	    while (fieldMatcher.find())
	    {
	       if (fields.containsKey(fieldMatcher.group(1)))
	       {
		  String name = fieldMatcher.group(0);
		  String value = fields.get(fieldMatcher.group(1)).getText();
		  // remove field
		  doc.remove(iTextOffset + fieldMatcher.start(), name.length());
		  // insert value
		  doc.insertString(iTextOffset + fieldMatcher.start(), value, null);
		  // ensure future replacements aren't displaced by this one
		  iTextOffset += value.length() - name.length();
	       } // next match
	    }
	 } // there are fields	 
      }
      catch(BadLocationException exception)
      {}

      return new DocumentPluginResultImpl(doc);
   }
}
