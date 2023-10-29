package com.example.purchaserequests;

/**
 *
 * @author spb.tav
 */
public class GetDataUserAD {
    
    public String userName;
    public String userDomain;
    
    public GetDataUserAD(){
        String osName = System.getProperty( "os.name" ).toLowerCase(); //OS name
        if( osName.contains( "windows" ) ){
            this.userName = new com.sun.security.auth.module.NTSystem().getName();
            this.userDomain = new com.sun.security.auth.module.NTSystem().getDomain();
        }else{
            this.userName   = "Only for NTSystem!!!";
            this.userDomain = "Only for NTSystem!!!";
        }
    }
}
