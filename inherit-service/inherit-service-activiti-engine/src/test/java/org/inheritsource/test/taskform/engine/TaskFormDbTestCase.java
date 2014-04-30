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
 
 
package org.inheritsource.test.taskform.engine;

import java.util.List;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.inheritsource.service.common.domain.ActivityInstanceItem;
import org.inheritsource.service.common.domain.ProcessInstanceListItem;
import org.inheritsource.service.common.domain.Tag;
import org.inheritsource.service.common.domain.UserInfo;
import org.inheritsource.taskform.engine.persistence.HibernateUtil;
import org.inheritsource.taskform.engine.persistence.TaskFormDb;
import org.inheritsource.taskform.engine.persistence.entity.ProcessActivityFormInstance;
import org.inheritsource.taskform.engine.persistence.entity.TagType;
import org.inheritsource.taskform.engine.persistence.entity.UserEntity;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TaskFormDbTestCase {

	SessionFactory sessionFactory;
	
	TaskFormDb taskFormDb = new TaskFormDb();

	String formPath = "test/formpath";
	String userId = "admin";
	String processInstanceUuid1 = "test_proc_uuid1";
	String processInstanceUuid2 = "test_proc_uuid2";
	String tagVal1 = "121212-1212";
	String tagVal2 = "1234567/22";
	Long processActivityFormInstanceId1;
	Long processActivityFormInstanceId2;

	@Before
	public void before() {
		Configuration cfg = new AnnotationConfiguration()
	    .addAnnotatedClass(org.inheritsource.taskform.engine.persistence.entity.StartFormDefinition.class)
	    .addAnnotatedClass(org.inheritsource.taskform.engine.persistence.entity.ActivityFormDefinition.class)
	    .addAnnotatedClass(org.inheritsource.taskform.engine.persistence.entity.ProcessActivityFormInstance.class)
	    .addAnnotatedClass(org.inheritsource.taskform.engine.persistence.entity.TagType.class)
	    .addAnnotatedClass(org.inheritsource.taskform.engine.persistence.entity.ProcessActivityTag.class)
	    .addAnnotatedClass(org.inheritsource.taskform.engine.persistence.entity.UserEntity.class);

	    cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
		cfg.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
		cfg.setProperty("hibernate.connection.url", "jdbc:h2:mem:InheritPlatform");
		cfg.setProperty("hibernate.hbm2ddl.auto", "create");
	    cfg.setProperty("show_sql", "true");
		
	    sessionFactory = cfg.buildSessionFactory();
		
		HibernateUtil.setSessionFactory(sessionFactory);

		
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
	
		
		ProcessActivityFormInstance pafi1 = new ProcessActivityFormInstance();
		pafi1.setFormDocId("testDocId");
		pafi1.setFormConnectionKey(formPath);
		pafi1.setUserId(userId);
		pafi1.setProcessInstanceUuid(processInstanceUuid1);		
		taskFormDb.save(pafi1);

		ProcessActivityFormInstance pafi2 = new ProcessActivityFormInstance();
		pafi2.setFormDocId("testDocId2");
		pafi2.setFormConnectionKey(formPath);
		pafi2.setUserId(userId);
		pafi2.setProcessInstanceUuid(processInstanceUuid2);		
		taskFormDb.save(pafi2);

		processActivityFormInstanceId1 = pafi1.getProcessActivityFormInstanceId();
		processActivityFormInstanceId2 = pafi2.getProcessActivityFormInstanceId();
	}
	
	@After
	 public void after() {
		HibernateUtil.getSessionFactory().close();
	 }

	/*
	@Test
	public void tags() {
		// add tags 
		taskFormDb.addTag(processActivityFormInstanceId1, TagType.TAG_APPLICATION_BY, tagVal1, userId);
		taskFormDb.addTag(processActivityFormInstanceId1, TagType.TAG_TYPE_DIARY_NO, tagVal2 , userId);

		taskFormDb.addTag(processActivityFormInstanceId2, TagType.TAG_TYPE_DIARY_NO, tagVal2, userId);

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
	
	@Test
	public void searchByTags() {
		// add tags 
		taskFormDb.addTag(processActivityFormInstanceId1, TagType.TAG_APPLICATION_BY, tagVal1, userId);
		taskFormDb.addTag(processActivityFormInstanceId1, TagType.TAG_TYPE_DIARY_NO, tagVal2 , userId);

		taskFormDb.addTag(processActivityFormInstanceId2, TagType.TAG_TYPE_DIARY_NO, tagVal2, userId);
		
		// search 
		List<String> search1 = taskFormDb.getProcessInstancesByTag(tagVal1);
		Assert.assertEquals(1, search1.size());
		List<String> search2 = taskFormDb.getProcessInstancesByTag(tagVal2);
		Assert.assertEquals(2, search2.size());
				
		// delete tags by id
		List<Tag> tags1 = taskFormDb.getTagsByProcessInstance(processInstanceUuid1);
		for (Tag tag : tags1) {
			Assert.assertTrue(taskFormDb.deleteTag(tag.getProcessActivityTagId(), userId));
		}

		List<Tag> tags2 = taskFormDb.getTagsByProcessInstance(processInstanceUuid2);
		for (Tag tag : tags2) {
			Assert.assertTrue(taskFormDb.deleteTag(tag.getProcessActivityTagId(), userId));
		}
				
	}
	
	@Test
	public void createUserDn() {
		UserEntity userDn = new UserEntity();
		userDn.setCategory(UserInfo.CATEGORY_INTERNAL);
		userDn.setSerial(null);
		userDn.setCn("dn cn");
		userDn.setDn("dn");
		userDn.setUuid("uuid-dn");
		UserInfo userInfoDn = taskFormDb.createUser(userDn);

		UserEntity userSer = new UserEntity();
		userSer.setCategory(UserInfo.CATEGORY_INTERNAL);
		userSer.setSerial("ser");
		userSer.setGn("gn");
		userSer.setSn("sn");
		userSer.setCn("ser cn");
		userSer.setDn(null);
		userSer.setUuid("uuid-ser");
		UserInfo userInfoSer = taskFormDb.createUser(userSer);

		UserInfo qDn = taskFormDb.getUserByDn(userDn.getDn());
		System.out.println("userInfoDn=" + userInfoDn);
		Assert.assertEquals(userInfoDn, qDn);
		
		UserInfo qSer = taskFormDb.getUserBySerial(userSer.getSerial());
		System.out.println("userSer=" + userSer);
		Assert.assertEquals(userInfoSer, qSer);
		
	}
*/
	
}
