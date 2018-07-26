package br.com.fichasordens.restcontroller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.fichasordens.service.DashboardService;

@RestController
@RequestMapping("/dashboard")
@EnableResourceServer
public class DashboardController {
	
	@Autowired
	private DashboardService dashBoardService;
	
	@RequestMapping(method = RequestMethod.GET, path="/allFichas")
	public ResponseEntity listarFichas() {
		Map<?,?> map = this.dashBoardService.contarFichasAtendimento();
		return new ResponseEntity<>(map,HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, path="/allOrdens")
	public ResponseEntity listarOrdens() {
		Map<?,?> map = this.dashBoardService.contarOrdensDeServicos();
		return new ResponseEntity<>(map,HttpStatus.OK);
	}

}
