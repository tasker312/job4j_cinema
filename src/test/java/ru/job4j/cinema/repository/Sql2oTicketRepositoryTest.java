package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.ticket.Sql2oTicketRepository;
import ru.job4j.cinema.repository.ticket.TicketRepository;

import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

class Sql2oTicketRepositoryTest {

    private static TicketRepository ticketRepository;

    private static Sql2o sql2o;

    private static List<Ticket> ticketList;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oTicketRepositoryTest.class.getClassLoader()
                .getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }

        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.dataSource(url, username, password);
        sql2o = new Sql2o(datasource);

        ticketRepository = new Sql2oTicketRepository(sql2o);
    }

    @BeforeEach
    public void setUp() {
        ticketList = List.of(
                new Ticket(0, 1, 1, 1, 1),
                new Ticket(0, 1, 1, 2, 1),
                new Ticket(0, 2, 1, 1, 1)
        );

        try (var connection = sql2o.open()) {
            connection.createQuery("SET REFERENTIAL_INTEGRITY = FALSE").executeUpdate();

            var query = connection.createQuery(
                    """
                            INSERT INTO tickets (session_id, row_number, place_number, user_id)
                            VALUES (:sessionId, :rowNumber, :placeNumber, :userId)
                            """,
                    true
            );

            for (var ticket : ticketList) {
                query.addParameter("sessionId", ticket.getSessionId())
                        .addParameter("rowNumber", ticket.getRowNumber())
                        .addParameter("placeNumber", ticket.getPlaceNumber())
                        .addParameter("userId", ticket.getUserId())
                        .addToBatch();
            }

            var generatedKeys = query.executeBatch().getKeys();
            for (int i = 0; i < ticketList.size(); i++) {
                ticketList.get(i).setId((Integer) generatedKeys[i]);
            }
        }

    }

    @AfterEach
    public void destroyRepositories() {
        try (var connection = sql2o.open()) {
            connection.createQuery("DELETE FROM tickets").executeUpdate();
            connection.createQuery("SET REFERENTIAL_INTEGRITY = FALSE").executeUpdate();
        }
    }

    @Test
    public void whenFindByIdThenReturnTicket() {
        var expectedTicket = ticketList.get(0);

        var actualTicket = ticketRepository.findById(expectedTicket.getId());

        assertThat(actualTicket).isPresent().get()
                .usingRecursiveComparison()
                .isEqualTo(expectedTicket);
    }

    @Test
    public void whenFindNonExistentIdWhenReturnOptionalEmpty() {

        var ticket = ticketRepository.findById(-1);

        assertThat(ticket).isEmpty();
    }

    @Test
    public void whenFindBySessionIdThenReturnList() {
        var sessionId = ticketList.get(0).getSessionId();
        var expectedList = ticketList.stream()
                .filter(t -> t.getSessionId() == sessionId)
                .toList();

        var actualList = ticketRepository.findBySession(sessionId);

        assertThat(actualList)
                .usingRecursiveComparison()
                .isEqualTo(expectedList);
    }

    @Test
    public void whenFindNonExistentSessionWhenReturnEmptyList() {

        var tickets = ticketRepository.findBySession(-1);

        assertThat(tickets).isEmpty();
    }

    @Test
    public void whenBookThenReturnTicket() {
        var ticket = new Ticket(0, 5, 5, 5, 1);

        var actualTicket = ticketRepository.book(ticket);
        ticket.setId(actualTicket.get().getId());

        assertThat(actualTicket).isPresent().get()
                .isEqualTo(ticket);
    }

    @Test
    public void whenBookForOccupiedPlaceThenReturnOptionalEmpty() {
        var ticket1 = new Ticket(0, 5, 5, 5, 1);
        var ticket2 = new Ticket(0, 5, 5, 5, 1);

        var actualTicket1 = ticketRepository.book(ticket1);
        ticket1.setId(actualTicket1.get().getId());
        var actualTicket2 = ticketRepository.book(ticket2);

        assertThat(actualTicket1).isPresent().get()
                .isEqualTo(ticket1);
        assertThat(actualTicket2).isEmpty();
    }

}
