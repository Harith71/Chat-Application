import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MyTest {
	Coffee order1;
	Coffee order2;
	Coffee order3;
	Coffee order4;
	Coffee order5;

	@BeforeEach
	void initialize() {
		order1 = new Sugar( new Cream( CoffeeFactory.getCoffee("lr")));
		order2 = new Sugar( new Cream( CoffeeFactory.getCoffee("cb")));
		order3 = new Sugar( new Cream( CoffeeFactory.getCoffee("dr")));
		order4 = new Cream( CoffeeFactory.getCoffee("lr"));
		order5 = new Cream( CoffeeFactory.getCoffee("dr"));
	}



	@Test
	void t1() {
		double cost = order1.makeCoffee();
		assertEquals(cost, 4.5);
	}

	@Test
	void t2() {
		double cost = order2.makeCoffee();
		assertEquals(cost, 4.5);
	}

	@Test
	void t3() {
		double cost = order3.makeCoffee();
		assertEquals(cost, 4.5);
	}

	@Test
	void t4() {
		double cost = order4.makeCoffee();
		assertEquals(cost, 4.0);
	}

	@Test
	void t5() {
		double cost = order5.makeCoffee();
		assertEquals(cost, 4.0);
	}

}
