#!/bin/sh
#DEBUG="-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=n"
DEBUG=
java $DEBUG -ea -classpath build:libs/jscheme.jar -verbose:gc -Xmx128000000 org.vermin.mudlib.Main $1 $2 $3
