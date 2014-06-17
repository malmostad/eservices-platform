# Groovy diverse hack #

## Skapa MD5 ##

```
def generateMD5(final file) {
   MessageDigest digest = MessageDigest.getInstance("MD5")
   file.withInputStream(){is->
   byte[] buffer = new byte[8192]
   int read = 0
      while( (read = is.read(buffer)) > 0) {
             digest.update(buffer, 0, read);
         }
     }
   byte[] md5sum = digest.digest()
   BigInteger bigInt = new BigInteger(1, md5sum)
   bigInt.toString(16).padLeft(32, '0')
}
```

## SHA-256 ##

```
def sha256Hash = { text ->
    java.security.MessageDigest.getInstance("SHA-256").digest(text.bytes)
            .collect { String.format("%02x", it) }.join('')
}
```
Inkl omformatering till hex. Obs formatet %02x för att inte inledande nollor ska försvinna, vanligt fel.

## Tryck ut classpath i groovyConsole ##

```
println getClass().classLoader.URLs
```

## Converting ISO-8859-1 Properties to UTF-8 ##

This is a Java problem, properties files must be ISO-8859-1. Many complicated solutions on the 'net. The easiest way is not to convert the properties files but convert on output.


```
return new String(val.getBytes("ISO-8859-1"), "UTF-8");
```

Groovy uses UTF-8 from the start.

## Comments in GSP ##

The normal gsp comment is

```
<%-- This is my comment --%>
```
Maybe old complaints that such comments do not work in templates. In such case the only way to fix it is this,

```
<% /*  some server side comment */ %>
```
Referred to as *an abomination* by some people.

NOTE: I have tested ordinary comments in a template and there is no problem.

