/* == Motrice Copyright Notice ==
 *
 * Motrice Service Platform
 *
 * Copyright (C) 2011-2014 Motrice AB
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * e-mail: info _at_ motrice.se
 * mail: Motrice AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN
 * phone: +46 8 641 64 14
 */
import org.activiti.spring.ProcessEngineFactoryBean
import org.activiti.spring.SpringProcessEngineConfiguration
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.SimpleDriverDataSource
import org.springframework.jmx.export.MBeanExporter
import org.springframework.jmx.support.MBeanServerFactoryBean
import org.springframework.jmx.export.annotation.AnnotationJmxAttributeSource
import org.springframework.jmx.export.assembler.MetadataMBeanInfoAssembler
import org.springframework.jmx.export.naming.MetadataNamingStrategy
import org.motrice.jmx.BasicAppManagement

// Place your Spring DSL code here
beans = {
  // Custom date editor for automatic data binding when converting
  // XML to domain objects
  customPropertyEditorRegistrar(org.motrice.migratrice.CustomDateEditorRegistrar)

  // The following stuff wires Spring components together for exporting
  // the management bean to JMX.
  // The convenience of using annotation brings some extra components.
  mbeanServer(MBeanServerFactoryBean) {
    locateExistingServerIfPossible = true
  }

  // Tells the MBeanExporter to pick up data from bean annotations
  jmxAttributeSource(AnnotationJmxAttributeSource)

  // The assembler puts the various pieces together
  jmxAssembler(MetadataMBeanInfoAssembler) {
    attributeSource = jmxAttributeSource
  }

  // A naming strategy is needed
  jmxNamingStrategy(MetadataNamingStrategy) {
    attributeSource = jmxAttributeSource
  }

  jmxExporter(MBeanExporter) {exporter ->
    server = mbeanServer
    assembler = jmxAssembler
    namingStrategy = jmxNamingStrategy
  }

  // Our JMX management bean
  basicJmxManagement(BasicAppManagement)

  // Activiti setup -- requires a transaction manager
  // Note: The data source placeholders must use single quotes
  // Placeholders get their values from normal config
  activitiDataSource(SimpleDriverDataSource) {
    driverClass = '${dataSource.driverClassName}'
    url = '${dataSource.url}'
    username = '${dataSource.username}'
    password = '${dataSource.password}'
  }

  activitiTxManager(DataSourceTransactionManager) {
    dataSource = activitiDataSource
  }

  activitiConfiguration(SpringProcessEngineConfiguration) {
    dataSource = activitiDataSource
    transactionManager = activitiTxManager
    databaseSchemaUpdate = false
    jobExecutorActivate = false
  }

  processEngine(ProcessEngineFactoryBean) {
    processEngineConfiguration = activitiConfiguration
  }

  activitiRepositoryService(processEngine: "getRepositoryService")
  activitiManagementService(processEngine: "getManagementService")

}
