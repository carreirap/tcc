package br.com.fichasordens.restcontroller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
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
import br.com.fichasordens.dto.PapelEnum;
import br.com.fichasordens.dto.UsuarioDto;
import br.com.fichasordens.exception.ExcecaoRetorno;

@RestController
@RequestMapping("/usuario")
@EnableResourceServer 
public class UsuarioController {
	
	private static final Logger LOGGER = LogManager.getLogger(UsuarioController.class);
	
	@Autowired private Usuario usuario;
	
	private static final String TRUE = "true";

	 @RequestMapping(method = RequestMethod.POST)
	 public ResponseEntity adicionarUsuario(@RequestBody UsuarioDto dto) {
		 Usuario newUsuario = this.convertToUsuario(dto);
		 try {
			newUsuario = usuario.adicionarUsuario(newUsuario);
			return new ResponseEntity<>(newUsuario, HttpStatus.OK);
		} catch (ExcecaoRetorno e) {
			LOGGER.error("Error adicionando um novo usuario", e);
			return new ResponseEntity<>(new MensagemRetornoDto(e.getMessage()), HttpStatus.BAD_REQUEST);
			
		}
	 }
	 
	 @RequestMapping(method = RequestMethod.PUT)
	 public ResponseEntity alterarSenha(@RequestBody UsuarioDto dto) {
		 Usuario newUsuario = this.convertToUsuario(dto);
		 try {
			newUsuario = usuario.alterarUsuario(newUsuario);
			return new ResponseEntity<>(newUsuario, HttpStatus.OK);
		} catch (ExcecaoRetorno e) {
			return new ResponseEntity<>(new MensagemRetornoDto(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	 }
	 
	 @RequestMapping(method = RequestMethod.GET)
	 public ResponseEntity<List<UsuarioDto>> getUsuario(@RequestParam(required=false) final String user) {
		 final List<Usuario> lst = usuario.listarUsuario(user);
		 
		 return new ResponseEntity<>(convertToDto(lst),HttpStatus.OK);
	 }
	 
	 @RequestMapping(value = "/getUpdatedUser", method = RequestMethod.POST)
	 public ResponseEntity<UsuarioDto> login(@RequestBody UsuarioDto user) {
		 final List<Usuario> lst = this.usuario.listarUsuario(user.getUsuario());
		 return new ResponseEntity<>(convertToDto(lst).get(0),HttpStatus.OK);
	 }
	 
	 private List<UsuarioDto> convertToDto(final List<Usuario> lst) {
		 final List<UsuarioDto> dtos = new ArrayList<>();
		 lst.forEach(e->{ 
			 UsuarioDto dto = new UsuarioDto();
			 dto.setNome(e.getNome());
			 dto.setUsuario(e.getNomeUsuario());
			 dto.setPapel(PapelEnum.convertEnum(e.getPapel()).getNome());
			 dto.setId(e.getId());
			 dto.setSituacao(e.getSituacao() == 1 ? UsuarioController.TRUE : null);
			 dtos.add(dto);
		 });
		 return dtos;
	 }
	 
	 private Usuario convertToUsuario(final UsuarioDto dto) {
		 final Usuario user = new Usuario();
		 user.setNome(dto.getNome());
		 user.setNomeUsuario(dto.getUsuario());
		 user.setPapel(dto.getPapel());
		 user.setNovaSenha(dto.getNovaSenha());
		 user.setConfirmaSenha(dto.getConfirmaSenha());
		 user.setSenha(dto.getSenha());
		 user.setPapel(dto.getPapel());
		 user.setId(dto.getId());
		 user.setSituacao(dto.getSituacao().equals(UsuarioController.TRUE) ? 1 : 0);
			 
		 return user;
	 }
}
