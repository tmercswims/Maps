import static org.junit.Assert.*;

import org.junit.Test;

import edu.brown.cs032.tmercuri.ja11.maps.gui.LatLngToPixel;


public class LatLngToPixelTest {

	@Test
	public void LatToPixelTest() {
		LatLngToPixel converter = new LatLngToPixel(20, -30);
		assertTrue(converter.LatToPixel(19) == 8000);
		assertTrue(converter.LatToPixel(10) == 80000);
		LatLngToPixel otherconverter = new LatLngToPixel (-20, -30);
		assertTrue(otherconverter.LatToPixel(-21) == 8000);
		assertTrue(otherconverter.LatToPixel(-30) == 80000);
	}
	
	@Test
	public void LngToPixelTest(){
		LatLngToPixel converter = new LatLngToPixel (20, 1);
		assertTrue(converter.LngToPixel(2) == 11000);
		assertTrue(converter.LngToPixel(4) == 33000);
		LatLngToPixel otherConverter = new LatLngToPixel(20, -5);
		assertTrue(otherConverter.LngToPixel(0) == 55000);
		assertTrue(otherConverter.LngToPixel(10) == 165000);
	}

	@Test
	public void pixelToLatTest(){
		LatLngToPixel converter = new LatLngToPixel(30, -40);
		assertTrue(converter.pixelToLat(8000) == 29);
		assertTrue (converter.pixelToLat(16000) == 28);
		LatLngToPixel otherConverter = new LatLngToPixel(10, 30);
		assertTrue(otherConverter.pixelToLat(8000) == 9);
		assertTrue(converter.pixelToLatDistance(8000) == 1);
		assertTrue(converter.pixelToLatDistance(16000) == 2);
	}
	
	@Test
	public void pixelToLngTest(){
		LatLngToPixel converter = new LatLngToPixel(30, -40);
		assertTrue(converter.pixelToLng(11000) == -39);
		assertTrue(converter.pixelToLng(22000) == -38);
		assertTrue(converter.pixelToLngDistance(11000) == 1);
		assertTrue(converter.pixelToLngDistance(22000) == 2);
	}
	
}
