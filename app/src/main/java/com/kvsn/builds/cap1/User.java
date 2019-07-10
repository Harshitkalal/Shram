package com.kvsn.builds.cap1;

public class User
{

     private String Email;
     private String Id;
     private String Contact_Number;
     private String Aadhar_Number;
     private String Street_No;
     private String Pincode;
     private String State;
     private String City;
     private String Gender;
     private String Profession;
     private String Type;
     private String Name;
     private String Alternate_Contact_Number;

     public User(String email , String id , String contact_Number , String aadhar_Number , String street_No , String pincode , String state , String city , String gender , String profession , String type , String name , String alternate_Contact_Number)
     {
	  Email = email;
	  Id = id;
	  Contact_Number = contact_Number;
	  Aadhar_Number = aadhar_Number;
	  Street_No = street_No;
	  Pincode = pincode;
	  State = state;
	  City = city;
	  Gender = gender;
	  Profession = profession;
	  Type = type;
	  Name = name;
	  Alternate_Contact_Number = alternate_Contact_Number;
     }

     public User()
     {

     }

     public String getEmail()
     {
	  return Email;
     }

     public void setEmail(String email)
     {
	  Email = email;
     }

     public String getId()
     {
	  return Id;
     }

     public void setId(String id)
     {
	  Id = id;
     }

     public String getContact_Number()
     {
	  return Contact_Number;
     }

     public void setContact_Number(String contact_Number)
     {
	  Contact_Number = contact_Number;
     }

     public String getAadhar_Number()
     {
	  return Aadhar_Number;
     }

     public void setAadhar_Number(String aadhar_Number)
     {
	  Aadhar_Number = aadhar_Number;
     }

     public String getStreet_No()
     {
	  return Street_No;
     }

     public void setStreet_No(String street_No)
     {
	  Street_No = street_No;
     }

     public String getPincode()
     {
	  return Pincode;
     }

     public void setPincode(String pincode)
     {
	  Pincode = pincode;
     }

     public String getState()
     {
	  return State;
     }

     public void setState(String state)
     {
	  State = state;
     }

     public String getCity()
     {
	  return City;
     }

     public void setCity(String city)
     {
	  City = city;
     }

     public String getGender()
     {
	  return Gender;
     }

     public void setGender(String gender)
     {
	  Gender = gender;
     }

     public String getProfession()
     {
	  return Profession;
     }

     public void setProfession(String profession)
     {
	  Profession = profession;
     }

     public String getType()
     {
	  return Type;
     }

     public void setType(String type)
     {
	  Type = type;
     }

     public String getName()
     {
	  return Name;
     }

     public void setName(String name)
     {
	  Name = name;
     }

     public String getAlternate_Contact_Number()
     {
	  return Alternate_Contact_Number;
     }

     public void setAlternate_Contact_Number(String alternate_Contact_Number)
     {
	  Alternate_Contact_Number = alternate_Contact_Number;
     }
}
