#!/usr/bin/env bash

#case 语法练习
case ${1} in
    "hello" )
        echo "hello ,how are you ?"
        ;;
    "") #为空
        echo "you Must input paramters, ex> {${0} someword}" ;;
    "--help")
        echo "Usage: ${1} [OPTION]... "
        echo "${0} hello  print hello.how are you";;
    *) #任意个
        echo "Usage ${0} "
        ;;
esac
