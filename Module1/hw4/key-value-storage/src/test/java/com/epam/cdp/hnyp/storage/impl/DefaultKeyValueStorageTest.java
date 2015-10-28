package com.epam.cdp.hnyp.storage.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.epam.cdp.hnyp.storage.exception.StorageException;
import com.epam.cdp.hnyp.storage.key.KeyDescriptor;
import com.epam.cdp.hnyp.storage.key.KeyStorage;
import com.epam.cdp.hnyp.storage.value.ValueStorage;

@RunWith(MockitoJUnitRunner.class)
public class DefaultKeyValueStorageTest {

    @Mock
    private KeyStorage mockKeyStorage;
    @Mock
    private ValueStorage mockValueStorage;
    
    @InjectMocks
    private DefaultKeyValueStorage keyValueStorage;
    
    @Captor
    private ArgumentCaptor<KeyDescriptor> captorDescriptor;
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowException_whenCreateWithEmptyKey() throws StorageException {
        keyValueStorage.create(StringUtils.EMPTY, "value");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowException_whenCreateWithNullValue() throws StorageException {
        keyValueStorage.create("key", null);
    }
    
    @Test
    public void shouldSaveKeyThenValueAndReturnTrue_whenCreate() throws StorageException {
        String key = "key", value = "value";
        KeyDescriptor keyDescriptor = new KeyDescriptor(key, 10, 1, 0, value.getClass());
        when(mockKeyStorage.create(eq(key), anyInt(), any()))
            .thenReturn(keyDescriptor);
        
        boolean result = keyValueStorage.create(key, value);
        assertTrue(result);
        
        InOrder order = inOrder(mockKeyStorage, mockValueStorage);
        order.verify(mockValueStorage).calculateLength(value);
        order.verify(mockKeyStorage).create(eq(key), anyInt(), any());
        order.verify(mockValueStorage).writeValue(keyDescriptor, value);
    }
    
    @Test
    public void shouldUseCalculatedValueLengthForKeyDescriptor_whenCreate() throws StorageException {
        String key = "key", value = "value";
        int valueLength = 10;
        when(mockValueStorage.calculateLength(value))
            .thenReturn(valueLength);
        
        keyValueStorage.create(key, value);
        
        verify(mockKeyStorage).create(key, valueLength, value.getClass());
    }
    
    @Test
    public void shouldUseValueClassToSaveKeyDescriptor_whenCreate() throws StorageException {
        String key = "key", value = "value";

        keyValueStorage.create(key, value);
        
        verify(mockKeyStorage).create(eq(key), anyInt(), eq(value.getClass()));
    }
    
    @Test
    public void shouldNotWriteValue_whenKeyIsNotCreatedDuringCreate() throws StorageException {
        String key = "key", value = "value";
        when(mockKeyStorage.create(eq(key), anyInt(), any()))
            .thenReturn(null);
        
        boolean result = keyValueStorage.create(key, value);
        assertFalse(result);
        
        verify(mockValueStorage, never()).writeValue(any(), eq(value));
    }
    
    @Test
    public void shouldNotWriteValueAndThrowException_whenExceptionRaisedDuringKeyCreate() throws StorageException {
        String key = "key", value = "value";
        when(mockKeyStorage.create(eq(key), anyInt(), any()))
            .thenThrow(StorageException.class);
        
        try {
            keyValueStorage.create(key, value);
            fail();
        } catch (StorageException e) {}
        
        verify(mockValueStorage, never()).writeValue(any(), eq(value));
    }
    
    @Test
    public void shouldDeleteKeyAndThrowException_whenWritingValueFailedDuringCreate() throws StorageException {
        String key = "key", value = "value";
        KeyDescriptor keyDescriptor = new KeyDescriptor(key, 10, 1, 0, value.getClass());
        when(mockKeyStorage.create(eq(key), anyInt(), any()))
            .thenReturn(keyDescriptor);
        doThrow(StorageException.class)
            .when(mockValueStorage).writeValue(keyDescriptor, value);
        
        try {
            keyValueStorage.create(key, value);
            fail();
        } catch (StorageException e) {}
        
        verify(mockKeyStorage).create(eq(key), anyInt(), eq(value.getClass()));
        verify(mockKeyStorage).delete(key);
    }
    
    @Test
    public void shouldReturnNull_whenKeyIsNotFound() throws StorageException {
        String key = "key";
        when(mockKeyStorage.read(key)).thenReturn(null);
        Object result = keyValueStorage.read(key);
        assertNull(result);
    }
    
    @Test
    public void shouldReturnReadValue_whenKeyFound() throws StorageException {
        String key = "key", value = "value";
        KeyDescriptor keyDescriptor = new KeyDescriptor(key, 10, 1, 0, value.getClass());
        when(mockKeyStorage.read(key))
            .thenReturn(keyDescriptor);
        when(mockValueStorage.readValue(keyDescriptor))
            .thenReturn(value);
        
        Object result = keyValueStorage.read(key);
        assertEquals(value, result);
    }
    
    @Test(expected=StorageException.class)
    public void shouldThrowException_whenReadingValueFailed() throws StorageException {
        String key = "key", value = "value";
        KeyDescriptor keyDescriptor = new KeyDescriptor(key, 10, 1, 0, value.getClass());
        when(mockKeyStorage.read(key))
            .thenReturn(keyDescriptor);
        when(mockValueStorage.readValue(keyDescriptor))
            .thenThrow(StorageException.class);
        
        keyValueStorage.read(key); 
    }
    
    @Test
    public void shouldUpdateDescriptor_whenUpdate() throws StorageException {
        String key = "key", value = "value";
        KeyDescriptor keyDescriptor = new KeyDescriptor(key, 10, 1, 0, value.getClass());
        when(mockKeyStorage.read(key))
            .thenReturn(keyDescriptor);
        
        keyValueStorage.update(key, value);
        
        verify(mockValueStorage).updateKeyDescriptor(captorDescriptor.capture(), eq(value));
        assertEquals(keyDescriptor.getKey(), captorDescriptor.getValue().getKey());
    }
    
    @Test
    public void shouldUpdateOldKeyDescriptorAndWriteNewValue_whenUpdate() throws StorageException {
        String key = "key", value = "value";
        String newValue = "newValue";
        KeyDescriptor keyDescriptor = new KeyDescriptor(key, 10, 1, 0, value.getClass());
        when(mockKeyStorage.read(key))
            .thenReturn(keyDescriptor);
        when(mockKeyStorage.update(any()))
            .thenReturn(true);
        
        boolean result = keyValueStorage.update(key, newValue);
        assertTrue(result);
        
        verify(mockKeyStorage).read(key);
        verify(mockValueStorage).updateKeyDescriptor(captorDescriptor.capture(), eq(newValue));
        KeyDescriptor updatedDescriptor = captorDescriptor.getValue();
        verify(mockKeyStorage).update(updatedDescriptor);
        verify(mockValueStorage).writeValue(updatedDescriptor, newValue);
    }
    
    @Test
    public void shouldRestoreOldDescriptor_whenWritingUpdateValueFailed() throws StorageException {
        String key = "key", value = "value";
        String newValue = "newValue";
        KeyDescriptor keyDescriptor = new KeyDescriptor(key, 10, 1, 0, value.getClass());
        when(mockKeyStorage.read(key))
            .thenReturn(keyDescriptor);
        when(mockKeyStorage.update(any()))
            .thenReturn(true);
        doThrow(StorageException.class)
            .when(mockValueStorage).writeValue(any(), eq(newValue));
        
        try {
            keyValueStorage.update(key, newValue);
            fail();
        } catch (StorageException e) {}
        
        verify(mockKeyStorage).update(keyDescriptor);
    }
    
    @Test
    public void shouldReturnFalse_whenUpdateAndKeyNotFound() throws StorageException {
        String key = "key";
        String newValue = "newValue";
        when(mockKeyStorage.read(key))
            .thenReturn(null);
        
        boolean result = keyValueStorage.update(key, newValue);
        assertFalse(result);
    }
    
    @Test(expected=StorageException.class)
    public void shouldThrowException_whenUpdateKeyFailed() throws StorageException {
        String key = "key", value = "value";
        String newValue = "newValue";
        KeyDescriptor keyDescriptor = new KeyDescriptor(key, 10, 1, 0, value.getClass());
        when(mockKeyStorage.read(key))
            .thenReturn(keyDescriptor);
        when(mockKeyStorage.update(any()))
            .thenThrow(StorageException.class);
        
        keyValueStorage.update(key, newValue);
    }
    
    @Test
    public void shouldNotReadValue_whenKeyDoesntExistDuringRead() throws StorageException {
        String key = "key";
        when(mockKeyStorage.read(key))
            .thenReturn(null);
        
        Object result = keyValueStorage.read(key);
        assertNull(result);
        
        verify(mockValueStorage, never()).readValue(any());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void shouldThrowException_whenReadingEmptyKey() throws StorageException {
        keyValueStorage.read(StringUtils.EMPTY);
    }
    
    @Test
    public void shouldReturnReadValue_whenReadingKey() throws StorageException {
        String key = "key", value = "value";
        KeyDescriptor keyDescriptor = new KeyDescriptor(key, 10, 1, 0, value.getClass());
        when(mockKeyStorage.read(key))
            .thenReturn(keyDescriptor);
        when(mockValueStorage.readValue(keyDescriptor))
            .thenReturn(value);
        
        Object result = keyValueStorage.read(key);
        assertEquals(value, result);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void shouldThrowException_whenDeletingEmptyKey() throws StorageException {
        keyValueStorage.delete(StringUtils.EMPTY);
    }
    
    @Test
    public void shouldReturnTrue_whenKeyDeleted() throws StorageException {
        String key = "key";
        when(mockKeyStorage.delete(key))
            .thenReturn(true);
        
        boolean result = keyValueStorage.delete(key);
        assertTrue(result);
    }
    
    @Test
    public void shouldReturnFalse_whenKeyIsNotDeleted() throws StorageException {
        String key = "key";
        when(mockKeyStorage.delete(key))
            .thenReturn(false);
        
        boolean result = keyValueStorage.delete(key);
        assertFalse(result);
    }

}
