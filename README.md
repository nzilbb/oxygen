# oxygen plugins

These are some plugin for the Oxygen XML editor (https://www.oxygenxml.com/).

To install a plugin:
1. download its jar file
2. Find the folder where Oxygen is installed - something like C:\Program Files\Oxygen XML Editor
3. In that folder, there's a subfolder called plugins
4. In the plugins subfolder, create another folder named after the plugin, e.g. "formfill"
5. Copy the jar file you downloaded into the new folder you just created
6. Double-click on it.

This should open a window that has a message saying "File extraction complete" and in the folder you created, there should be a new file called plugin.xml next to the jar file.

Then start up Oxygen and thr plugin should be available.

## formfill

This plugin allows standard fields for data entry to be included in an XML template, so they can be filled in for each document just by filling in a form.

When you hit [ctrl]+[alt]+[H], the document is scanned, looking for form fields - anything enclosed in square brackets [] - to present to the user, so they can add the values to the document.

## teiregularization

This plugin makes TEI regularization tag constructions easy to enter.

To use the plugin, select the 'original' text you want to regularize, and hit [ctrl]+[R]. You will be asked for the 'regular form'. When you enter it, the selection will be wrapped in a TEI construction like this:

&lt;choice&gt;&lt;orig&gt;{what you selected}&lt;/orig&gt;&lt;reg&gt;{regular form you entered}&lt;/reg&gt;&lt;/choice&gt;
