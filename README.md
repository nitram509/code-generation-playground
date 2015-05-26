
## Motivation

General object serializer (like [Jackson](http://jackson.codehaus.org/)) are often used in Java & JSON development contexts. 
They are relatively easy to set up and do a good job in general.
The actual mapping is weaved at runtime and most often its declared and configured at compile time.
This ...:

   * eases the work for developers, especially when models change often
   * pays some extra performance costs for reflection
   * makes (static) code navigation impossible (e.g. see usages of a given field)
   * needs to testing the serializer configuration

To me, the main motivation seems to be easing my developer live.
But guess what, there are other ways to make my live easier,
when it comes to object serialization...

**Viva la Code Generation!**

## WriterGenerator

Generates a serializer (a.k.a. writer) class for a given model/object.
The writer produces a ```javax.json.JsonObject``` based on JSR 353: Java API for JSON Processing.
The sources are generated via [CodeModel](https://codemodel.java.net/) via default formatter.
