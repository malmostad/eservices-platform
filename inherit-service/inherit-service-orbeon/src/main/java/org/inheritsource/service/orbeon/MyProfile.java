package org.inheritsource.service.orbeon;

import java.util.logging.Logger;

/**
 * MyProfile
 *
 */
public class MyProfile 
{
	public static final Logger log = Logger.getLogger(MyProfile.class.getName());
	private static final String MYPROFILEFORMPATH="malmo/profil";

	OrbeonService orbeonService = new OrbeonService();
	
	public String getEmail(String userUuid) {
		String result = 
		orbeonService.getFormDataValue(MYPROFILEFORMPATH, userUuid, "//section-1/email");
		return result;
	}
	
	public static void main( String[] args )
    {
        MyProfile myProfile = new MyProfile();
        System.out.println("email: " + myProfile.getEmail("john"));
    }
}
