package br.com.erudio.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

public class ListMockTest {

	@Test
	void testMockingList_When_SizeIsCalled_ShouldReturnMultipleValues() {

		List<?> list = mock(List.class);
		when(list.size()).thenReturn(10).thenReturn(20);

		assertEquals(10, list.size());
		assertEquals(20, list.size());

	}

	@Test
	void testMocckingList_When_GetIsCalled_ShouldReturnErudio() {

		// Given / Arrange
		var list = mock(List.class);
		when(list.get(0)).thenReturn("Erudio");

		// When / Act & Then / Assert
		assertEquals("Erudio", list.get(0));
		assertNull(list.get(1));
	}

	@Test
	void testMocckingList_When_GetIsCalledWithArgumentMatcher_ShouldReturnErudio() {

		// Given / Arrange
		var list = mock(List.class);

		// If you are using argument matchers, all arguments
		// have to be provided by matchers.
		when(list.get(anyInt())).thenReturn("Erudio");

		// When / Act & Then / Assert
		assertEquals("Erudio", list.get(anyInt()));
		assertEquals("Erudio", list.get(anyInt()));
	}

	@Test
	void testMockingList_When_ThrowsAnException() {

		// Given / Arrange
		var list = mock(List.class);

		// If you are using argument matchers, all arguments
		// have to be provided by matchers.
		when(list.get(anyInt())).thenThrow(new RuntimeException("Foo Bar!!"));

		// When / Act & Then / Assert
		assertThrows(RuntimeException.class, () -> {
			// When / Act
			list.get(anyInt());
		}, () -> "Should have throw an RuntimeException");
	}
}
