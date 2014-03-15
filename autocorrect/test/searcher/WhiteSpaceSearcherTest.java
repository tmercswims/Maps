package searcher;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

import org.junit.Test;

import dictionary.Entry;
import dictionary.Vocab;

public class WhiteSpaceSearcherTest {

	@Test
	public void findTest() {
		Vocab vocab = new Vocab();
		try {
			vocab.addVocab("./test/searcher/whitespace.txt");
			Searcher test = new WhiteSpaceSearcher(vocab);
			Collection<Entry> results = test.find("bigger");
			String resultString = "";
			for (Entry i: results){
				resultString +=i.getString();
			}
			assertTrue(resultString.equals("big ger"));
		} catch (FileNotFoundException e) {
			fail("ERROR: oops");
		} catch (IOException e) {
			fail("ERROR: oops");
		}
	}

}
