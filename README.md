# Abring android SDK
Abring as a MBAAS (Mobile Backend As A Service) is a service provider that helps developers to have an unbreakable connection with clients during the mobile app

# Preview
![img 1](http://s8.picofile.com/file/8312681284/111.png) 
![img 1](http://s9.picofile.com/file/8312681300/222.png) 

# Requirements
- IDE : android studio
- Minimum SDK : 17

# Usage
**Adding Dependency**
</br>
Add this to `build.gradle` Project level
</br>
```
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```
Add this to `build.gradle` Module:app
</br>
```
dependencies {
  compile 'com.github.msddev:abring-sdk-android:v0.3'
}
```

**Adding The View**
```
AbringRegister abringUser = new AbringRegister 
        .RegisterBuilder()                     
        .setUsername("string required")        
        .setPassword("string required")        
        .setName("string optional")            
        .setPhone("string optional")           
        .setEmail("string optional")           
        .setAvatar(File file optional)         
        .build();                              
        
abringUser.register(activity, new AbringCallBack() {                                                              
    @Override                                                                                                     
    public void onSuccessful(Object response) {                                                                   
        AbringRegisterModel register = (AbringRegisterModel) response;                                            
        Toast.makeText(activity, "اطلاعات با موفقیت ثبت شد", Toast.LENGTH_SHORT).show();                          
    }                                                                                                             
                                                                                                                  
    @Override                                                                                                     
    public void onFailure(Object response) {                                                                      
        AbringApiError apiError = (AbringApiError) response;                                                      
        Toast.makeText(mActivity,                                                                                 
                AbringCheck.isEmpty(apiError.getMessage()) ? "متاسفانه خطایی رخ داده است" : apiError.getMessage(),
                Toast.LENGTH_SHORT).show();                                                                       
    }                                                                                                             
});                                                                                                               
```
