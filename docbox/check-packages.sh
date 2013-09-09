#!/bin/bash

for P; do
    dpkg -s "$P" >/dev/null 2>&1 && {
        echo "OK  Package '$P' is installed"
    } || {
        echo "ERR Package '$P' is not installed"
    }
done
