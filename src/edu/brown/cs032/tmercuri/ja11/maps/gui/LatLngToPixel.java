
public class LatLngToPixel {
	
	private double latScale = 8000;
	private double lngScale = 110000;
	private double lowLat;
	private double lowLng;
	
	
	public LatLngToPixel (double lowLat, double lowLng){
		this.lowLat = lowLat;
		this.lowLng = lowLng;
	}
	
	public int LatToPixel (double  lat){
		double distance = lat - lowLat;
		int result =  (int) (distance * latScale);
		return result;
	}
	
	public int LngToPixel (double lng){
		double distance = lng - lowLng;
		int result = (int) (distance *lngScale);
		return result;
	}
	
	public double pixelToLat(int pixel){
		double distance = pixel/ latScale;
		return (lowLat+ distance);
	}
	
	public double pixeltoLng(int pixel){
		double distance = pixel/lngScale;
		return (lowLng + distance);
	}
	
	
}
