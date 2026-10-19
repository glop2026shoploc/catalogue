package com.glop.catalogue;

import com.glop.catalogue.domain.adapter.out.TestAdapterOut;
import com.glop.catalogue.domain.model.TestModel;
import com.glop.catalogue.domain.service.TestService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestServiceTest {

    @Mock
    TestAdapterOut adapter;

    @InjectMocks
    TestService service;

    @Test
    void shouldReturnMessageAndIncrementCounter() {
        // préparer une réponse fictive
        TestModel model = new TestModel("Bonjour", null, 5);
        when(adapter.findTestInformation()).thenReturn(model);

        // exécuter la méthode
        TestModel result = service.getTestInformation(false);

        assertEquals("Bonjour", result.getMyMessage());
        assertEquals("Test Message", result.getMyOtherMessage());

        // Vérifier l'interaction avec la dépendance
        verify(adapter).incrementRequestNumber();
        verify(adapter).findTestInformation();
    }
}