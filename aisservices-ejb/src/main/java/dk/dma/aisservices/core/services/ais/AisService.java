package dk.dma.aisservices.core.services.ais;

import javax.ejb.Local;

import dk.dma.aisservices.core.domain.AisVesselTarget;

@Local
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
	public DetailedAisTarget getTargetDetails(int id, boolean pastTrack);

	/**
	 * Get past track for AIS target
	 * @param mmsi
	 * @return
	 */
	public PastTrack getPastTrack(int mmsi);
	
	/**
	 * Get past track seconds back
	 * @param mmsi
	 * @param secondsBack
	 * @return
	 */
	public PastTrack getPastTrack(int mmsi, int secondsBack);
	
	
}
