//
// Copyright 2015 New Zealand Institute of Language, Brain and Behaviour, 
// University of Canterbury
// Written by Robert Fromont - robert.fromont@canterbury.ac.nz
//
//    This file is part of LaBB-CAT.
//
//    LaBB-CAT is free software; you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation; either version 2 of the License, or
//    (at your option) any later version.
//
//    LaBB-CAT is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with LaBB-CAT; if not, write to the Free Software
//    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//

package nz.ac.canterbury.ling.oxygen;

import java.applet.*;
import javax.swing.*;
import java.lang.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.regex.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.util.jar.*;
import java.util.zip.*;
import java.io.*;

/**
 * Utility class for extracting the contents of it's own jar file - for file updates.  Basically like a cut down auto-extracter.
 * @author Robert Fromont
 */
public class InstallPlugin
   extends JPanel
   implements ActionListener, WindowListener, Runnable
{
   static
   {
      try 
      {
	 UIManager.setLookAndFeel(
	    UIManager.getSystemLookAndFeelClassName());
      } 
      catch (Exception e) 
      {
	 System.err.println("Could not set Look and Feel: " + e.getMessage());
      }
   }
   
   JFrame frame_;
   
   /**
    * Main entrypoint if run as an application.
    */
   public static void main(String argv[])
   {
      InstallPlugin application = new InstallPlugin();
      String sTitle = "Oxygen Plugin installer";
      try
      {
	 // if possible, put the name of the jar file in the title
	 URL url = application.getClass().getResource(
	    application.getClass().getName() + ".class");
	 Matcher matcher = Pattern.compile(".*/([^/]*)\\.jar.*")
	    .matcher(url.toString());
	 if (matcher.find()) sTitle = matcher.group(1);
      }
      catch(Throwable exception) {}
      
      application.frame_ = new JFrame(sTitle);
      application.frame_.addWindowListener(application);
      
      application.init();
      
      Toolkit toolkit = application.frame_.getToolkit();
      Dimension screenSize = toolkit.getScreenSize();
      int width = 500;
      int height = 400;
      int top = (screenSize.height - height) / 2;
      int left = (screenSize.width - width) / 2;
      // icon
      try
      {
	 URL imageUrl = application.getClass().getResource("/images/UnJar.png");
	 if (imageUrl != null)
	 {
	    application.frame_.setIconImage(toolkit.createImage(imageUrl));
	 }
      }
      catch(Exception exception)
      {}
      application.frame_.getContentPane().add("Center", application);
      application.frame_.setSize(width, height);
      application.frame_.setLocation(left, top);
      application.frame_.setVisible(true);
      
      Thread thread = new Thread(application);
      thread.start();
   } // end of main
   
   // Attributes:
   JTextArea txtStatus = new JTextArea();
   JProgressBar pb = new JProgressBar();
   boolean bUpgrade = false;
   
   // Methods:
   
   /**
    * Initialise the applet.
    */
   public void init()
   {
      setBackground(Color.lightGray);
      setLayout(new BorderLayout());
      
      add(new JScrollPane(txtStatus), BorderLayout.CENTER);
      add(pb, BorderLayout.SOUTH);
   } // end of init()
   
   
   /**
    * Displays a status message.
    * @param sMessage
    */
   public void message(String sMessage)
   {
      txtStatus.append(sMessage + "\n");
      txtStatus.setCaretPosition(txtStatus.getText().length());	 
   } // end of message()
   
   /**
    * Displays an error message.
    * @param sMessage
    */
   public void error(String sMessage)
   {
      message("ERROR: " + sMessage);
   } // end of message()
   
   
   /**
    * Runnable implementation.
    */
   public void run()
   {
      try
      {
	 // are we in a jar file?
	 URL url = getClass().getResource("/plugin.xml");
	 String sUrl = url.toString();
	 if (!sUrl.startsWith("jar:"))
	 {
	    message("InstallPlugin must be run from within a jar archive file.");
	 }
	 else
	 {
	    int iUriStart = 4;
	    int iUriEnd = sUrl.indexOf("!");
	    String sFileUri = sUrl.substring(iUriStart, iUriEnd);
	    File fJar = new File(new URI(sFileUri));
	    File root = fJar.getParentFile();
	    message(fJar.getPath());
	    JarFile jfJar = new JarFile(fJar);
	    // list entries:
	    pb.setMaximum(jfJar.size());
	    pb.setValue(0);
	    
	    Enumeration<JarEntry> enEntries = jfJar.entries();
	    while (enEntries.hasMoreElements())
	    {
	       JarEntry jeEntry = enEntries.nextElement();
	       if (jeEntry.getName().equals("plugin.xml"))
	       {
		  if (!jeEntry.isDirectory())
		  {
		     message(jeEntry.getName());
		     
		     File parent = root;
		     String sFileName = jeEntry.getName();
		     StringTokenizer stPathParts = new StringTokenizer(jeEntry.getName(), "/");
		     if (stPathParts.countTokens() > 1)
		     { // complex path
			// ensure that the required directories exist
			sFileName = stPathParts.nextToken();
			
			while(stPathParts.hasMoreTokens())
			{
			   // previous token was not the last, so it
			   // must be a directory
			   parent = new File(parent, sFileName);
			   if (!parent.exists())
			   {
			      message("Creating " + parent.getPath());
			      parent.mkdir();
			   }

			   sFileName = stPathParts.nextToken();
			} // next token
		     }
		     File file = new File(parent, sFileName);
		     
		     // get input stream
		     InputStream in = jfJar.getInputStream(jeEntry);
		     
		     // get output stream
		     FileOutputStream out = new FileOutputStream(file);
		     
		     // pump data from one stream to the other
		     byte[] buffer = new byte[1024];
		     int bytesRead = in.read(buffer);
		     while(bytesRead >= 0)
		     {
			out.write(buffer, 0, bytesRead);
			bytesRead = in.read(buffer);
		     } // next chunk of data
		     
		     // close streams
		     in.close();
		     out.close();
		     
		  }
	       }
	       pb.setValue(pb.getValue() + 1);
	    } // next entry
	       
	    pb.setValue(jfJar.size());
	    message("File extraction complete.");
	 }
      }
      catch(Exception ex)
      {
	 error(ex.getClass().getName() + ": " + ex.getMessage());
	 ex.printStackTrace();
      }
      
   } // end of run()
   
   
   /**
    * Action listener method.
    */
   public void actionPerformed(ActionEvent event) 
   {
      String command = event.getActionCommand();
   } // end of actionPerformed()
   
   // WindowListener methods:
   
   /**
    * Called when the user tries to close the window.
    */
   public void windowClosing(WindowEvent e) 
   {
      exit();
   } // end of windowClosing()
   
   /**
    * Called when the window is brought to the front.
    */
   public void windowActivated(WindowEvent e) 
   {} // end of windowActivated()
   
   /**
    * Called when the window closes.
    */
   public void windowClosed(WindowEvent e)
   {}  // end of windowClosed()
   
   /**
    * Called when the window is no longer at the front.
    */
   public void windowDeactivated(WindowEvent e)
   {} // end of windowDeactivated()
   
   /**
    * Called when the window is brought up from an icon.
    */
   public void windowDeiconified(WindowEvent e)
   {}  // end of windowDeiconified()
   
   /**
    * Called when the window is iconified.
    */
   public void windowIconified(WindowEvent e)
   {} // end of windowIconified()
   
   /**
    * Called when the winodw is first opened.
    */
   public void windowOpened(WindowEvent e) 
   {} // end of windowOpened()
   
   /**
    * Exists the java applet.
    */
   public boolean exit()
   {
      System.exit(0);
      return true;
   } // end of exit()
   private static final long serialVersionUID = 1;
} // end of class UnJar
