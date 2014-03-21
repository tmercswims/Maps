/*
 * Thomas Mercurio, tmercuri
 * CS032, Spring 2014
 */

package edu.brown.cs032.tmercuri.TSV;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Thomas Mercurio
 */
public class TSVReader implements AutoCloseable {
    
    private Scanner file;
    private final String filename;
    private final Map<String, Integer> columns;
    
    /**
     *
     * @param filename
     * @throws FileNotFoundException
     * @throws IOException
     */
    public TSVReader(String filename) throws FileNotFoundException, IOException {
        this.file = new Scanner(new File(filename), "UTF-8");
        this.filename = filename;
        this.columns = getHeaders();
    }
    
    private Map<String, Integer> getHeaders() throws IOException {
        Map<String, Integer> map = new HashMap<>();
        
        // scan in the first line of the file
        String[] headers = file.hasNextLine() ? file.nextLine().split("\t") : null;
        // empty file
        if (headers == null) {
            throw new IOException("bad file");
        }
        
        // map each header string to its column number
        for (int i=0; i<headers.length; i++) {
            if (!headers[i].equals("")) {
                map.put(headers[i], i);
            }
        }
        
        return map;
    }
    
    /**
     *
     * @param columnName
     * @return
     * @throws FileNotFoundException
     */
    public List<String> getAllEntriesInColumn(String columnName) throws FileNotFoundException {
        List<String> list = new ArrayList<>();
        
        Integer colNum = columns.get(columnName);
        
        // column is not in this file
        if (colNum == null) {
            throw new IllegalArgumentException("no such column in file: " + columnName);
        }
        
        String[] line;
        while (file.hasNextLine()) {
            line = file.nextLine().split("\t");
            list.add(line[colNum]);
        }
        
        // reset the Scanner, so this process can be carried out again
        file.close();
        file = new Scanner(new File(filename), "UTF-8");
        file.nextLine();
        
        return list;
    }

    @Override
    public void close() throws IOException {
        file.close();
    }
}
