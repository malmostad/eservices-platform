package org.motrice.orifice;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class must be mentioned in conf/spring/resources.groovy.
 * Important interfaces:
 * java.beans.PropertyEditor
 * java.beans.PropertyEditorSupport implements PropertyEditor
 * CustomDateEditor extends PropertyEditorSupport
 */
public class CustomDateEditorRegistrar implements PropertyEditorRegistrar {
    public void registerCustomEditors(PropertyEditorRegistry registry) {
      // registerCustomEditor(Class requiredType, PropertyEditor propertyEditor)
      registry.registerCustomEditor(Date.class, new ApplicationDateEditor());
  }
}
