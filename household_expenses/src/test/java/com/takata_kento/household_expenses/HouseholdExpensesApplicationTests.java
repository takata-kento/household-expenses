package com.takata_kento.household_expenses;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class HouseholdExpensesApplicationTests {

    @Test
    void contextLoads() {}
}
