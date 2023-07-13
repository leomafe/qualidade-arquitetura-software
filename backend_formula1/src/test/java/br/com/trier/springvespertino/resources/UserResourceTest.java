package br.com.trier.springvespertino.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import br.com.trier.springvespertino.SpringVespertinoApplication;
import br.com.trier.springvespertino.config.jwt.LoginDTO;
import br.com.trier.springvespertino.models.dto.UserDTO;

@ActiveProfiles("test")
@SpringBootTest(classes = SpringVespertinoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserResourceTest {

	@Autowired
	protected TestRestTemplate rest;
	
	private HttpHeaders getHeaders(String email, String password){
		LoginDTO loginDTO = new LoginDTO(email, password);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<LoginDTO> requestEntity = new HttpEntity<>(loginDTO, headers);
		ResponseEntity<String> responseEntity = rest.exchange(
				"/auth/token", 
				HttpMethod.POST,  
				requestEntity,    
				String.class   
				);
		String token = responseEntity.getBody();
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(token);
		return headers;
	}
	
		private ResponseEntity<UserDTO> getUser(String url) {
		return rest.exchange(
				url,  
				HttpMethod.GET, 
				new HttpEntity<>(getHeaders("email1", "senha1")), 
				UserDTO.class
				);
	}

	private ResponseEntity<List<UserDTO>> getUsers(String url) {
		return rest.exchange(
				url, HttpMethod.GET, 
				new HttpEntity<>(getHeaders("email1", "senha1")), 
				new ParameterizedTypeReference<List<UserDTO>>() {}
			);
	}

	@Test
	@DisplayName("Buscar por nome")
	@Sql({"classpath:/src/test/sqls/limpa_tabelas.sql"})
	@Sql({"classpath:/src/test/sqls/usuario.sql"})
	public void findByNameTest() {
		ResponseEntity<List<UserDTO>> response = getUsers("/users/name/u");
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertEquals(2, response.getBody().size());
	}
	
	@Test
	@DisplayName("Buscar por id")
	@Sql({"classpath:/src/test/sqls/limpa_tabelas.sql"})
	@Sql({"classpath:/src/test/sqls/usuario.sql"})
	public void findByIdTest() {
		ResponseEntity<UserDTO> response = getUser("/users/3");
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		UserDTO user = response.getBody();
		assertEquals("User 1", user.getName());
	}

	@Test
	@DisplayName("Buscar por id inexistente")
	@Sql({"classpath:/src/test/sqls/limpa_tabelas.sql"})
	@Sql({"classpath:/src/test/sqls/usuario.sql"})
	public void testGetNotFound() {
		ResponseEntity<UserDTO> response = getUser("/users/100");
		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
	}
	
	@Test
	@DisplayName("Cadastrar usuário")
	@Sql({"classpath:/src/test/sqls/limpa_tabelas.sql"})
	@Sql({"classpath:/src/test/sqls/usuario.sql"})
	public void testCreateUser() {
		UserDTO dto = new UserDTO(null, "nome", "email", "senha", "ADMIN");
		HttpHeaders headers = getHeaders("email1", "senha1");
		HttpEntity<UserDTO> requestEntity = new HttpEntity<>(dto, headers);
		ResponseEntity<UserDTO> responseEntity = rest.exchange(
	            "/users", 
	            HttpMethod.POST,  
	            requestEntity,    
	            UserDTO.class   
	    );
		assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
		UserDTO user = responseEntity.getBody();
		assertEquals("nome", user.getName());
	}
	
	@Test
	@DisplayName("Listar Todos")
	@Sql({"classpath:/src/test/sqls/limpa_tabelas.sql"})
	@Sql({"classpath:/src/test/sqls/usuario.sql"})
	public void findAll() {
		ResponseEntity<List<UserDTO>> response =  rest.exchange(
				"/users", 
				HttpMethod.GET, 
				new HttpEntity<>(getHeaders("email1", "senha1")),
				new ParameterizedTypeReference<List<UserDTO>>() {} 
				);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertEquals(2, response.getBody().size());
	}
	
	@Test
	@DisplayName("Alterar usuário")
	@Sql({"classpath:/src/test/sqls/limpa_tabelas.sql"})
	@Sql({"classpath:/src/test/sqls/usuario.sql"})
	public void testUpdateUser() {
		UserDTO dto = new UserDTO(3, "nome", "email", "senha", "ADMIN");
		HttpHeaders headers = getHeaders("email1", "senha1");
		HttpEntity<UserDTO> requestEntity = new HttpEntity<>(dto, headers);
		ResponseEntity<UserDTO> responseEntity = rest.exchange(
				"/users/3", 
				HttpMethod.PUT,  
				requestEntity,    
				UserDTO.class   
				);
		assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
		UserDTO user = responseEntity.getBody();
		assertEquals("nome", user.getName());
	}
	
	@Test
	@DisplayName("Excluir usuário")
	@Sql({"classpath:/src/test/sqls/limpa_tabelas.sql"})
	@Sql({"classpath:/src/test/sqls/usuario.sql"})
	public void testDeleteUser() {
		HttpHeaders headers = getHeaders("email1", "senha1");
		HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);
		ResponseEntity<Void> responseEntity = rest.exchange(
				"/users/3", 
				HttpMethod.DELETE,  
				requestEntity, 
				Void.class
				);
		assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	@DisplayName("Excluir usuário inexistente")
	@Sql({"classpath:/src/test/sqls/limpa_tabelas.sql"})
	@Sql({"classpath:/src/test/sqls/usuario.sql"})
	public void testDeleteNonExistUser() {
		HttpHeaders headers = getHeaders("email1", "senha1");
		HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);
		ResponseEntity<Void> responseEntity = rest.exchange(
				"/users/100", 
				HttpMethod.DELETE,  
				requestEntity, 
				Void.class
				);
		assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
	}
}
