package com.mycompany.statdata;

import java.awt.*;
import javax.swing.*;

public class View {
    
    protected JFrame frame;
    protected JButton inputButton;
    protected JButton processButton;
    protected JButton writeButton;
    protected JTextField filePathField;
    
    public View() {    
        frame = new JFrame("Интерфейс для создания статистических данных из вашего файла");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 200);  
        frame.setLayout(new GridLayout(3, 1));

        inputButton = new JButton("Прочитать данные");
        
        processButton = new JButton("Обработать данные");
        processButton.setEnabled(false);
        
        writeButton = new JButton("Записать данные в файл");
        writeButton.setEnabled(false);
        
        
        frame.add(inputButton);
        frame.add(processButton);
        frame.add(writeButton);
       
    }

}

