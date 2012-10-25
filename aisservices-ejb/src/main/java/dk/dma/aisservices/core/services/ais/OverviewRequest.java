package dk.dma.aisservices.core.services.ais;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import dk.frv.enav.common.net.http.HttpParams;

public class OverviewRequest {

	private double swLat = -360;
	private double swLon = -360;
	private double neLat = 360;
	private double neLon = 360;
	private Map<String, List<String>> filterMap = new HashMap<String, List<String>>();
	private Map<String, List<String>> excludeMap = new HashMap<String, List<String>>();
	
	private static final String[] filterNames = {"vesselClass", "country", "sourceType", "sourceCountry", "sourceRegion", "sourceBs", "sourceSystem"};
		
	public OverviewRequest() {
		
	}
	
	public OverviewRequest(HttpParams params) {
		parse(params);
	}
	
	public void parse(HttpParams params) {
		if (params.exists("swLat")) {
			swLat = Double.parseDouble(params.getFirst("swLat"));
		}
		if (params.exists("swLon")) {
			swLon = Double.parseDouble(params.getFirst("swLon"));
		}
		if (params.exists("neLat")) {
			neLat = Double.parseDouble(params.getFirst("neLat"));
		}
		if (params.exists("neLon")) {
			neLon = Double.parseDouble(params.getFirst("neLon"));
		}
		for (String filterName : filterNames) {
			if (params.exists(filterName)) {
				List<String> values = new ArrayList<String>();
				String[] arr = StringUtils.split(params.getFirst(filterName), ",");
				for (String value : arr) {
					values.add(value);
				}
				if (values.size() > 0) {
					filterMap.put(filterName, values);
				}
			}
		}
	}

	public double getSwLat() {
		return swLat;
	}

	public void setSwLat(double swLat) {
		this.swLat = swLat;
	}

	public double getSwLon() {
		return swLon;
	}

	public void setSwLon(double swLon) {
		this.swLon = swLon;
	}

	public double getNeLat() {
		return neLat;
	}

	public void setNeLat(double neLat) {
		this.neLat = neLat;
	}

	public double getNeLon() {
		return neLon;
	}

	public void setNeLon(double neLon) {
		this.neLon = neLon;
	}
	
	public Map<String, List<String>> getFilterMap() {
		return filterMap;
	}
	
	public Map<String, List<String>> getExcludeMap() {
		return excludeMap;
	}
	
}
