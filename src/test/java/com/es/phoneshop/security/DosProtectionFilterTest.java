package com.es.phoneshop.security;

import com.es.phoneshop.security.impl.DosProtectionFilterImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class DosProtectionFilterTest {
    @InjectMocks
    private DosProtectionFilterImpl dosProtectionFilter;
    private static final String FIELD_INSTANCE = "instance";
    @Mock
    private Map<String, Long> countMap = new ConcurrentHashMap();
    @Mock
    private Map<String, Long> timeMap = new ConcurrentHashMap();

    @Before
    public void setup(){
        dosProtectionFilter = Mockito.spy(new DosProtectionFilterImpl());
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnTrueWhenFirstAppealByIp(){
        dosProtectionFilter.isAllowed(anyString(), anyLong());

        verify(timeMap).put(anyString(),anyLong());
        assertTrue(dosProtectionFilter.isAllowed(anyString(), anyLong()));
    }

    @Test
    public void shouldReturnFalseWhenFirstAppealByIp(){
        when(countMap.get(anyString())).thenReturn(21L);
        when(timeMap.get(anyString())).thenReturn(0L);

        dosProtectionFilter.isAllowed(anyString(), anyLong());

        assertFalse(dosProtectionFilter.isAllowed(anyString(), anyLong()));
    }
}
