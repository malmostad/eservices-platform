package org.inheritsource.test.service.processengine;

import java.util.Locale;

import org.inheritsource.service.coordinatrice.CoordinatriceFacade;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CoordinatriceFacadeTest {
	
	CoordinatriceFacade c;
	
	@Before
	public void before() {
		
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/applicationContext.xml");
        
        
		
		c = (CoordinatriceFacade)applicationContext.getBean("coordinatriceFacade");
		
	}
	
	@Test
	public void testGetLabel() {
		
		System.out.println("testGetLabel:" + c.getLabel("ForenkladDelgivning", "sv", "Ta del av", 1));
	}
	
	@Test
	public void testGetLabel2() {
		Locale locale = new Locale("sv", "SE");
		System.out.println("testGetLabel2:" + c.getLabel("ForenkladDelgivning:1:10", "Ta del av", locale));
	}

}
