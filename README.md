# DDBuilder & DDBuilderTemplate
A software tool to generate Java web applications from Domain Driven Design domain model.

## Description
1. Supports iterative development with domain driven design.
2. By automatically generating Java web applications from the domain model, make the DDD iteration light weight and shorten the iteration cycle.
3. The tool consists of DDBuilder and DDBuilderTemplate.
- DDBuilder (tool body) 
https://github.com/nextdesign-co-jp/DDBuilder.git
- DDBuildertemplate (template) 
https://github.com/nextdesign-co-jp/DDBuilderTemplate.git  


## Requirement
1. Eclipse-jee.
2. Java 5 or later.

## Usage
1. Import and use DDBuilder and DDBuilderTemplate into Eclipse respectively.
2. DDBuilder is a Java project.
3. DDBuilderTemplate is a JavaEE project.
4. After modifying the DDBuilderTemplate, it is necessary to reflect the template in the DDBuilder side.
Please refer to the contents of "CopyTemplateIntoDDBuilder.bat" of DDBuilder for the method to reflect.  
5. How to start DDBuilder   
Start jp.co.nextdesign.ddb.ui.main.BdMainWindow  
6. How to start DDBuilderTemplate  
Start template on Server.  
Example: template -> right click -> debug -> debug on server  
7. How to create an executable JAR for DDBuilder  
DDBuilder -> right click -> export -> executable Jar file  
- Activation configuration: BdMainWindow  
- Export destination: ...\ddbuilder\jp-co-nextdesign-ddbuilder\lib  
- Select [Copy required library to subfolder next to generated JAR]  
8. Reference sites  
http://www.nextdesign.co.jp/ddd/en_index.html  
http://www.nextdesign.co.jp/ddd/index.html (Japanese)  

## Licence
[Apache License Version 2.0 January 2004]
http://www.apache.org/licenses/

## Author
Next Design Ltd.
http://www.nextdesign.co.jp/

## References
See the licenses folder.  
We ware inspired by Naked Objects Pattern.

