package com.example.apachecamel.camel.processors;

import org.apache.camel.Exchange;

import com.example.apachecamel.camel.models.DataInputFile;

public class TransportFileProcessor{

    public static DataInputFile getDataFile(Exchange exchange){
        
        DataInputFile dataInputFile = new DataInputFile();
        String[] data = exchange.getIn().getBody(String.class).split(",");

        dataInputFile.setName(data[0]);
        dataInputFile.setEmail(data[1]);
        dataInputFile.setPhoneNro(data[2]);
        dataInputFile.setSalary(Double.parseDouble(data[3]));
        
        return dataInputFile;

    }

}
