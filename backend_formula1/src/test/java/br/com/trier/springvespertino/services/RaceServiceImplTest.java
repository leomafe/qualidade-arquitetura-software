package br.com.trier.springvespertino.services;

import br.com.trier.springvespertino.BaseTest;
import br.com.trier.springvespertino.models.*;
import br.com.trier.springvespertino.services.exceptions.IntegrityViolation;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
public class RaceServiceImplTest  extends BaseTest {

    @Autowired
    private RaceService service;

    @Test
    @DisplayName("Teste buscar corrida por ID")
    @Sql({"classpath:/sqls/pais.sql","classpath:/sqls/pista.sql","classpath:/sqls/campeonato.sql", "classpath:/sqls/corrida.sql"})
    void testFindById() {
        var corrida = service.findById(3);
        assertNotNull(corrida);
        assertEquals(3, corrida.getId());
    }

    @Test
    @DisplayName("Teste buscar corrida por ID inexistente")
    @Sql({"classpath:/sqls/pais.sql","classpath:/sqls/pista.sql","classpath:/sqls/campeonato.sql", "classpath:/sqls/corrida.sql"})
    void testFindByIdNonExists() {

        var exception = assertThrows(
                ObjectNotFound.class, () -> service.findById(10));
        assertEquals("Corrida 10 não existe", exception.getMessage());
    }

    @Test
    @DisplayName("Teste salvar corrida")
    @Sql({"classpath:/sqls/pais.sql","classpath:/sqls/pista.sql","classpath:/sqls/campeonato.sql", "classpath:/sqls/corrida.sql"})
    void testInsert() {
        LocalDate data = LocalDate.of(2022, 7, 18);
        LocalTime hora = LocalTime.of(0, 0, 0);
        ZoneId fusoHorario = ZoneId.systemDefault();

        ZonedDateTime dataCorrida = ZonedDateTime.of(data, hora, fusoHorario);
        var corrida = new Race(null, dataCorrida, new Speedway(3, "Pista Curta", 3, new Country(3, "Brasil")), new Championship(3,"Pista Curta",2022));
        service.insert(corrida);
        corrida = service.findById(1);
        assertEquals(1, corrida.getId());
    }

    @Test
    @DisplayName("Teste salvar corrida com valor do campeonato nulo")
    @Sql({"classpath:/sqls/pais.sql","classpath:/sqls/pista.sql","classpath:/sqls/campeonato.sql", "classpath:/sqls/corrida.sql"})
    void testInsertChampioshipNull() {
        LocalDate data = LocalDate.of(2022, 7, 18);
        LocalTime hora = LocalTime.of(0, 0, 0);
        ZoneId fusoHorario = ZoneId.systemDefault();

        ZonedDateTime dataCorrida = ZonedDateTime.of(data, hora, fusoHorario);
        var corrida = new Race(null, dataCorrida, new Speedway(3, "Pista Curta", 3, new Country(3, "Brasil")), null);

        var exception = assertThrows(
                IntegrityViolation.class, () -> service.insert(corrida));
        assertEquals("Campeonato não pode ser nulo", exception.getMessage());

    }

    @Test
    @DisplayName("Teste salvar corrida com data inválida")
    @Sql({"classpath:/sqls/pais.sql","classpath:/sqls/pista.sql","classpath:/sqls/campeonato.sql", "classpath:/sqls/corrida.sql"})
    void testInsertInvalidDate() {

        var corrida = new Race(null, null, new Speedway(3, "Pista Curta", 3, new Country(3, "Brasil")), new Championship(3,"Pista Curta",2022));

        var exception = assertThrows(
                IntegrityViolation.class, () -> service.insert(corrida));
        assertEquals("Data inválida", exception.getMessage());

    }

    @Test
    @DisplayName("Teste salvar corrida com valor do ano inválido")
    @Sql({"classpath:/sqls/pais.sql","classpath:/sqls/pista.sql","classpath:/sqls/campeonato.sql", "classpath:/sqls/corrida.sql"})
    void testInsertInvalidYear() {
        LocalDate data = LocalDate.of(2023, 7, 18);
        LocalTime hora = LocalTime.of(0, 0, 0);
        ZoneId fusoHorario = ZoneId.systemDefault();
        ZonedDateTime dataCorrida = ZonedDateTime.of(data, hora, fusoHorario);
        var corrida = new Race(null, dataCorrida, new Speedway(3, "Pista Curta", 3, new Country(3, "Brasil")), new Championship(3,"Pista Curta",2022));

        var exception = assertThrows(
                IntegrityViolation.class, () -> service.insert(corrida));
        assertEquals("Ano da corrida diferente do ano do campeonato", exception.getMessage());

    }

