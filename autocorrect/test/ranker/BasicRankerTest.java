package ranker;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

import org.junit.Test;

import dictionary.DoubleEntry;
import dictionary.Entry;
import dictionary.Vocab;
import dictionary.WordEntry;

public class BasicRankerTest {

	@Test
	public void IdentityTest() {
		BasicRanker r = new BasicRanker("test");
		assertTrue(r.compare(new WordEntry("test"), new WordEntry("other"))>0);
		assertTrue(r.compare(new WordEntry("test"), new WordEntry("tes"))>0);
		assertTrue(r.compare(new WordEntry("tes"), new WordEntry("test"))<0);
		assertTrue(r.compare(new WordEntry("test"), new WordEntry("test"))==0);
		assertTrue(r.compare(new WordEntry("test"), new WordEntry("tesz"))>0);
	}
	
	@Test
	public void AlphabeticTest(){
		BasicRanker r = new BasicRanker("test");
		assertTrue(r.compare(new WordEntry("alpha"), new WordEntry("beta"))>0);
		assertTrue(r.compare(new WordEntry("gamma"), new WordEntry("beta"))<0);
		assertTrue(r.compare(new WordEntry("zygity"), new WordEntry("whoareyo"))<0);
	}
	
	@Test
	public void UnigramTest(){
		Vocab vocab = new Vocab();
		try {
			vocab.addVocab("./test/ranker/basicrankerdata.txt");
			BasicRanker r = new BasicRanker("im");
			assertTrue(r.compare(vocab.getEntry("you"),vocab.getEntry("test"))<0);
			assertTrue(r.compare(vocab.getEntry("black"), vocab.getEntry("is"))>0);
			assertTrue(r.compare(vocab.getEntry("is"), vocab.getEntry("test"))>0);
		} catch (IOException e) {
			fail("ERROR: while reading file");
		}
	}
	
	@Test
	public void BigramTest(){
		Vocab vocab = new Vocab();
		try{
			vocab.addVocab("./test/ranker/bigramprob.txt");
			BasicRanker r = new BasicRanker("the", "bore");
			assertTrue(r.compare(vocab.getEntry("bore"),vocab.getEntry("the"))>0);
			assertTrue(r.compare(vocab.getEntry("d"), vocab.getEntry("foo"))>0);
			assertTrue(r.compare(vocab.getEntry("d"), vocab.getEntry("book"))>0);
			assertTrue(r.compare(vocab.getEntry("forest"), vocab.getEntry("book"))<0);
			assertTrue(r.compare(vocab.getEntry("horse"), vocab.getEntry("hill"))<0);
			assertTrue(r.compare(vocab.getEntry("horse"), vocab.getEntry("food"))>0);
			DoubleEntry test = new DoubleEntry(vocab.getEntry("foo"), vocab.getEntry("the"));
			assertTrue(r.compare(test, vocab.getEntry("foo"))<0);
			assertTrue(r.compare(test, vocab.getEntry("the"))>0);
			
			Vocab othervocab = new Vocab();
			othervocab.addVocab("./test/ranker/replicator.txt");
			BasicRanker the = new BasicRanker("the");
			assertTrue(the.compare(othervocab.getEntry("the"), othervocab.getEntry("t")) >0);
			assertTrue(the.compare(othervocab.getEntry("the"), othervocab.getEntry("there")) >0);
			assertTrue(the.compare(othervocab.getEntry("there"), othervocab.getEntry("t")) >0);
			assertTrue(the.compare(othervocab.getEntry("the"), new DoubleEntry(othervocab.getEntry("t"), othervocab.getEntry("he"))) >0);
			assertTrue(the.compare(othervocab.getEntry("the"), new DoubleEntry(othervocab.getEntry("t"), othervocab.getEntry("he"))) >0);



		}
		catch(IOException e){
			fail("ERROR: while reading file");
		}
	}

}
