package utils;

import java.security.MessageDigest;

public class MonkeyEncryption
{
	private MonkeyEncryption(){}

	public static String toSHA256(String value)
	{
		String returnValue = "";
		try
		{
			MessageDigest md = MessageDigest.getInstance("SHA-256");			 
			byte byteData[] = md.digest(value.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++)
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			 
			returnValue = sb.toString();
		} catch (Exception ex) {
			System.out.println("Exception in toSHA256, Message: " + ex);
		}
		return returnValue;
	}
}