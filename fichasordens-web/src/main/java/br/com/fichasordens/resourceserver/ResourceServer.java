package br.com.fichasordens.resourceserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.fichasordens.dto.UsuarioDto;


@ComponentScan({"br.com"})
@EntityScan("br.com.fichasordens")
@EnableJpaRepositories("br.com.fichasordens.repository")
@CrossOrigin(allowedHeaders="*",allowCredentials="true")
@SpringBootApplication
@RestController
@EnableResourceServer 
public class ResourceServer {
   public static void main(String[] args) {
      SpringApplication.run(ResourceServer.class, args);
   }

   @RequestMapping(value = "/getUpdatedUser", method = RequestMethod.POST)
   public UsuarioDto login(@RequestBody UsuarioDto user) {
      
	   user.setUsuario("Hello "+ user.getUsuario()+ " !! ");
	   
	   return user;
   }

 
}
