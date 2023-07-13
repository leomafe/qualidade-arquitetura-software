package br.com.trier.springvespertino.models;

import br.com.trier.springvespertino.models.dto.UserDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode (of = "id")
@Entity(name="usuario")
public class User {
	
	@Setter
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario")
	private Integer id;
	
	@Column(name = "nome_usuario")
	private String name;
	
	@Column(name = "email_usuario", unique = true)
	private String email;
	
	@Column(name = "senha_usuario")
	private String password;
	
	@Column(name = "permissoes_usuario")
	private String roles;
	
	public User(UserDTO dto) {
		this(dto.getId(), dto.getName(), dto.getEmail(), dto.getPassword(), dto.getRoles());
	}
	
	public UserDTO toDTO() {
		return new UserDTO(id, name, email, password, roles);
	}

}
