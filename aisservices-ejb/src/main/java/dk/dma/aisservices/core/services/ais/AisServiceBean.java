package dk.dma.aisservices.core.services.ais;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import dk.dma.aisservices.core.domain.AisVesselTarget;
import dk.dma.aisservices.core.domain.AisVesselTrack;
import dk.frv.ais.geo.GeoLocation;

@Stateless
@Remote(AisServiceRemote.class)
@Local(AisService.class)
public class AisServiceBean implements AisService {

	private static final long MAX_PAST_TRACK_GAP = 10 * 60 * 1000; // 10 min
	private static final long MIN_PAST_TRACK_DISTANCE = 500; // 500 meters
	private static final long MAX_TARGET_AGE = 6 * 60 * 1000; // 6 min

	@PersistenceContext(unitName = "ais")
	private EntityManager entityManager;

	@Override
	public int getVesselCount() {
		Query query = entityManager.createQuery("SELECT COUNT(*) FROM AisVesselTarget t WHERE t.validTo >= :now");
		query.setParameter("now", new Date());
		Long count = (Long) query.getSingleResult();
		return count.intValue();
	}

	@Override
	public OverviewResponse getOverview(OverviewRequest overviewRequest) {
		OverviewResponse response = new OverviewResponse();
		
		List<Object[]> lines = getOverviewRows(overviewRequest); 
		
		for (Object[] values : lines) {			
			response.addShip((Integer) values[0], (String) values[1], (Double) values[2], (Double) values[3], (Double) values[4],
					(Double) values[5], (Byte) values[6], (Byte) values[7]);
		}

		return response;
	}
	
	@Override
	public String getOverviewTable(OverviewRequest overviewRequest) {
		List<Object[]> lines = getOverviewRows(overviewRequest);
		StringBuilder buf = new StringBuilder();
		buf.append("id,latitude,longitude\n");
		
		for (Object[] values : lines) {
			buf.append(Integer.toString((Integer)values[0]) + ",");
			buf.append(String.format(Locale.US, "%.4f,", (Double) values[4]));
			buf.append(String.format(Locale.US, "%.4f\n", (Double) values[5]));
		}
				
		return buf.toString();
	}

