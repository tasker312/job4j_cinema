package ru.job4j.cinema.controller;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IndexControllerTest {

    @Test
    public void whenGetIndexThenReturnIndex() {
        var indexController = new IndexController();

        var view = indexController.getIndex();

        assertThat(view).isEqualTo("index");
    }

}
