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
 
/*
    Copyright 2004 Orbeon, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
import org.orbeon.oxf.common.OXFException;
import org.orbeon.oxf.pipeline.api.PipelineContext;
import org.orbeon.oxf.pipeline.api.XMLReceiver;
import org.orbeon.oxf.processor.DatabaseContext;
import org.orbeon.oxf.processor.Datasource;
import org.orbeon.oxf.processor.ProcessorInputOutputInfo;
import org.orbeon.oxf.processor.SimpleProcessor;
import org.orbeon.oxf.xml.ContentHandlerHelper;
import org.xml.sax.SAXException;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

/**
 * This processor takes a datasource as input and generates a document containing all the tables
 * starting with "ORBEON_".
 */
public class ListInitializedTables extends SimpleProcessor {

    private static final String INPUT_DATASOURCE = "datasource";

    public ListInitializedTables() {
        addInputInfo(new ProcessorInputOutputInfo(INPUT_DATASOURCE));
        addOutputInfo(new ProcessorInputOutputInfo(OUTPUT_DATA));
    }

    public void generateData(PipelineContext pipelineContext, XMLReceiver xmlReceiver) throws SAXException {
        try {
            // Get connection
            Datasource datasource = Datasource.getDatasource(pipelineContext, this, getInputByName(INPUT_DATASOURCE));
            Connection connection = DatabaseContext.getConnection(pipelineContext, datasource);

            // Determine if we have tables in the database
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, "ORBEON_%", null);

            // Send the result as XML, outputting one row per table
            ContentHandlerHelper contentHandlerHelper = new ContentHandlerHelper(xmlReceiver);
            contentHandlerHelper.startDocument();
            contentHandlerHelper.startElement("tables");
            while (tables.next()) {
                contentHandlerHelper.startElement("table");
                contentHandlerHelper.startElement("name");
                contentHandlerHelper.text(tables.getString("TABLE_NAME"));
                contentHandlerHelper.endElement();
                contentHandlerHelper.endElement();
            }
            contentHandlerHelper.endElement();
            contentHandlerHelper.endDocument();
        } catch (Exception e) {
            throw new OXFException(e);
        }
    }
}