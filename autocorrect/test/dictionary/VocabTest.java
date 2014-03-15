package dictionary;

import static org.junit.Assert.*;

import org.junit.Test;

public class VocabTest {

	@Test
	public void getRootTest() {
		Vocab test = new Vocab();
		try{
		test.addVocab("./test/dictionary/easyfile.txt");
		TreeSearchable root = test.getRoot();
		assertTrue(root.getElement() == '\0');
		assertTrue(root.isWord("this"));
		assertTrue(root.isWord("is"));
		assertTrue(root.isWord("a"));
		assertTrue(root.isWord("test"));
		assertFalse(root.isWord("isn"));
		
		}
		catch(Exception e){
			fail("ERROR: exception while reading files");
		}
	}
	
	@Test
	public void getEntryTest(){
		Vocab test = new Vocab();
		try{
			test.addVocab("./test/dictionary/easyfile.txt");
			assertTrue(test.getEntry("this").getString().equals("this"));
			test.getEntry("is").setTotal(2);
			assertTrue(test.getEntry("is").getUnigramProb()==0.5);
			assertTrue(test.getEntry("isnt") == null);
		}
		catch(Exception e){
			fail("ERROR: Test failed while reading file");
		}
	}
	
	@Test
	public void addVocabTest(){
		Vocab test = new Vocab();
		try{
			test.addVocab("./test/dictionary/hardfile.txt");
			TreeSearchable root = test.getRoot();
			assertTrue(root.isWord("this"));
			assertTrue(test.getEntry("this").getUnigramProb()==(2/30.0));
		}
		catch(Exception e){
			fail("ERROR: test failed while reading file");
		}
	}

}
