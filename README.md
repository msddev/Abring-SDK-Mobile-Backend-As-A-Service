# Abring android SDK
Abring as a MBAAS (Mobile Backend As A Service) is a service provider that helps developers to have an unbreakable connection with clients during the mobile app

[![NPM version](https://img.shields.io/badge/Download-0.3-brightgreen.svg)](https://github.com/msddev/abring-sdk-android)

# Preview
![img 1](http://s8.picofile.com/file/8312681284/111.png) 
![img 1](http://s9.picofile.com/file/8312681300/222.png) 

# Requirements
- IDE : android studio
- Minimum SDK : 17

# Installation

**Adding Dependency**
</br>
Add this to `build.gradle` Project level
</br>
```java
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```
Add this to `build.gradle` Module:app
</br>
```java
dependencies {
  compile 'com.github.msddev:abring-sdk-android:v0.3'
}

```
Add this to `Application` class:
</br>
```java
new Abring.Builder(this)
                .setPackageName("Your abring package name")
                .build();
```
# Abring services

# User service (Auth)
</br>
</br>
**1. Register**
> Register a user with a username, password required and name, phone, email, avatar optional.

The avatar is from the File Object type.

- Whitout abring UI :
```java
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

- Whit abring UI :
```java
AbringRegister abringUser = new AbringRegister
        .DialogBuilder()
        .setName(true/false)
        .setPhone(true/false)
        .setEmail(true/false)
        .setAvatar(true/false)
        .build();
        
abringUser.showDialog(getSupportFragmentManager(), activity, new AbringCallBack() {
    @Override
    public void onSuccessful(Object response) {
        AbringRegisterModel register = (AbringRegisterModel) response;
    }

    @Override
    public void onFailure(Object response) {
        AbringApiError apiError = (AbringApiError) response;
    }
});
```
**2. Mobile Register**
> Register a user with a mobile number with pattern `09123456789` required and name, phone, email, avatar optional.

The avatar is from the File Object type.

- Whitout abring UI :
