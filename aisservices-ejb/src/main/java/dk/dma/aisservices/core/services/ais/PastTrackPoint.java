package dk.dma.aisservices.core.services.ais;

import java.util.Date;

import dk.dma.aisservices.core.domain.AisVesselTrack;

public class PastTrackPoint {
	
	private double lat;
	private double lon;
	private double cog;
	private double sog;
	private Date time;
	
	public PastTrackPoint() {
		
	}

	public PastTrackPoint(AisVesselTrack aisVesselTrack) {
		this.lat = aisVesselTrack.getLat();
		this.lon = aisVesselTrack.getLon();
		this.cog = aisVesselTrack.getCog();
		this.sog = aisVesselTrack.getSog();
		this.time = aisVesselTrack.getTime();
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getCog() {
		return cog;
	}

	public void setCog(double cog) {
		this.cog = cog;
	}

	public double getSog() {
		return sog;
	}

	public void setSog(double sog) {
		this.sog = sog;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
	
}
