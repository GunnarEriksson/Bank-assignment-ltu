/**
 * Programmer: Gunnar Eriksson, guneri-5@student.ltu.se
 * Date: 2015-12-20
 * Last Updated: 2015-12-20, Gunnar Eriksson
 * Description: File format filter types that can be used
 * Version: First version
 */
package se.gunnareriksson.bank.gui;

import javax.swing.filechooser.FileNameExtensionFilter;

public enum FileFormatFilter
{
    TEXT_FILE(new FileNameExtensionFilter("Textfil", "txt"));
    
    private FileNameExtensionFilter fileFilter;
    
    /**
     * Help constructor
     * Connects the file name extension filter to the key
     * 
     * @param fileFilter
     */
    private FileFormatFilter(FileNameExtensionFilter fileFilter)
    {
        this.fileFilter = fileFilter;
    }
    
    /**
     * Gets the file name extension filter
     * 
     * @return - the file name extension filter
     */
    public FileNameExtensionFilter getFilter()
    {
        return fileFilter;
    }
}
