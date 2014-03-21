package edu.brown.cs032.ja11.autocorrect.dictionary;

import java.util.Collection;

/**
 *
 * @author Thomas Mercurio
 */
public interface TreeSearchable {

    /**
     *
     * @return
     */
    public char getElement();

    /**
     *
     * @param word
     * @return
     */
    public boolean isWord(String word);

    /**
     *
     * @return
     */
    public boolean isWordEnd();

    /**
     *
     * @param element
     * @return
     */
    public TreeSearchable getNode(char element);

    /**
     *
     * @return
     */
    public Collection<TreeSearchable> getNodes();

    /**
     *
     * @param word
     */
    public void addWord(String word);

    /**
     *
     * @param route
     * @return
     */
    public TreeSearchable searchNode(String route);

}
