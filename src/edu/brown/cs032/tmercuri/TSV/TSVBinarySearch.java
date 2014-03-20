/*
 * Thomas Mercurio, tmercuri
 * CS032, Spring 2014
 */

package edu.brown.cs032.tmercuri.TSV;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * A class to binary search on TSV files.
 * @author Thomas Mercurio
 */
public class TSVBinarySearch {
    private final RandomAccessFile raf;
    private final Map<String, Integer> columns;
    private List<String> firstLine;
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private static final int BUFFER = 4096;
    
    /**
     * New BinarySearchTSV, for the given file.
     * @param filename the file to use for lookups
     * @throws FileNotFoundException if the file does not exist
     * @throws IOException if the file cannot be read
     */
    public TSVBinarySearch(String filename) throws FileNotFoundException, IOException {
        // initialize things
        this.raf = new RandomAccessFile(filename, "r");
        this.columns = new HashMap<>();
        
        // parse the headers
        getHeaders(filename);
    }
    
    private void getHeaders(String filename) throws FileNotFoundException, IOException {
        String fL;
        // scan in the first line of the file
        try (Scanner s = new Scanner(new File(filename), "UTF-8")) {
            fL = s.hasNextLine() ? s.nextLine() : null;
        }
        // empty file
        if (fL == null) {
            throw new IOException("bad file");
        }
        
        
        String[] headers = fL.split("\t");
        this.firstLine = Arrays.asList(headers);
        
        // map each header string to its column number
        for (int i=0; i<headers.length; i++) {
            if (!headers[i].equals("")) {
                columns.put(headers[i], i);
            }
        }
    }
    
    /**
     * Looks up a query in the RandomAccessFile using a binary search. It is assumed that the TSV file is sorted based on the column that query is a member of.
     * @param query the string we are searching for
     * @param giveField the column it will be in
     * @param wantField the column that we want back
     * @return the string in column wantField of the row that query is in giveField
     * @throws IllegalArgumentException if either field is not part of the file
     * @throws IOException if the file cannot be read
     */
    public String lookup(String query, String giveField, String wantField) throws IllegalArgumentException, IOException {
        // get column numbers
        Integer giveColumn = columns.get(giveField);
        Integer wantColumn = columns.get(wantField);
        if (giveColumn == null || wantColumn == null) {
            throw new IllegalArgumentException("no such field: " + giveField);
        }
        
        // seek to start
        raf.seek(0);
        
        // start with the whole file as bounds
        long top = 0;
        long bot = raf.length();
        // as long as we have an area to search in
        while (top <= bot) {
            // better than (top+bot)/2, to prevent overflow
            long mid = (top+bot)>>>1;
            // go to the middle
            raf.seek(mid);
            
            // try to go the beginning of the next line
            seekToNextNewLine();
            // if that is the end of the file...
            if (raf.getFilePointer() == raf.length()) {
                // ...go back to beginning of the last line
                seekToPrevNewLine();
                seekToPrevNewLine();
            }
            
            // see if this line contains query in giveColumn
            String res = checkLine(query, giveColumn);
            int cmp = res.compareTo(query);
            // it does
            if (cmp == 0) {
                // get back to the beginning of the line
                seekToPrevNewLine();
                // for duplicate keys
                seekToPrevNewLine();
                for (String before = checkLine(query, giveColumn); before.equals(query); before = checkLine(query, giveColumn)) {
                    seekToPrevNewLine();
                    seekToPrevNewLine();
                }
                seekToNextNewLine();
                String toRet = "";
                while (query.equals(checkLine(query, giveColumn))) {
                    toRet += getWant(wantColumn);
                    toRet += ",";
                    seekToNextNewLine();
                }
                return toRet.substring(0, toRet.length() - 1);
            } // we are too high; new top is the mid
            else if (cmp < 0) {
                top = mid+1;
            } // we are too low; new bot is the mid
            else if (cmp > 0) {
                bot = mid-1;
            }
        }
        // search falls through if bounds are reduced to nothing, which means the query was not in the file
        return null;
    }
    
