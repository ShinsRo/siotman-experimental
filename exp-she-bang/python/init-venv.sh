#!/bin/bash

cd "$(dirname "$0")" || exit

if [ ! "$(command -v python3)" ]; then
  echo 'Python 이 존재하지 않습니다.' >&2
  exit 1
fi

# venv 셋업
if [ ! -f "venv/bin/activate" ]; then
  python3 -m venv "venv"
fi

# venv activate
. venv/bin/activate
pip3 -v install -r requirements.txt

# run
./index.py "$@"