	private List<Object[]> getOverviewRows(OverviewRequest overviewRequest) {
		String sql = "SELECT vt.id, vt.vesselClass, vt.aisVesselPosition.cog, vt.aisVesselPosition.sog, vt.aisVesselPosition.lat, vt.aisVesselPosition.lon, vt.aisVesselStatic.shipType, casp.navStatus "
				+ "FROM AisVesselTarget vt "
				+ "LEFT OUTER JOIN vt.aisVesselPosition.aisClassAPosition AS casp "
				+ "WHERE vt.aisVesselPosition.lat > :swLat "
				+ "AND vt.aisVesselPosition.lon > :swLon "
				+ "AND vt.aisVesselPosition.lat < :neLat "
				+ "AND vt.aisVesselPosition.lon < :neLon "
				+ "AND vt.validTo >= :now "
				+ "AND vt.aisVesselPosition.lat IS NOT NULL AND vt.aisVesselPosition.lon IS NOT NULL ";

		if (overviewRequest.getCountries().size() > 0) {
			String[] ors = new String[overviewRequest.getCountries().size()];
			for (int i = 0; i < ors.length; i++) {
				ors[i] = "vt.country = :country" + i;
			}
			sql += "\nAND (";
			sql += StringUtils.join(ors, " OR ");
			sql += ")";
		}

		Query query = entityManager.createQuery(sql);
		query.setParameter("swLat", overviewRequest.getSwLat());
		query.setParameter("swLon", overviewRequest.getSwLon());
		query.setParameter("neLat", overviewRequest.getNeLat());
		query.setParameter("neLon", overviewRequest.getNeLon());
		query.setParameter("now", new Date());
		if (overviewRequest.getCountries().size() > 0) {
			for (int i = 0; i < overviewRequest.getCountries().size(); i++) {
				query.setParameter("country" + i, overviewRequest.getCountries().get(i));
			}
		}

		@SuppressWarnings("unchecked")
		List<Object[]> lines = query.getResultList();
		
		return lines;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<AisVesselTarget> getAisTargets(double swLat,double swLon, double neLat, double neLon) {		
		Query query = entityManager.createQuery("" +
				"SELECT vt FROM AisVesselTarget vt " +
				"WHERE vt.aisVesselPosition.lat > :swLat " +
				"AND vt.aisVesselPosition.lon > :swLon " +
				"AND vt.aisVesselPosition.lat < :neLat " +
				"AND vt.aisVesselPosition.lon < :neLon " +
				"AND vt.aisVesselPosition.received > :newDate");

		query.setParameter("swLat",swLat);
		query.setParameter("swLon", swLon);
		query.setParameter("neLat", neLat);
		query.setParameter("neLon", neLon);
		query.setParameter("newDate", new Date(System.currentTimeMillis()-MAX_TARGET_AGE));
		
		return query.getResultList();
	
	}

	@SuppressWarnings("unchecked")
	@Override
	public AisVesselTarget getById(int id) {
		Query query = entityManager.createNamedQuery("AisVesselTarget:getById");
		query.setParameter("id", id);
		List<AisVesselTarget> list = query.getResultList();
		return (list.size() == 1 ? list.get(0) : null);
	}

	@Override
	public DetailedAisTarget getTargetDetails(int id, boolean pastTrack) {
		DetailedAisTarget aisTarget = new DetailedAisTarget();
		AisVesselTarget target = getById(id);
		if (target != null) {
			aisTarget.init(target);
		}
		if (pastTrack) {
			// Determine secondsBack based on source
			int secondsBack = 120 * 60; // 2 hours
			boolean checkMaxGap = true;
			if (aisTarget.getSource().equals("SAT")) {
				secondsBack = 36 * 60 * 60; // 36 hours
				checkMaxGap = false;
			}						
			aisTarget.setPastTrack(getPastTrack(target.getMmsi(), secondsBack, checkMaxGap));
		}
		return aisTarget;
	}

	@Override
	public PastTrack getPastTrack(int mmsi) {
		// Default seconds back
		int secondsBack = 120 * 60; // 2 hours
		return getPastTrack(mmsi, secondsBack, true);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PastTrack getPastTrack(int mmsi, int secondsBack, boolean checkMaxGap) {
		PastTrack pastTrack = new PastTrack();
		Query query = entityManager.createNamedQuery("AisVesselTrack:get");
		query.setParameter("mmsi", mmsi);
		Date from = new Date(System.currentTimeMillis() - secondsBack * 1000);
		query.setParameter("from", from);
		List<AisVesselTrack> list = query.getResultList();

		AisVesselTrack last = null;
		Date lastTime = new Date();

		for (AisVesselTrack aisVesselTrack : list) {
			// Determine if too long between points
			long elapsed = lastTime.getTime() - aisVesselTrack.getTime().getTime();
			if (checkMaxGap && elapsed > MAX_PAST_TRACK_GAP) {
				break;
			}
			lastTime = aisVesselTrack.getTime();
			// Always add first
			if (last == null) {
				pastTrack.getPoints().add(new PastTrackPoint(aisVesselTrack));
				last = aisVesselTrack;
				continue;
			}
			// Determine distance between this and last
			GeoLocation thisPos = new GeoLocation(aisVesselTrack.getLat(), aisVesselTrack.getLon());
			GeoLocation lastPos = new GeoLocation(last.getLat(), last.getLon());
			double dist = thisPos.getRhumbLineDistance(lastPos);
			if (dist < MIN_PAST_TRACK_DISTANCE) {
				continue;
			}
			pastTrack.getPoints().add(new PastTrackPoint(aisVesselTrack));
			last = aisVesselTrack;
		}

		return pastTrack;
	}

	@Override
	public Date getLatestUpdate() {
		Query query = entityManager.createQuery("SELECT MAX(vt.lastReceived) FROM AisVesselTarget vt");
		return (Date)query.getSingleResult();
	}

}
