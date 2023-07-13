package br.com.trier.springvespertino.services;

import br.com.trier.springvespertino.BaseTest;
import br.com.trier.springvespertino.models.Country;
import br.com.trier.springvespertino.models.Team;
import br.com.trier.springvespertino.services.exceptions.IntegrityViolation;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
public class TeamServiceTest extends BaseTest {

    @Autowired
    private TeamService service;

    @Test
    @DisplayName("Teste salvar equipe")
    void testSalvar() {
        var equipe = new Team(null,"Mclaren");
        service.salvar(equipe);
        equipe = service.findById(1);
        assertEquals(1, equipe.getId());
        assertEquals("Mclaren", equipe.getName());
    }

    @Test
    @DisplayName("Teste salvar equipe já cadastrada")
    @Sql({"classpath:/sqls/equipe.sql"})
    void testSalvarEquipeCadastrada() {
        var equipe = new Team(3,"Ferrari");
        var exception = assertThrows(
                IntegrityViolation.class, () -> service.salvar(equipe));
        assertEquals("Nome já existente: Ferrari", exception.getMessage());

    }
}
