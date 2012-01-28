#!/usr/bin/env python

import random

f = open('Codex.java', 'w')

f.write("package team133;\n\nimport team133.*;\n\npublic class Codex {\n\n  public static int[] code_by_round = {")

for i in xrange(1337):
  f.write('\n    ')
  f.write(str(random.randint(-(2 ** 31), (2 ** 31) - 1)))
  f.write(',')

f.write("};\n\n}")

f.close()
