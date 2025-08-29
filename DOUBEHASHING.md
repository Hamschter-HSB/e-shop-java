# How to enable double hashing
**Bitcoin is using SHA-256 double hashing for more security reasons.**
We can implement it in our project too.

/client/ui/viewmodel/LoginAndRegistrationViewModel.java
--> line 40
*Change:*
```
String password = PasswordHasher.hashPassword(new String(charPassword));
```
*to:*
```
String password2 = PasswordHasher.hashPassword(new String(charPassword));
String password = PasswordHasher.hashPassword(password2);   // Double hashing to match stored password
```
*Re-do it for the following things.*

--> line 90
```
String password2 = PasswordHasher.hashPassword(new String(charPassword));
String password = PasswordHasher.hashPassword(password2);   // Double hashing to match stored password
```

--> line 128
```
String password2 = PasswordHasher.hashPassword(new String(passwordField.getPassword()));
String password = PasswordHasher.hashPassword(password2);   // Double hashing to match stored password
```

Thats it!
Remember that every password need to be changed.


---------------------------------------------------------------------

> *Example hashed password*:
> 
> 1234
> 
> 03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4
> 
> 756bc47cb5215dc3329ca7e1f7be33a2dad68990bb94b76d90aa07f4e44a233a
