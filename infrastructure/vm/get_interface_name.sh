#!/usr/bin/env bash
route | grep -m 1 "^default" | grep -Po "[^ ]+$"