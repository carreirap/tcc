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
import br.com.fichasordens.dto.PapelEnum;
import br.com.fichasordens.dto.UsuarioDTO;
import br.com.fichasordens.exception.ExcecaoRetorno;

@RestController
@RequestMapping("/usuario")
@EnableResourceServer 
public class UsuarioController {
	
	@Autowired private Usuario usuario;

	 @RequestMapping(method = RequestMethod.POST)
	 public ResponseEntity adicionarUsuario(@RequestBody UsuarioDTO dto) {
		 Usuario newUsuario = this.convertToUsuario(dto);
		 try {
			newUsuario = usuario.adicionarUsuario(newUsuario);
			return new ResponseEntity<>(newUsuario, HttpStatus.OK);
		} catch (ExcecaoRetorno e) {
			return new ResponseEntity<>(new MensagemRetornoDto(e.getMessage()), HttpStatus.BAD_REQUEST);
			//e.printStackTrace();
		}
	 }
	 
	 @RequestMapping(method = RequestMethod.PUT)
	 public ResponseEntity alterarSenha(@RequestBody UsuarioDTO dto) {
		 Usuario newUsuario = this.convertToUsuario(dto);
		 try {
			newUsuario = usuario.adicionarUsuario(newUsuario);
			return new ResponseEntity<>(newUsuario, HttpStatus.OK);
		} catch (ExcecaoRetorno e) {
			return new ResponseEntity<>(new MensagemRetornoDto(e.getMessage()), HttpStatus.BAD_REQUEST);
			//e.printStackTrace();
		}
	 }
	 
	 @RequestMapping(method = RequestMethod.GET)
	 public ResponseEntity<List<UsuarioDTO>> getUsuario(@RequestParam(required=false) final String user) {
		 final List<Usuario> lst = usuario.listarUsuario();
		 
		 return new ResponseEntity<List<UsuarioDTO>>(convertToDto(lst),HttpStatus.OK);
	 }
	 
	 private List<UsuarioDTO> convertToDto(final List<Usuario> lst) {
		 final List<UsuarioDTO> dtos = new ArrayList<UsuarioDTO>();
		 lst.forEach(e->{ 
			 UsuarioDTO dto = new UsuarioDTO();
			 dto.setNome(e.getNome());
			 dto.setUsuario(e.getUsuario());
			 dto.setPapel(PapelEnum.convertEnum(e.getPapel()).getNome());
			 dto.setId(e.getId());
			 dto.setSituacao(e.getSituacao() == 1 ? "true" : null);
			 dtos.add(dto);
		 });
		 return dtos;
	 }
	 
	 private Usuario convertToUsuario(final UsuarioDTO dto) {
		 final Usuario usuario = new Usuario();
		 usuario.setNome(dto.getNome());
		 usuario.setUsuario(dto.getUsuario());
		 usuario.setPapel(dto.getPapel());
		 usuario.setNovaSenha(dto.getNovaSenha());
		 usuario.setConfirmaSenha(dto.getConfirmaSenha());
		 usuario.setPapel(dto.getPapel());
		 usuario.setId(dto.getId());
		 usuario.setSituacao(dto.getSituacao() != null ? 1 : 0);
			 
		 return usuario;
	 }
}
