Requirements - Gradle 4.7 and Java 1.8

Run the server using 
```
gradle bootrun
```

To run on Ubuntu you may need to install `rng-tools` to provide randomness for SecureRandom.

For the mail service to work you need to create a password.dat file in the root of this git repo with 2 lines - 
First line contains email
Second line contains password

Make sure to enable remote smtp access in you gmail/email settings.
