package dk.dma.aisservices.core.services.ais;

import java.util.Date;
import java.util.List;

import dk.dma.aisservices.core.domain.AisVesselTarget;

public interface AisService {
	
	/**
	 * Get total active vessel count
	 * @return
	 */
	int getVesselCount();
	
	/**
	 * Get list of AIS targets in the form of overview targets
	 * @param overviewRequest
	 * @return
	 */
	public OverviewResponse getOverview(OverviewRequest overviewRequest);
	
	/**
	 * Get overview table
	 * @param overviewRequest
	 * @return
	 */
	public String getOverviewTable(OverviewRequest overviewRequest);
	
	/**
	 * Get AIS targets within bounding box
	 * @param swLat
	 * @param swLon
	 * @param neLat
	 * @param neLon
	 * @return
	 */
	public List<AisVesselTarget> getAisTargets(double swLat,double swLon, double neLat, double neLon);
	
	/**
	 * Get vessel target by id
	 * @param id
	 * @return
	 */
	public AisVesselTarget getById(int id);
	
	/**
	 * Get details for MMSI target
	 * @param id
	 * @param pastTrack (get past track also)
	 * @return
	 */
	public DetailedAisTarget getTargetDetails(int id, boolean pastTrak);

	/**
	 * Get past track for AIS target
	 * @param mmsi
	 * @return
	 */
	public PastTrack getPastTrack(int mmsi);
	
	/**
	 * Get past track seconds back
	 * @param mmsi
	 * @param secondsBack The track for this many minutes back
	 * @param checkMaxGap Check if the gap between points becomes to large
	 * @return
	 */
	public PastTrack getPastTrack(int mmsi, int secondsBack, boolean checkMaxGap);
	
	/**
	 * Get the date for the latest update in the database
	 * @return
	 */
	public Date getLatestUpdate();
	
	
}
