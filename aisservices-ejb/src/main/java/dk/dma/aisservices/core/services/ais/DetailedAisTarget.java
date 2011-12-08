package dk.dma.aisservices.core.services.ais;

import java.util.Date;

import dk.dma.aisservices.core.domain.AisClassAPosition;
import dk.dma.aisservices.core.domain.AisClassAStatic;
import dk.dma.aisservices.core.domain.AisVesselPosition;
import dk.dma.aisservices.core.domain.AisVesselStatic;
import dk.dma.aisservices.core.domain.AisVesselTarget;
import dk.frv.ais.message.NavigationalStatus;

public class DetailedAisTarget {
	
	protected long id;
	protected long mmsi;
	protected String vesselClass;
	protected long lastReceived;
	protected long currentTime;
	protected double lat;
	protected double lon;
	protected double cog;
	protected boolean moored;
	protected String vesselType;
	protected short length;
	protected byte width;
	protected double sog;
	protected String name;
	protected String callsign;
	protected String imoNo;
	protected String cargo;
	protected String country;
	protected Double draught;
	protected Double heading;
	protected Double rot;
	protected String destination;
	protected String navStatus;
	protected Date eta;
	protected byte posAcc;
	protected String source;
	protected PastTrack pastTrack = null;	

	public DetailedAisTarget() {

	}

	public void init(AisVesselTarget aisVessel) {
		AisVesselStatic aisVesselStatic = aisVessel.getAisVesselStatic();
		if (aisVesselStatic == null) return;
		AisVesselPosition aisVesselPosition = aisVessel.getAisVesselPosition();
		if (aisVesselPosition == null) return;
		AisClassAPosition aisClassAPosition = aisVesselPosition.getAisClassAPosition();
		AisClassAStatic aisClassAStatic = aisVesselStatic.getAisClassAStatic();		
		
		this.length = (short) (aisVesselStatic.getDimBow() + aisVesselStatic.getDimStern());
		this.width = (byte) (aisVesselStatic.getDimPort() + aisVesselStatic.getDimStarboard());		
		
		this.currentTime = System.currentTimeMillis();
		this.id = aisVessel.getId();
		this.mmsi = aisVessel.getMmsi();
		this.vesselClass = aisVessel.getVesselClass();
		this.lastReceived = ((currentTime - aisVessel.getLastReceived().getTime()) / 1000);
		this.lat = aisVesselPosition.getLat();
		this.lon = aisVesselPosition.getLon();
		this.cog = aisVesselPosition.getCog();
		this.heading = aisVesselPosition.getHeading();
		this.sog = aisVesselPosition.getSog(); 		
		this.vesselType = aisVesselStatic.getShipTypeCargo().prettyType();
		
		this.name = aisVesselStatic.getName();
		this.callsign = aisVesselStatic.getCallsign();
		this.cargo = aisVesselStatic.getShipTypeCargo().prettyCargo();
		
		this.source = aisVessel.getSource();

		// Class A statics
		if (aisClassAStatic != null) {
			this.imoNo = Integer.toString(aisClassAStatic.getImo());
			this.destination = aisClassAStatic.getDestination();
			this.draught = (double)aisClassAStatic.getDraught();
			this.eta = aisClassAStatic.getEta();			
		}		
		
		// Class A position
		if (aisClassAPosition != null) {
			NavigationalStatus navigationalStatus = new NavigationalStatus(aisClassAPosition.getNavStatus());
			this.navStatus = navigationalStatus.prettyStatus();
			this.moored = (aisClassAPosition.getNavStatus() == 1 || aisClassAPosition.getNavStatus() == 5);
		}
		
		// Determine country TODO move to DB
//		String str = Long.toString(mmsi);
//		if (str.length() > 3) {
//			str = str.substring(0, 3);
//			MidCountry midCountry = CountryMapper.getInstance().getByMid(Integer.parseInt(str)); 
//			if (midCountry != null) {
//				country = midCountry.getName();
//			}
//		}
		
		if (aisVesselPosition.getHeading() != null) {
			this.heading = aisVesselPosition.getHeading();
		}
		
		this.posAcc = aisVesselPosition.getPosAcc();
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCallsign() {
		return callsign;
	}

	public void setCallsign(String callsign) {
		this.callsign = callsign;
	}

	public String getImoNo() {
		return imoNo;
	}

	public void setImoNo(String imoNo) {
		this.imoNo = imoNo;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Double getDraught() {
		return draught;
	}

	public void setDraught(Double draught) {
		this.draught = draught;
	}

	public Double getHeading() {
		return heading;
	}

	public void setHeading(Double heading) {
		this.heading = heading;
	}

	public Double getRot() {
		return rot;
	}

	public void setRot(Double rot) {
		this.rot = rot;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getNavStatus() {
		return navStatus;
	}

	public void setNavStatus(String navStatus) {
		this.navStatus = navStatus;
	}

	public Date getEta() {
		return eta;
	}

	public void setEta(Date eta) {
		this.eta = eta;
	}

	public byte getPosAcc() {
		return posAcc;
	}

	public void setPosAcc(byte posAcc) {
		this.posAcc = posAcc;
	}
	
	public long getMmsi() {
		return mmsi;
	}
	
	public void setMmsi(long mmsi) {
		this.mmsi = mmsi;
	}
	
	public String getVesselClass() {
		return vesselClass;
	}
	
	public void setVesselClass(String vesselClass) {
		this.vesselClass = vesselClass;
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

	public void setCog(double hdg) {
		this.cog = hdg;
	}

	public boolean isMoored() {
		return moored;
	}
	
	public void setMoored(boolean moored) {
		this.moored = moored;
	}

	public String getVesselType() {
		return vesselType;
	}

	public void setVesselType(String vesselType) {
		this.vesselType = vesselType;
	}

	public short getLength() {
		return length;
	}

	public void setLength(short length) {
		this.length = length;
	}

	public double getSog() {
		return sog;
	}

	public void setSog(double sog) {
		this.sog = sog;
	}

	public long getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
	}

	public long getLastReceived() {
		return lastReceived;
	}

	public void setLastReceived(long lastReceived) {
		this.lastReceived = lastReceived;
	}

	public byte getWidth() {
		return width;
	}

	public void setWidth(byte width) {
		this.width = width;
	}
	
	public String getSource() {
		return source;
	}
	
	public void setSource(String source) {
		this.source = source;
	}
	
	public PastTrack getPastTrack() {
		return pastTrack;
	}
	
	public void setPastTrack(PastTrack pastTrack) {
		this.pastTrack = pastTrack;
	}
	
}