    @Test
    @DisplayName("Testes listar todos as corridas cadastradas")
    @Sql({"classpath:/sqls/pais.sql","classpath:/sqls/pista.sql","classpath:/sqls/campeonato.sql", "classpath:/sqls/corrida.sql"})
    void testListAll() {
        List<Race> races = service.listAll();
        assertTrue(!races.isEmpty() && races.size() == 2);
    }

    @Test
    @DisplayName("Teste listar todos sem possuir corrida cadastrada")
    void testListAllIsEmpty() {

        var exception = assertThrows(
                ObjectNotFound.class, () -> service.listAll());
        assertEquals("Não existem corridas cadastradas", exception.getMessage());;

    }

    @Test
    @DisplayName("Teste alterar corrida")
    @Sql({"classpath:/sqls/pais.sql","classpath:/sqls/pista.sql","classpath:/sqls/campeonato.sql", "classpath:/sqls/corrida.sql"})
    void testUpdate() {
        var corrida = service.findById(3);
        String nomeAntesAlterar = corrida.getSpeedway().getName();

        LocalDate data = LocalDate.of(2023, 7, 18);
        LocalTime hora = LocalTime.of(0, 0, 0);
        ZoneId fusoHorario = ZoneId.systemDefault();
        ZonedDateTime dataCorrida = ZonedDateTime.of(data, hora, fusoHorario);

        var corridaAlterada = new Race(3, dataCorrida, new Speedway(4, "Pista Longa", 3, new Country(3, "Brasil")), new Championship(3,"Mundial",2023));
        corridaAlterada =  service.update(corridaAlterada);
        assertEquals("Pista Longa", corridaAlterada.getSpeedway().getName());
        assertNotEquals(nomeAntesAlterar, corridaAlterada.getSpeedway().getName());
    }

    @Test
    @DisplayName("Teste remover corrida")
    @Sql({"classpath:/sqls/pais.sql","classpath:/sqls/pista.sql","classpath:/sqls/campeonato.sql", "classpath:/sqls/corrida.sql"})
    void testDelete() {
        service.delete(3);

        var exception = assertThrows(
                ObjectNotFound.class, () -> service.findById(3));
        assertEquals("Corrida 3 não existe", exception.getMessage());

    }

    @Test
    @DisplayName("Teste buscar corrida pela data")
    @Sql({"classpath:/sqls/pais.sql","classpath:/sqls/pista.sql","classpath:/sqls/campeonato.sql", "classpath:/sqls/corrida.sql"})
    void testFindByDate() {


        LocalDate data = LocalDate.of(2023, 7, 18);
        LocalTime hora = LocalTime.of(0, 0, 0);
        ZoneId fusoHorario = ZoneId.systemDefault();

        var corridas = service.findByDate(ZonedDateTime.of(data, hora, fusoHorario));
        assertTrue(corridas.size() == 1);


        var exception = assertThrows(
                ObjectNotFound.class, () -> service.findByDate(ZonedDateTime.now()));
        assertEquals("Não existe corrida para a data especificada", exception.getMessage());

    }

    @Test
    @DisplayName("Teste buscar corrida pela pista")
    @Sql({"classpath:/sqls/pais.sql","classpath:/sqls/pista.sql","classpath:/sqls/campeonato.sql", "classpath:/sqls/corrida.sql"})
    void testFindBySpeedway() {


        var corridas = service.findBySpeedway(new Speedway(4, "Pista Longa", 3, new Country(3, "Brasil")));
        assertTrue(corridas.size() == 1);


        var exception = assertThrows(
                ObjectNotFound.class, () -> service.findBySpeedway(new Speedway(1, "Pista Média", 3, new Country(3, "Brasil"))));
        assertEquals("Não existe corrida na pista especificada", exception.getMessage());

    }

    @Test
    @DisplayName("Teste buscar corrida pelo campeonato")
    @Sql({"classpath:/sqls/pais.sql","classpath:/sqls/pista.sql","classpath:/sqls/campeonato.sql", "classpath:/sqls/corrida.sql"})
    void testFindByChampionship() {


        var corridas = service.findByChampionship(new Championship(3,"Mundial",2022));
        assertTrue(corridas.size() == 1);


        var exception = assertThrows(
                ObjectNotFound.class, () -> service.findByChampionship(new Championship(1,"Mundial",2022)));
        assertEquals("Não existe corrida para o campeonato especificado", exception.getMessage());

    }


}
