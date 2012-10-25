package dk.dma.aisservices.api.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import dk.dma.aisservices.core.services.ais.AisService;
import dk.dma.aisservices.core.services.ais.DetailedAisTarget;
import dk.dma.aisservices.core.services.ais.OverviewRequest;
import dk.dma.aisservices.core.services.ais.OverviewResponse;
import dk.frv.enav.common.net.http.HttpParams;

public class AisGreenland extends HttpApiServlet {

	private static final long serialVersionUID = 1L;
	private Gson gson = new Gson();
	
	@EJB
	AisService aisService;
	
	public void init() throws ServletException {
		super.init();
	}
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();                
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");        
        @SuppressWarnings("unchecked")
		HttpParams params = new HttpParams(request.getParameterMap());
        String output = "";
        
        // Determine method
        String method = (params.containsKey("method") ? params.getFirst("method") : "overview");
        
        if (method.equalsIgnoreCase("overview")) {
        	response.setContentType("application/json");
        	OverviewRequest overviewRequest = new OverviewRequest();
        	overviewRequest.setSwLat(56);
        	overviewRequest.setSwLon(-70);
        	overviewRequest.setNeLat(90);
        	overviewRequest.setNeLon(-5);
        	List<String> excludeRegions = new ArrayList<String>();
        	excludeRegions.add("808");
        	overviewRequest.getExcludeMap().put("sourceRegion", excludeRegions);
        	
        	
            OverviewResponse overviewResponse = aisService.getOverview(overviewRequest);
            output = gson.toJson(overviewResponse);
        }
        else if (method.equalsIgnoreCase("details")) {
        	response.setContentType("application/json");
        	Integer id = Integer.parseInt(params.getFirst("id"));
        	DetailedAisTarget detailedAisTarget = aisService.getTargetDetails(id, params.containsKey("past_track"));
        	output = gson.toJson(detailedAisTarget);
        }
        else if (method.equalsIgnoreCase("table")) {
        	response.setContentType("text/plain");
        	OverviewRequest overviewRequest = new OverviewRequest(params);        	
        	String table = aisService.getOverviewTable(overviewRequest);        	
        	output = table;
        }
        
        out.print(output);
	}
}
