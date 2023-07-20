package br.com.trier.springvespertino.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import br.com.trier.springvespertino.BaseTest;
import br.com.trier.springvespertino.models.User;
import br.com.trier.springvespertino.services.exceptions.IntegrityViolation;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;

@Transactional
public class UserServiceImplTest extends BaseTest{
	
	@Autowired
	UserService userService;
	
	@Test
	@DisplayName("Teste buscar usuário por ID")
	@Sql({"classpath:/sqls/usuario.sql"})
	void findByIdTest() {
		var usuario = userService.findById(3);
		assertNotNull(usuario);
		assertEquals(3, usuario.getId());
		assertEquals("User 1", usuario.getName());
		assertEquals("email1", usuario.getEmail());
		assertEquals("senha1", usuario.getPassword());
	}
	
	@Test
	@DisplayName("Teste buscar usuário por ID inexistente")
	@Sql({"classpath:/sqls/usuario.sql"})
	void findByIdNonExistsTest() {
		var exception = assertThrows(
				ObjectNotFound.class, () -> userService.findById(10));
		assertEquals("O usuário 10 não existe", exception.getMessage());
	}
	
	@Test
	@DisplayName("Teste inserir usuário")
	void insertUserTest() {
		User usuario = new User(null, "insert", "insert", "insert", "ADMIN" );
		userService.insert(usuario);
		usuario = userService.findById(1);
		assertEquals(1, usuario.getId());
		assertEquals("insert", usuario.getName());
		assertEquals("insert", usuario.getEmail());
		assertEquals("insert", usuario.getPassword());
	}
	
	@Test
	@DisplayName("Teste inserir usuário com e-mail duplicado")
	@Sql({"classpath:/sqls/usuario.sql"})
	void insertUserDuplicatedEmailTest() {
		User usuario = new User(null, "insert", "email1", "insert", "ADMIN" );
		var exception = assertThrows(
				IntegrityViolation.class, () -> userService.insert(usuario));
		assertEquals("Email já existente: email1", exception.getMessage());
	}
	
	@Test
	@DisplayName("Teste remover usuário")
	@Sql({"classpath:/sqls/usuario.sql"})
	void removeUserTest() {
		userService.delete(3);
		List<User> lista = userService.listAll();
		assertEquals(1, lista.size());
		assertEquals(4, lista.get(0).getId());
	}
	
	@Test
	@DisplayName("Teste remover usuário inexistente")
	@Sql({"classpath:/sqls/usuario.sql"})
	void removeUserNonExistsTest() {
		var exception = assertThrows(
				ObjectNotFound.class, () -> userService.delete(10));
		assertEquals("O usuário 10 não existe", exception.getMessage());
		
	}
	
	@Test
	@DisplayName("Teste listar todos")
	@Sql({"classpath:/sqls/usuario.sql"})
	void listAllUsersTest() {
		List<User> lista = userService.listAll();
		assertEquals(2, lista.size());
	}
	
	@Test
	@DisplayName("Teste listar todos sem possuir usuários cadastrados")
	void listAllUsersEmptyTest() {
		var exception = assertThrows(
				ObjectNotFound.class, () -> userService.listAll());
		assertEquals("Nenhum usuário cadastrado", exception.getMessage());
	}
	
	@Test
	@DisplayName("Teste alterar usuário")
	@Sql({"classpath:/sqls/usuario.sql"})
	void updateUsersTest() {
		var usuario = userService.findById(3);
		assertEquals("User 1", usuario.getName());
		var usuarioAltera = new User(3,"altera", "altera", "altera", "ADMIN");
		userService.update(usuarioAltera);
		usuario = userService.findById(3);
		assertEquals("altera", usuario.getName());
	}
	
	@Test
	@DisplayName("Teste alterar usuário com e-mail duplicado")
	@Sql({"classpath:/sqls/usuario.sql"})
	void updateUsersDuplicatedEmailTest() {
		var usuarioAltera = new User(3,"altera", "email2", "altera", "ADMIN");
		var exception = assertThrows(
				IntegrityViolation.class, () -> userService.update(usuarioAltera));
		assertEquals("Email já existente: email2", exception.getMessage());
	}
	
	@Test
	@DisplayName("Teste alterar usuário inexistente")
	void updateUsersNonExistsTest() {
		var usuarioAltera = new User(1,"altera", "altera", "altera", "ADMIN");
		var exception = assertThrows(
				ObjectNotFound.class, () -> userService.update(usuarioAltera));
		assertEquals("O usuário 1 não existe", exception.getMessage());
		
	}
	
	@Test
	@DisplayName("Teste buscar por nome que inicia com")
	@Sql({"classpath:/sqls/usuario.sql"})
	void findByNameStartsWithTest() {
		var lista = userService.findByName("u");
		assertEquals(2, lista.size());
		lista = userService.findByName("User 1");
		assertEquals(1, lista.size());
		var exception = assertThrows(
				ObjectNotFound.class, () -> userService.findByName("c"));
		assertEquals("Nenhum nome de usuário inicia com c", exception.getMessage());
	}
	
}
