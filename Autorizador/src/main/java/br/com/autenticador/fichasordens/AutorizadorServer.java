package br.com.autenticador.fichasordens;

import java.security.Principal;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@ComponentScan({"br.com"})
@EntityScan("br.com.fichasordens")
@EnableJpaRepositories("br.com.fichasordens.repository")
@CrossOrigin(allowedHeaders="*",allowCredentials="true")
@SpringBootApplication
@RestController
public class AutorizadorServer {
   private static final Log logger = LogFactory.getLog(AutorizadorServer.class);

   public static void main(String[] args) {
      SpringApplication.run(AutorizadorServer.class, args);
   }

   @RequestMapping("/user")
   public Principal user(Principal user) {
      logger.info("AS /user has been called");
      logger.debug("user info: "+user.toString());
      return user;
   }
   /*@RequestMapping(value = "/oauth/token",method = RequestMethod.OPTIONS)
   public HttpServletResponse hello() {
      return new HttpHttpServletResponse.SC_OK;
      https://medium.com/@ct7/the-simple-way-to-make-a-mobile-angular-2-bootstrap-navbar-without-jquery-d6b3f67b037b
      https://getbootstrap.com/docs/4.0/components/navbar/
   }*/
   
}