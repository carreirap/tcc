package br.com.fichasordens.restcontroller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fichasordens.Usuario;
import br.com.fichasordens.dto.MensagemRetornoDto;
import br.com.fichasordens.dto.UsuarioDTO;

@RestController
@RequestMapping("/usuario")
@EnableResourceServer 
public class UsuarioController {
	
	@Autowired private Usuario usuario;

	 @RequestMapping(method = RequestMethod.POST)
	 public ResponseEntity<MensagemRetornoDto> adicionarUsuario() {
		 return new ResponseEntity<>(HttpStatus.OK);
	 }
	 
	 @RequestMapping(value = "/alterar", method = RequestMethod.POST)
	 public ResponseEntity<MensagemRetornoDto> alterarSenha(@RequestBody UsuarioDTO usuario) {
		 return new ResponseEntity<>(HttpStatus.OK);
	 }
	 
	 @RequestMapping(method = RequestMethod.GET)
	 public ResponseEntity<List<UsuarioDTO>> getUsuario(@RequestParam(required=false) final String user) {
		 final List<Usuario> lst = usuario.listarUsuario();
		 
		 return new ResponseEntity<List<UsuarioDTO>>(convertDto(lst),HttpStatus.OK);
	 }
	 
	 private List<UsuarioDTO> convertDto(final List<Usuario> lst) {
		 final List<UsuarioDTO> dtos = new ArrayList<UsuarioDTO>();
		 lst.forEach(e->{ 
			 UsuarioDTO dto = new UsuarioDTO();
			 dto.setNome(e.getNome());
			 dto.setUsuario(e.getUsuario());
			 dto.setPapel(e.getPapel());
			 dtos.add(dto);
		 });
		 return dtos;
	 }
	 
}
