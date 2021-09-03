package com.we.modbus;

import com.we.modbus.model.ModbusMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.io.IOException;

/**
 *
 * @author fakadey
 */
public class Modbus {
    
	private static final Logger logger = LoggerFactory.getLogger(Modbus.class);  
    public static final Marker modbusMarker = MarkerFactory.getMarker("MODBUS");
    /**
     * Максимальный допустимый адрес ведомого
     */
    public static final int ADDRESS_MAX = 65535;
    
    /**
     * Максимальная длина сообщения
     */
    public static final int MAX_MESSAGE_LENGTH = 255;
    
    /**
     * Максимальное значение для 16 бит-го типа UINT
     */
    public static final int UINT16_MAX = (int) 0xFFFF;
    
    /**
     * Минимальное значения для 16 бит-го типа UINT
     */
    public static final int UINT16_MIN = (int) 0x0000;
    
    /**
     * Максимальное значение для 8 бит-го типа UINT
     */
    public static final int UINT8_MAX = (int) 0xFF;
    
    /**
     * Минимальное значение для 8 бит-го типа UINT
     */
    public static final int UINT8_MIN = (int) 0x00;
    
    /**
     * Модификатор кода функции. Этот модификатор добавляется
     * к коду функции для сигнализации ошибки
     */
    public static final byte EXCEPTION_MODIFIER = (byte) 0x80;
    
    /**
     * Modbus Transport объект через который будет прозиводится
     * весь обмен данными
     */
    protected ModbusTransport transport;
    
    /**
     * Конструктор класса. Инициализирует поле типа
     * ModbusTransport
     * 
     * @author Pablo
     * 
     * @param transport Объект типа ModbusTransport, который
     * используется для отправления и приема посылок
     */
    protected Modbus(ModbusTransport transport){
        this.transport = transport;
    }
    
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
    public boolean sendFrame(ModbusMessage msg) throws IOException{
        logger.debug(modbusMarker, "Модбас: Отправление данных {}", msg);
        return transport.sendFrame(msg);
    }
    
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
    public int receiveFrame(ModbusMessage msg) throws IOException{
        int result = transport.receiveFrame(msg);
        if (result > 0){
        	logger.debug(modbusMarker, "Модбас прием: {}", msg);
        }
    	return result;
    }
    
    /**
     * Метод для закрытия соединения
     * @throws IOException 
     */
    public void close() throws IOException{
    	transport.close();
    }
        	
}
