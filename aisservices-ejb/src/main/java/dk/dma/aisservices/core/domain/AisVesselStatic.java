package dk.dma.aisservices.core.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import dk.frv.ais.message.ShipTypeCargo;

/**
 * The persistent class for the ais_vessel_static database table.
 * 
 */
@Entity
@Table(name = "ais_vessel_static")
public class AisVesselStatic implements Serializable {
	private static final long serialVersionUID = 1L;

	private int mmsi;
	private String callsign;
	private Date created;
	private Short dimBow;
	private Byte dimPort;
	private Byte dimStarboard;
	private Short dimStern;
	private String name;
	private Date received;
	private Byte shipType;
	private Byte decodedShipType;
	private Byte cargo;
	private AisClassAStatic aisClassAStatic;
	private AisVesselTarget aisVesselTarget;

	public AisVesselStatic() {

	}

	@Id
	@Column(unique = true, nullable = false)
	public int getMmsi() {
		return this.mmsi;
	}

	public void setMmsi(int mmsi) {
		this.mmsi = mmsi;
	}

	@Column(nullable = true, length = 8)
	public String getCallsign() {
		return this.callsign;
	}

	public void setCallsign(String callsign) {
		this.callsign = callsign;
	}

	@Column(nullable = false)
	public Date getCreated() {
		return this.created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Column(name = "dim_bow", nullable = true)
	public Short getDimBow() {
		return this.dimBow;
	}

	public void setDimBow(Short dimBow) {
		this.dimBow = dimBow;
	}

	@Column(name = "dim_port", nullable = true)
	public Byte getDimPort() {
		return this.dimPort;
	}

	public void setDimPort(Byte dimPort) {
		this.dimPort = dimPort;
	}

	@Column(name = "dim_starboard", nullable = true)
	public Byte getDimStarboard() {
		return this.dimStarboard;
	}

	public void setDimStarboard(Byte dimStarboard) {
		this.dimStarboard = dimStarboard;
	}

	@Column(name = "dim_stern", nullable = true)
	public Short getDimStern() {
		return this.dimStern;
	}

	public void setDimStern(Short dimStern) {
		this.dimStern = dimStern;
	}

	@Column(nullable = true, length = 32)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(nullable = false)
	public Date getReceived() {
		return this.received;
	}

	public void setReceived(Date received) {
		this.received = received;
	}

	@Column(name = "ship_type", nullable = true)
	public Byte getShipType() {
		return this.shipType;
	}

	public void setShipType(Byte shipType) {
		this.shipType = shipType;
	}

	@Transient
	public ShipTypeCargo getShipTypeCargo() {
		Byte shipType = getShipType();
		return new ShipTypeCargo((shipType == null) ? 0 : shipType);
	}

	@Column(name = "decoded_ship_type", nullable = true)
	public Byte getDecodedShipType() {
		return decodedShipType;
	}

	public void setDecodedShipType(Byte decodedShipType) {
		this.decodedShipType = decodedShipType;
	}

	@Column(name = "cargo", nullable = true)
	public Byte getCargo() {
		return cargo;
	}

	public void setCargo(Byte cargo) {
		this.cargo = cargo;
	}

	// bi-directional one-to-one association to AisClassAStatic
	@OneToOne(mappedBy = "aisVesselStatic")
	public AisClassAStatic getAisClassAStatic() {
		return this.aisClassAStatic;
	}

	public void setAisClassAStatic(AisClassAStatic aisClassAStatic) {
		this.aisClassAStatic = aisClassAStatic;
	}

	// bi-directional one-to-one association to AisVesselTarget
	@OneToOne
	@JoinColumn(name = "mmsi", nullable = false, insertable = false, updatable = false)
	public AisVesselTarget getAisVesselTarget() {
		return this.aisVesselTarget;
	}

	public void setAisVesselTarget(AisVesselTarget aisVesselTarget) {
		this.aisVesselTarget = aisVesselTarget;
	}

}