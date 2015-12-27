package com.stuff.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtil {

	private static SimpleDateFormat dateFormatterForCompare = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat dateFormatterForDisplay = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat timeFormatterForDisplay = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static int toInt(byte[] bytes){
		switch(bytes.length){
		case 1:
			return 0xff&bytes[0];
		case 2:
			return ((bytes[0]<<8)&0xff00)+(bytes[1]&0xff);
		case 3:
			return ((bytes[0]<<16)&0xff0000) + ((bytes[1]<<8)&0xff00) + (bytes[2]&0xff);
		default:
			return ((bytes[0]<<24)&0xff000000) + ((bytes[1]<<16)&0xff0000) + ((bytes[2]<<8)&0xff00) + (bytes[3]&0xff);
		}
	}
	
	public static short toShort(byte[] bytes){
		switch(bytes.length){
		case 1:
			return (short) (0xff&bytes[0]);
		default :
			return (short) (((bytes[0]<<8)&0xff00)+(bytes[1]&0xff));
		}
	}
	
	public static byte[] toByteArray(int value) {
	    return new byte[] {
	        (byte) (value >> 24),
	        (byte) (value >> 16),
	        (byte) (value >> 8),
	        (byte) value};
	}
	
	public static byte[] toByteArray(short value) {
	    return new byte[] {
	        (byte) (value >> 8),
	        (byte) value};
	}
	
	
	public static int[] toIntArray(byte[] bytes){
		int[] ret = new int[bytes.length/4];
		for(int i=0; i< bytes.length; ) {
			byte[] tmp = new byte[4];
			for(int j=0; j<4; j++) {
				tmp[j] = bytes[i+j];
			}
			ret[i/4] = toInt(tmp);
			i+=4;
		}
		return ret;
	}
	
	public static short[] toShortArray(byte[] bytes){
		short[] ret = new short[bytes.length/2];
		for(int i=0; i< bytes.length; ) {
			byte[] tmp = new byte[2];
			for(int j=0; j<2; j++) {
				tmp[j] = bytes[i+j];
			}
			ret[i/2] = toShort(tmp);
			i+=2;
		}
		return ret;
	}
	
	/**
	 * byte[] to short[], every gap number points get one point.
	 * 
	 * @param bytes byte array
	 * @param gap the gap number
	 * @return short array
	 */
	public static short[] toShortArray(byte[] bytes, int gap)
	{
		int bLen = bytes.length/2;
		int retLen = bLen/gap + (bLen%gap == 0 ? 0 : 1);
		int skip = 2 * gap;
		int retIndex = 0;
		
		short[] ret = new short[retLen];
		for(int i=0; i< bytes.length; ) 
		{
			byte[] tmp = new byte[2];
			for(int j=0; j<2; j++) 
			{
				tmp[j] = bytes[i+j];
			}
			ret[retIndex] = toShort(tmp);
			
			i += skip;
			retIndex++;
		}
		
		return ret;
	}
	
	public static byte[] toByteArray(short[] shorts){
		byte[] ret = new byte[shorts.length*2];
		for(int i=0; i< shorts.length; i++) {
			short value = shorts[i];
			byte[] tmp = toByteArray(value);
			for(int j=0; j<2; j++) {
				ret[i*2 + j] = tmp[j];
			}
		}
		return ret;
	}
	
	public static byte[] toByteArray(Short[] shorts){
		byte[] ret = new byte[shorts.length*2];
		for(int i=0; i< shorts.length; i++) {
			short value = shorts[i];
			byte[] tmp = toByteArray(value);
			for(int j=0; j<2; j++) {
				ret[i*2 + j] = tmp[j];
			}
		}
		return ret;
	}
	
	public static byte[] toByteArray(int[] ints){
		byte[] ret = new byte[ints.length*4];
		for(int i=0; i< ints.length; i++) {
			int value = ints[i];
			byte[] tmp = toByteArray(value);
			for(int j=0; j<4; j++) {
				ret[i*4 + j] = tmp[j];
			}
		}
		return ret;
	}
	
	public static byte[] toByteArray(Integer[] ints){
		byte[] ret = new byte[ints.length*4];
		for(int i=0; i< ints.length; i++) {
			int value = ints[i];
			byte[] tmp = toByteArray(value);
			for(int j=0; j<4; j++) {
				ret[i*4 + j] = tmp[j];
			}
		}
		return ret;
	}
	
	public static boolean isEmptyString(String s)
	{
		return s == null || s.isEmpty();
	}
	
	public static boolean hasEmptyString(String ... strings)
	{
		if(strings == null)
			return true;
		for(String s : strings)
		{
			if(isEmptyString(s))
				return true;
		}
		return false;
	}
	
	public static boolean notEmptyString(String s)
	{
		return !isEmptyString(s);
	}
	
	
	public static String dateToStringForCompare(Date date)
	{
		return dateFormatterForCompare.format(date);
	}
	
	public static String dateToStringForDisplay(Date date)
	{
		return dateFormatterForDisplay .format(date);
	}
	
	public static String timeToStringForDisplay(Date date)
	{
		return timeFormatterForDisplay.format(date);
	}
	
	public static Date timeToDate(Date date)
	{
		String dateStr = dateFormatterForCompare.format(date);
		try
		{
			Date dateDate = dateFormatterForCompare.parse(dateStr);
			return dateDate;
		}
		catch (ParseException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static String convertBirthToAge(Date birthDate)
	{
		if (birthDate == null)
		{
			return null;
		}
		String age = null;
		Date currentDate = new Date();
		java.util.Date birthDateJava = new java.util.Date(birthDate.getTime());
		java.util.Date currentDateJava = new java.util.Date(currentDate.getTime());
		int ageInt = currentDateJava.getYear() - birthDateJava.getYear();
		age = Integer.toString(ageInt);
		return age;
	}
	
	/**
	 * 用户名以字母开头
	 * 由字母，数字，下划线组成
	 * 6到20位
	 * 
	 * @return true is available
	 */
	public static boolean checkUserAvailable(String username)
	{
		if(username == null)
			return false;
		
		String pattern = "^[a-zA-Z][a-zA-Z0-9_]{5,19}$";
		return username.matches(pattern);
	}
	
    /**
     * 如果是合法的用户名，则返回1
     * 如果是合法的身份证，返回2
     * 
     * 如果不是合法的用户名也不是合法的身份证则返-1
     */
    public static int checkClientIdType(String clientId)
    {
    	if(DataUtil.checkUserAvailable(clientId))
    		return 1;
    	
    	if(IDCardValidator.isValidateIDCard(clientId))
    		return 2;
    	
    	else
    		return -1;
    }
    
    /**
     * @return true if validate
     */
    public static boolean isValidatePhoneNumber(String phone)
    {
    	if(phone == null)
    		return false;
    	//String regExp = "/^(13[0-9]|14[57]|15[012356789]|17[678]|18[0-9])[0-9]{8}$/";
//    	String regExp = "^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$";  
    	String regExp = "^[1](3|4|5|7|8)[0-9]{9}$";
    	Pattern p = Pattern.compile(regExp);  
    	Matcher m = p.matcher(phone); 
    	return m.find();
    }

    
    public static void main(String[] argws)
    {
    	System.out.println(isValidatePhoneNumber("14488002255"));
    	System.out.println(isValidatePhoneNumber("17011225566"));
    	System.out.println(isValidatePhoneNumber("19022335566"));
    	System.out.println(isValidatePhoneNumber("13900223355"));
    	System.out.println(isValidatePhoneNumber("188001122552"));
    }
	
    
    public static boolean isValidateEmail(String email)
    {
    	if(email == null)
    		return false;
    	String regExp = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
    	Pattern p = Pattern.compile(regExp);
    	Matcher m = p.matcher(email);
    	return m.find();
    }
    
    public static int compareDate(Date d1, Date d2)
    {
    	String s1 = DataUtil.dateToStringForCompare(d1);
    	String s2 = DataUtil.dateToStringForCompare(d2);
    	return s1.compareTo(s2);
    }
    
    /**
     * @return 6 number string
     */
	public static String randomNumber()
	{
		 int code = (int) ((Math.random() * 9 + 1) * 100000);
		return code + ""; //$NON-NLS-1$
	}
	
}