    public List<List<String>> getAllBetween(String topBound, String botBound, String keyField) throws IOException {
        Integer keyColumn = columns.get(keyField);
        List<List<String>> linesToReturn = new ArrayList<>();
        linesToReturn.add(firstLine);
        
        // seek to start
        raf.seek(0);
        
        // start with the whole file as bounds
        long top = 0;
        long bot = raf.length();
        // as long as we have an area to search in
        while (top <= bot) {
            // better than (top+bot)/2, to prevent overflow
            long mid = (top+bot)>>>1;
            // go to the middle
            raf.seek(mid);
            
            // try to go the beginning of the next line
            seekToNextNewLine();
            // if that is the end of the file...
            if (raf.getFilePointer() == raf.length()) {
                // ...go back to beginning of the last line
                seekToPrevNewLine();
                seekToPrevNewLine();
            }
            
            // see if this line contains query in giveColumn
            String res = checkLine(topBound, keyColumn);
            int cmp = res.compareTo(topBound);
            // it does
            if (cmp == 0) {
                // get back to the beginning of the line
                seekToPrevNewLine();
                // for duplicate keys
                seekToPrevNewLine();
                for (String before = checkLine(topBound, keyColumn); before.equals(topBound); before = checkLine(topBound, keyColumn)) {
                    seekToPrevNewLine();
                    seekToPrevNewLine();
                }
                seekToNextNewLine();
                while (botBound.compareTo(checkLine(botBound, keyColumn)) >= 0) {
                    seekToPrevNewLine();
                    linesToReturn.add(Arrays.asList(readToNextNewLine().trim().split("\t")));
                }
                return linesToReturn;
            } // we are too high; new top is the mid
            else if (cmp < 0) {
                top = mid+1;
            } // we are too low; new bot is the mid
            else if (cmp > 0) {
                bot = mid-1;
            }
        }
        // search falls through if bounds are reduced to nothing, which means the query was not in the file
        return null;
    }
    
    public String getColumnOnFirstLine(String column) throws IOException {
        Integer columnNum = columns.get(column);
        raf.seek(0);
        
        seekToNextNewLine();
        
        for (int t=0; t<columnNum;) {
            int r = raf.read();
            if (r == '\t') {
                t++;
            }
        }
        
        byte[] line = new byte[BUFFER];
        
        int i = 0;
        for (int r=raf.read(); r!='\t'&&r!='\n'; r=raf.read()) {
            // found EOF
            if (r == -1) {
                // seek to end of file
                raf.seek(raf.length());
                break;
            }
            line[i] = (byte) r;
            i++;
        }
        
        return new String(line, UTF8).trim();
    }
    
    public String getColumnOnLastLine(String column) throws IOException {
        int columnNum = columns.get(column);
        raf.seek(raf.length());
        
        seekToPrevNewLine();
        
        for (int t=0; t<columnNum;) {
            int r = raf.read();
            if (r == '\t') {
                t++;
            }
        }
        
        byte[] line = new byte[BUFFER];
        
        int i = 0;
        for (int r=raf.read(); r!='\t'&&r!='\n'; r=raf.read()) {
            // found EOF
            if (r == -1) {
                // seek to end of file
                raf.seek(raf.length());
                break;
            }
            line[i] = (byte) r;
            i++;
        }
        
        return new String(line, UTF8).trim();
    }
    
    private String readToNextNewLine() throws IOException {
        byte[] line = new byte[2*BUFFER];
        
        int i = 0;
        for (int r=raf.read(); r!='\n'; r=raf.read()) {
            // found EOF
            if (r == -1) {
                // seek to end of file
                raf.seek(raf.length());
                break;
            }
            line[i] = (byte) r;
            i++;
        }
        
        return new String(line, UTF8);
        
    }
    
    private void seekToNextNewLine() throws IOException {
        // read one byte at a time until we find a newline or EOF
        for (int r=raf.read(); r!='\n'; r=raf.read()) {
            // found EOF
            if (r == -1) {
                // seek to end of file
                raf.seek(raf.length());
                break;
            }
        }
    }
    
    private void seekToPrevNewLine() throws IOException {
        raf.seek(raf.getFilePointer()-2);
        // read backwards one byte at a time, until we find a newline or hit the start of the file
        for (int r=raf.read(); r!='\n'; r=raf.read()) {
            // move back two, one for the read plus one more
            if (raf.getFilePointer() > 1) {
                raf.seek(raf.getFilePointer()-2);
            } // at the start of the file
            else {
                // seek to start
                raf.seek(0);
                break;
            }
        }
    }
    
    private String checkLine(String query, int giveColumn) throws IOException {
        // read tabs to get to the column we want
        for (int t=0; t<giveColumn;) {
            int r = raf.read();
            if (r == '\t') {
                t++;
            }
        }
        // new byte array to (maybe) hold query
        byte[] b = new byte[query.length()];
        int numRead = raf.read(b);
        // did not read enough
        if (numRead != b.length) {
            return null;
        }
        // read enough, return it as a UTF-8 string
        return new String(b, UTF8);
    }
    
    private String getWant(int wantColumn) throws IOException {
        // read tabs to get the column we want
        for (int t=0; t<wantColumn;) {
            int r = raf.read();
            if (r == '\t') {
                t++;
            }
        }
        // new byte array to hold whatever it is we are looking up
        byte[] b = new byte[BUFFER];
        int i=0;
        // read until we hit a tab or newline of some kind
        for (int r = raf.read(); r!='\t'&&r!='\r'&&r!='\n'&&r!=-1; r=raf.read()) {
            b[i] = (byte) r;
            i++;
        }
        // return this as a UTF-8 string
        return new String(b, UTF8).trim();
    }

    /**
     * Closes this RandomAccessFile. Object is unusable after this is called.
     * @throws IOException if the file cannot be accessed
     */
    public void close() throws IOException {
        raf.close();
    }
}
