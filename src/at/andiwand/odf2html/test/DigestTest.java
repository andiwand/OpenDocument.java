package at.andiwand.odf2html.test;

import java.security.MessageDigest;
import java.util.Arrays;


public class DigestTest {
	
	public static void main(String[] args) throws Throwable {
		byte[] checksum = {-101, 79, -78, 78, -35, 109, 29, -120, 48, -30, 114,
				57, -126, 99, -51, -65, 2, 107, -105, 57, 44, -61, 83, -121,
				-71, -111, -36, 2, 72, -90, 40, -7};
		byte[] file = {3, 0};
		
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		digest.update(file);
		
		System.out.println(Arrays.toString(checksum));
		System.out.println(Arrays.toString(digest.digest()));
	}
}