package com.we.modbus;

import com.we.modbus.model.ModbusMessage;

import java.io.IOException;

/**
 *
 * @author fakadey
 */
public interface ModbusTransport {
    
    /**
     * Метод для отправления сообщения. Возвращаемое значение
     * функции отражает статус отправки.
     * 
     * @Author Pablo
     * 
     * @param msg Модбас сообщения для отправки
     * @return Флаг успешного отправления
     * @throws IOException 
     */
    public boolean sendFrame(ModbusMessage msg) throws IOException;
    
    /**
     * Метод для приема сообщений. Возвращает количество
     * байт принятого сообщения. Метод блокирует выполнение,
     * пока не будет принято сообщение или не разорвется соединение
     * 
     * @Author Pablo
     * 
     * @param msg Принятое модбас сообщение
     * @return Возвращает количество принятых байт
     * @throws IOException 
     */
    public int receiveFrame(ModbusMessage msg) throws IOException;
    
    /**
     * Метод для закрытия соединения.
     * @throws IOException 
     */
    public void close() throws IOException;
}
