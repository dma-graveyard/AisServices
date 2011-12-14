package dk.dma.aisservices.core.services.ais;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import dk.frv.enav.common.net.http.HttpParams;

public class OverviewRequest {

	private double swLat = -360;
	private double swLon = -360;
	private double neLat = 360;
	private double neLon = 360;
	private List<String> countries = new ArrayList<String>();
		
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
		if (params.exists("country")) {
			String[] arr = StringUtils.split(params.getFirst("country"), ",");
			for (String country : arr) {
				countries.add(country);
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

	public List<String> getCountries() {
		return countries;
	}

	public void setCountries(List<String> countries) {
		this.countries = countries;
	}
	
}
