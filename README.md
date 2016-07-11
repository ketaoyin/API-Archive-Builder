# API Archive Builder
Uses Java JDK SE 8 documentation files downloaded from Oracle.com

Program uses list of JDK packages to locate them in documentation files as well as corresponding classes. Grabber is used to extract Method table information from class pages. Outputs API information into .txt file in format of <PackageName><ClassName><MethodName>.

ToDo:
Currently only grab class methods. Need to extend functionality to interfaces as well.
