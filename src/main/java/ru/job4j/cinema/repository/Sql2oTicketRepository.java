package ru.job4j.cinema.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import ru.job4j.cinema.model.Ticket;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class Sql2oTicketRepository implements TicketRepository {

    private Sql2o sql2o;

    @Override
    public Optional<Ticket> book(Ticket ticket) {
        try (Connection connection = sql2o.open()) {
            var query = connection.createQuery(
                            """
                                    INSERT INTO tickets (session_id, row_number, place_number, user_id)
                                    Values (:session_id, :row_number, :place_number, :user_id)
                                    """,
                            true
                    )
                    .addParameter("session_id", ticket.getSessionId())
                    .addParameter("row_number", ticket.getRowNumber())
                    .addParameter("place_number", ticket.getPlaceNumber())
                    .addParameter("user_id", ticket.getUserId());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            ticket.setId(generatedId);
            return Optional.of(ticket);
        } catch (Sql2oException e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

}
