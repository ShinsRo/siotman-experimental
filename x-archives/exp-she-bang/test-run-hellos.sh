#!/bin/bash

echo '################'

echo '# print-hello.js'
node/print-hello.js

echo '# print-hello.py'
python/print-hello.py

echo '# print-hello.kts'
kotlin/print-hello.kts

#echo '# 잘못된 인터프리터에 직접넘기기 - Error'
#sh node/print-hello.js
#zsh node/print-hello.js
#zsh python/print-hello.py
#node python/print-hello.py
