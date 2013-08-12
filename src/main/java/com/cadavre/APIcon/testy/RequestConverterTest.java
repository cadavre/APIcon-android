package com.cadavre.APIcon.testy;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import retrofit.client.Header;
import retrofit.client.Request;

/**
 * APIcon class
 *
 * @author Seweryn Zeman
 */
public class RequestConverterTest implements Converter {

    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {

        Request request = (Request) source;
        writer.startNode("method");
        writer.setValue(request.getMethod());
        writer.endNode();
        writer.startNode("url");
        writer.setValue(request.getUrl());
        writer.endNode();
        writer.startNode("headers");
        for (Header header : request.getHeaders()) {
            writer.startNode("header");
            {
                writer.startNode("name");
                writer.setValue(header.getName());
                writer.endNode();
                writer.startNode("value");
                writer.setValue(header.getValue());
                writer.endNode();
            }
            writer.endNode();
        }
        writer.endNode();
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {

        reader.moveDown();
        String val = reader.getValue();
        reader.moveUp();
        reader.moveDown();
        String val2 = reader.getValue();
        reader.moveUp();

        Request request = new Request("", "", null, null);

        return request;
    }

    @Override
    public boolean canConvert(Class type) {

        return type.equals(Request.class);
    }
}
