package dk.dma.aisservices.api.http;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import dk.frv.enav.common.net.http.HttpParams;
import dk.frv.enav.common.text.TextUtils;

public abstract class HttpApiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static Logger LOG = Logger.getLogger(HttpApiServlet.class);
	
	@Override
    public void init() throws ServletException {
        super.init();
	}
	
    protected static void apiLogReq(Class<?> cls, HttpServletRequest request, HttpParams params) {
        LOG.debug(String.format("%-19s (req): (%s) %s", TextUtils.className(cls), request.getRemoteAddr(), params.makeQueryString(false)));
    }
    
    protected static void apiLogRes(Class<?> cls, String reply) {
        LOG.debug(String.format("%-19s (res):\n%s", TextUtils.className(cls), reply));
    }


	

}
