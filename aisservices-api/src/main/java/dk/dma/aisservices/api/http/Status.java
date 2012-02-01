package dk.dma.aisservices.api.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dk.dma.aisservices.core.services.ais.AisService;

public class Status extends HttpApiServlet {
	
	private static final long serialVersionUID = 1L;
	
	@EJB
	AisService aisService;
	
	public void init() throws ServletException {
		super.init();
	}
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();                
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");
        
        String status = "ERROR";
        Date latestUpdate = aisService.getLatestUpdate();
        long elapsedSecs = (System.currentTimeMillis() - latestUpdate.getTime()) / 1000;
        if (elapsedSecs < 300) {
        	status = "OK";
        }        
        out.print("AIS=" + status);
	}

}
