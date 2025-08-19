package com.example.web;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.example.config.AppConfig;
import com.example.web.config.WebConfig;

public class WebMain {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(WebMain.class);
        int port = 8080;

        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.getEnvironment().setActiveProfiles("file");
        context.register(AppConfig.class, WebConfig.class);
        
        try {
            Server server = new Server(port);
            ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
            handler.setContextPath("/");
            
            context.setServletContext(handler.getServletContext());
            context.refresh();
            
            DispatcherServlet servlet = new DispatcherServlet(context);
            handler.addServlet(new ServletHolder(servlet), "/*");
            server.setHandler(handler);
            
            server.setStopAtShutdown(true);
            server.start();
            logger.info("Web server started on port {}", port);
            logger.info("REST API: http://localhost:{}/api/todos/findAll", port);
            
            server.join(); // Clean shutdown - no console complexity
            
        } catch (Exception e) {
            logger.error("Failed to start web server", e);
            context.close();
        }
    }
}
