package br.com.erudio.business;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.jupiter.api.Test;

public class ListMockBDDTest {

	@Test
	void testMockingList_When_SizeIsCalled_ShouldReturnMultipleValues() {

		List<?> list = mock(List.class);
		given(list.size()).willReturn(10).willReturn(20);

		assertThat(list.size(), is(10));
		assertThat(list.size(), is(20));

	}

	@Test
	void testMocckingList_When_GetIsCalled_ShouldReturnErudio() {

		// Given / Arrange
		var list = mock(List.class);
		given(list.get(0)).willReturn("Erudio");

		// When / Act & Then / Assert
		assertThat(list.get(0), is("Erudio"));
		assertThat(list.get(1), nullValue());
	}

	@Test
	void testMocckingList_When_GetIsCalledWithArgumentMatcher_ShouldReturnErudio() {

		// Given / Arrange
		var list = mock(List.class);

		// If you are using argument matchers, all arguments
		// have to be provided by matchers.
		given(list.get(anyInt())).willReturn("Erudio");

		// When / Act & Then / Assert
		assertThat(list.get(anyInt()), is("Erudio"));
		assertThat(list.get(anyInt()), is("Erudio"));
	}

	@Test
	void testMockingList_When_ThrowsAnException() {

		// Given / Arrange
		var list = mock(List.class);

		// If you are using argument matchers, all arguments
		// have to be provided by matchers.
		given(list.get(anyInt())).willThrow(new RuntimeException("Foo Bar!!"));

		// When / Act & Then / Assert

		assertThrows(RuntimeException.class, () -> {
			// When / Act
			list.get(anyInt());
		}, () -> "Should have throw an RuntimeException");

	}
}
