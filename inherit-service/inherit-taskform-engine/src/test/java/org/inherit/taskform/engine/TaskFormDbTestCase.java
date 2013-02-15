package org.inherit.taskform.engine;

import java.util.List;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.inherit.service.common.domain.ActivityInstanceItem;
import org.inherit.service.common.domain.Tag;
import org.inherit.taskform.engine.persistence.HibernateUtil;
import org.inherit.taskform.engine.persistence.TaskFormDb;
import org.inherit.taskform.engine.persistence.entity.ProcessActivityFormInstance;
import org.inherit.taskform.engine.persistence.entity.TagType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TaskFormDbTestCase {

	SessionFactory sessionFactory;
	
	@Before
	public void before() {
		System.out.println("before start");
		Configuration cfg = new AnnotationConfiguration()
	    .addAnnotatedClass(org.inherit.taskform.engine.persistence.entity.StartFormDefinition.class)
	    .addAnnotatedClass(org.inherit.taskform.engine.persistence.entity.ActivityFormDefinition.class)
	    .addAnnotatedClass(org.inherit.taskform.engine.persistence.entity.ProcessActivityFormInstance.class)
	    .addAnnotatedClass(org.inherit.taskform.engine.persistence.entity.TagType.class)
	    .addAnnotatedClass(org.inherit.taskform.engine.persistence.entity.ProcessActivityTag.class);

	    cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
		cfg.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
		cfg.setProperty("hibernate.connection.url", "jdbc:h2:mem:InheritPlatform");
		cfg.setProperty("hibernate.hbm2ddl.auto", "create");
	    cfg.setProperty("show_sql", "true");
		
	    sessionFactory = cfg.buildSessionFactory();
		
	    
		HibernateUtil.setSessionFactory(sessionFactory);
		System.out.println("before finished");
	}
	
	@After
	 public void after() {
		HibernateUtil.getSessionFactory().close();
	 }

	
	@Test
	public void tags() {
		
		String formPath = "test/formpath";
		String userId = "admin";
		String processInstanceUuid1 = "test_proc_uuid1";
		String processInstanceUuid2 = "test_proc_uuid2";
	
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		TagType diaryTagType = new TagType();
		diaryTagType.setTagTypeId(TagType.TAG_TYPE_DIARY_NO);
		diaryTagType.setName("diary_no");
		diaryTagType.setLabel("Diarienr");

		TagType applicationByTagType = new TagType();
		applicationByTagType.setTagTypeId(TagType.TAG_APPLICATION_BY);
		applicationByTagType.setName("application_by");
		applicationByTagType.setLabel("Ansökan av");

		TagType otherTagType = new TagType();
		otherTagType.setTagTypeId(TagType.TAG_OTHER);
		otherTagType.setName("other");
		otherTagType.setLabel("Annan");

		session.save(diaryTagType);
		session.save(applicationByTagType);
		session.save(otherTagType);
		session.getTransaction().commit();
		session.close();
	
		TaskFormDb taskFormDb = new TaskFormDb();
		
		ProcessActivityFormInstance pafi1 = new ProcessActivityFormInstance();
		pafi1.setFormDocId("testDocId");
		pafi1.setFormPath(formPath);
		pafi1.setUserId(userId);
		pafi1.setProcessInstanceUuid(processInstanceUuid1);		
		taskFormDb.save(pafi1);

		ProcessActivityFormInstance pafi2 = new ProcessActivityFormInstance();
		pafi2.setFormDocId("testDocId2");
		pafi2.setFormPath(formPath);
		pafi2.setUserId(userId);
		pafi2.setProcessInstanceUuid(processInstanceUuid2);		
		taskFormDb.save(pafi2);

		// add tags 
		taskFormDb.addTag(pafi1.getProcessActivityFormInstanceId(), TagType.TAG_APPLICATION_BY, "121212-1212", userId);
		taskFormDb.addTag(pafi1.getProcessActivityFormInstanceId(), TagType.TAG_TYPE_DIARY_NO, "1234567/11", userId);

		taskFormDb.addTag(pafi2.getProcessActivityFormInstanceId(), TagType.TAG_TYPE_DIARY_NO, "1234567/22", userId);

		// check tags count
		List<Tag> tags1 = taskFormDb.getTagsByProcessInstance(processInstanceUuid1);
		Assert.assertEquals(2, tags1.size());
		
		List<Tag> tags2 = taskFormDb.getTagsByProcessInstance(processInstanceUuid2);
		Assert.assertEquals(1, tags2.size());
		
		// delete tag by processInstanceUuid and value
		Assert.assertTrue(taskFormDb.deleteTag(processInstanceUuid1, "121212-1212", userId));
		Assert.assertFalse(taskFormDb.deleteTag(processInstanceUuid1, "121212-5678", userId));
		
		// check tags count
		tags1 = taskFormDb.getTagsByProcessInstance(processInstanceUuid1);
		Assert.assertEquals(1, tags1.size());
		
		tags2 = taskFormDb.getTagsByProcessInstance(processInstanceUuid2);
		Assert.assertEquals(1, tags2.size());

		// delete tags by id
		for (Tag tag : tags1) {
			Assert.assertTrue(taskFormDb.deleteTag(tag.getProcessActivityTagId(), userId));
		}
		for (Tag tag : tags2) {
			Assert.assertTrue(taskFormDb.deleteTag(tag.getProcessActivityTagId(), userId));
		}
		
		// check tags count
		tags1 = taskFormDb.getTagsByProcessInstance(processInstanceUuid1);
		Assert.assertEquals(0, tags1.size());
		
		tags2 = taskFormDb.getTagsByProcessInstance(processInstanceUuid2);
		Assert.assertEquals(0, tags2.size());
		
		

	}
}
