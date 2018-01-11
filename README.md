# InternalVM

This project provides a working Virtual Machine that uses a pseudo-assembly code capable of accessing to a restricted number of objects and functions of your project

You must first create a Provider class implementing VMProviderInterface, and programming this class to return informations about every object and function you can access into your scripts.

The perser and the compiler will access to those data to map the functions, and check the syntax of your script. 
The compiler will then create the pseudo-assembly. The Virtual Machine will then be able to execute this pseudo-assembly and call all the functions and objects required from your same application in runtime
