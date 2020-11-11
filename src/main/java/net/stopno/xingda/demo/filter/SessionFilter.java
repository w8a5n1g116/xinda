package net.stopno.xingda.demo.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class SessionFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)servletRequest;
        HttpSession session = httpRequest.getSession();
        String uri = httpRequest.getRequestURI();
        if(uri.equals("/loginPost") || uri.equals("/login") || uri.contains("/assets/")){
            filterChain.doFilter(servletRequest, servletResponse);
        }else{
            if(session.getAttribute("user") != null){
                filterChain.doFilter(servletRequest, servletResponse);
            }else{
                httpRequest.getRequestDispatcher("/login").forward(servletRequest, servletResponse);
            }
        }

    }
}
