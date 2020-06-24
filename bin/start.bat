title "database-doc-generator"
cd ..
set JAVA_OPTIONS=-Djava.ext.dirs="bin"
set JAVA_OPTIONS2= -Dfile.encoding="UTF-8"

java  -classpath "%CLASSPATH%" %JAVA_OPTIONS% %JAVA_OPTIONS2%  com.guigu.tool.database.doc.generator.Main

pause

