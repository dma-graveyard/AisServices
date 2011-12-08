package dk.dma.aisservices.core.services.ais;

import java.util.ArrayList;
import java.util.List;

public class PastTrack {
	
	private List<PastTrackPoint> points = new ArrayList<PastTrackPoint>();
	
	public PastTrack() {
		
	}
	
	public List<PastTrackPoint> getPoints() {
		return points;
	}
	
	public void setPoints(List<PastTrackPoint> points) {
		this.points = points;
	}

}
