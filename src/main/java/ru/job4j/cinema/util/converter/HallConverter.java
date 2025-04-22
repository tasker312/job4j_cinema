package ru.job4j.cinema.util.converter;

import ru.job4j.cinema.dto.HallDTO;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.model.Ticket;

import java.util.Collection;

public class HallConverter {

    public static HallDTO convert(Hall hall, Collection<Ticket> tickets) {
        var hallDTO = HallDTO.builder()
                .id(hall.getId())
                .name(hall.getName())
                .rowCount(hall.getRowCount())
                .placeCount(hall.getPlaceCount())
                .description(hall.getDescription())
                .seats(new boolean[hall.getRowCount()][hall.getPlaceCount()])
                .build();
        tickets.forEach(ticket -> hallDTO.getSeats()[ticket.getRowNumber() - 1][ticket.getPlaceNumber() - 1] = true);
        return hallDTO;
    }

}
