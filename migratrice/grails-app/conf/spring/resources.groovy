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

}
