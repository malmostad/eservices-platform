/* 
 *  Process Aware Web Application Platform 
 * 
 *  Copyright (C) 2011-2013 Inherit S AB 
 * 
 *  This program is free software: you can redistribute it and/or modify 
 *  it under the terms of the GNU Affero General Public License as published by 
 *  the Free Software Foundation, either version 3 of the License, or 
 *  (at your option) any later version. 
 * 
 *  This program is distributed in the hope that it will be useful, 
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 *  GNU Affero General Public License for more details. 
 * 
 *  You should have received a copy of the GNU Affero General Public License 
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>. 
 * 
 *  e-mail: info _at_ inherit.se 
 *  mail: Inherit S AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN 
 *  phone: +46 8 641 64 14 
 */ 
 
/**
 *  Copyright (C) 2004 Orbeon, Inc.
 *
 *  This program is free software; you can redistribute it and/or modify it under the terms of the
 *  GNU Lesser General Public License as published by the Free Software Foundation; either version
 *  2.1 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU Lesser General Public License for more details.
 *
 *  The full text of the license is available at http://www.gnu.org/copyleft/lesser.html
 */
import org.orbeon.oxf.processor.SimpleProcessor;
import org.orbeon.oxf.processor.ProcessorInputOutputInfo;
import org.orbeon.oxf.pipeline.api.PipelineContext;
import org.xml.sax.SAXException;
import org.xml.sax.ContentHandler;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

import java.util.Enumeration;
import java.util.Properties;

public class SystemProperties extends SimpleProcessor {

    private static final Attributes NO_ATTRIBUTES = new AttributesImpl();

    public SystemProperties() {
        addOutputInfo(new ProcessorInputOutputInfo(OUTPUT_DATA));
    }

    public void generateData(PipelineContext context, ContentHandler contentHandler) throws SAXException {
        
        contentHandler.startDocument();
        contentHandler.startElement("", "properties", "properties", NO_ATTRIBUTES);
        Properties systemProperties = System.getProperties();
        for (Enumeration i = systemProperties.propertyNames(); i.hasMoreElements();) {
            String name = (String) i.nextElement();
            String value = systemProperties.getProperty(name);
            if(value != null)
                outputProperty(contentHandler, name, value);
        }
        contentHandler.endElement("", "properties", "properties");
        contentHandler.endDocument();
    }

    private static void outputProperty(ContentHandler contentHandler, String name, String value) throws SAXException {
        contentHandler.startElement("", "property", "property", NO_ATTRIBUTES);
        contentHandler.startElement("", "name", "name", NO_ATTRIBUTES);
        contentHandler.characters(name.toCharArray(), 0, name.length());
        contentHandler.endElement("", "name", "name");
        contentHandler.startElement("", "value", "value", NO_ATTRIBUTES);
        contentHandler.characters(value.toCharArray(), 0, value.length());
        contentHandler.endElement("", "value", "value");
        contentHandler.endElement("", "property", "property");
    }
}