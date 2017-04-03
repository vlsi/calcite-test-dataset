#!/bin/sh
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to you under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# Size of Swap file in Gb
swapsize=4096

# does the swap file already exist?
grep -q "swapfile" /etc/fstab

# if swap not already created then create it
if [ $? -ne 0 ]; then
	echo 'swapfile not found. Adding swapfile.'
	fallocate -l ${swapsize}M /swapfile
	chmod 600 /swapfile
	mkswap /swapfile
	swapon /swapfile
	echo '/swapfile none swap defaults 0 0' >> /etc/fstab
else
	echo 'swapfile found. No changes made.'
fi

# output results to terminal
cat /proc/swaps
cat /proc/meminfo | grep Swap
