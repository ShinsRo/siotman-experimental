#!/usr/bin/env python
import sys

hello: str = "안녕, Python 으로부터"
version: str = f'{sys.version_info.major}.{sys.version_info.minor}'

print(f"{hello}, - python version : {version}")
