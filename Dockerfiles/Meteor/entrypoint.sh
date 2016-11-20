#!/bin/bash

# Starten von Meteor
( cd /app ; meteor run )

# don't exit
/usr/bin/tail -f /dev/null