package tui;

import java.util.EnumSet;
import java.util.Properties;
import javax.servlet.DispatcherType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

public class App {

    public static void main(String[] args) throws Exception {
        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
        appContext.setConfigLocation(AppConfig.class.getName());

        Properties props = new Properties();
        props.load(App.class.getResourceAsStream("/application.properties"));
        Server server = new Server(Integer.parseInt(props.getProperty("http.port")));
        ServletContextHandler root = new ServletContextHandler(server, "/");
        ServletHolder dispatcherServlet = new ServletHolder(new DispatcherServlet(appContext));
        dispatcherServlet.setInitOrder(1);
        dispatcherServlet.setAsyncSupported(true);

        FilterHolder encodingFilter = new FilterHolder(CharacterEncodingFilter.class);
        encodingFilter.setInitParameter("encoding", "UTF-8");

        root.addServlet(dispatcherServlet, "/*");
        root.addFilter(encodingFilter, "/*", EnumSet.allOf(DispatcherType.class));

        server.start();
    }
}

