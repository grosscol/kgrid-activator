package org.uofm.ot.activator;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
@ComponentScan("org.uofm.ot.pad")
@ComponentScan("org.uofm.ot.activator")
public class ObjectTellerExecutionStackApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {

		new SpringApplicationBuilder(ObjectTellerExecutionStackApplication.class)
				.build()
				.run(args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application
				.sources(ObjectTellerExecutionStackApplication.class);
	}


}
