@echo off

call USER_DIR.bat

if "%JCR_HOME%"=="" goto LABEL_NOEXEC

set CLASSPATH=%CLASSPATH%;%JCR_HOME%\lib\

set CLASSPATH=%CLASSPATH%;%JCR_HOME%\lib\commons-logging-1.1.jar
set CLASSPATH=%CLASSPATH%;%JCR_HOME%\lib\dom4j-1.6.1.jar
set CLASSPATH=%CLASSPATH%;%JCR_HOME%\lib\geronimo-stax-api_1.0_spec-1.0.jar
set CLASSPATH=%CLASSPATH%;%JCR_HOME%\lib\junit-3.8.1.jar
set CLASSPATH=%CLASSPATH%;%JCR_HOME%\lib\log4j-1.2.13.jar
set CLASSPATH=%CLASSPATH%;%JCR_HOME%\lib\org.eclipse.core.contenttype_3.4.100.v20100505-1235.jar
set CLASSPATH=%CLASSPATH%;%JCR_HOME%\lib\org.eclipse.core.jobs_3.5.1.R36x_v20100824.jar
set CLASSPATH=%CLASSPATH%;%JCR_HOME%\lib\org.eclipse.core.resources_3.6.1.R36x_v20110131-1630.jar
set CLASSPATH=%CLASSPATH%;%JCR_HOME%\lib\org.eclipse.core.runtime_3.6.0.v20100505.jar
set CLASSPATH=%CLASSPATH%;%JCR_HOME%\lib\org.eclipse.equinox.common_3.6.0.v20100503.jar
set CLASSPATH=%CLASSPATH%;%JCR_HOME%\lib\org.eclipse.equinox.preferences_3.3.0.v20100503.jar
set CLASSPATH=%CLASSPATH%;%JCR_HOME%\lib\org.eclipse.jdt.core_3.6.2.v_A76_R36x.jar
set CLASSPATH=%CLASSPATH%;%JCR_HOME%\lib\org.eclipse.osgi_3.6.2.R36x_v20110210.jar
set CLASSPATH=%CLASSPATH%;%JCR_HOME%\lib\poi-3.7-20101029.jar
set CLASSPATH=%CLASSPATH%;%JCR_HOME%\lib\poi-ooxml-3.7-20101029.jar
set CLASSPATH=%CLASSPATH%;%JCR_HOME%\lib\poi-ooxml-schemas-3.7-20101029.jar
set CLASSPATH=%CLASSPATH%;%JCR_HOME%\lib\poi-scratchpad-3.7-20101029.jar
set CLASSPATH=%CLASSPATH%;%JCR_HOME%\lib\xmlbeans-2.3.0.jar

set CLASSPATH=%CLASSPATH%;%JCR_HOME%\lib\jclassreport.jar

@echo お待ちください・・・
java -jar lib\jclassreport.jar
goto LABEL_END

:LABEL_NOEXEC
@echo エラー：USER_INFO.BATファイルの名前と内容が正しく設定されているか確認してください。
@pause
:LABEL_END
@echo on

