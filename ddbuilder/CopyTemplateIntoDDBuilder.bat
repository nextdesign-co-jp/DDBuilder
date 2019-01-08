
set TEMPLATE=C:\_dev\DDBuilderTemplate\repos\template

set DDBUILDER=C:\_dev\DDBuilder\repos\DDBuilder\templateddb\template
robocopy %TEMPLATE% %DDBUILDER% /MIR /NDL /XD "target"


set DDBUILDER=C:\_dev\DDBuilder\repos\DDBuilder\jp-co-nextdesign-ddbuilder\lib\templateddb\template
robocopy %TEMPLATE% %DDBUILDER% /MIR /NDL /XD "target"

@pause
