#!/usr/bin/env bash
vagrant ssh-config  > vagrant-ssh-config
scp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -F vagrant-ssh-config "default:/etc/kubernetes/admin.conf" ~/.kube/config
sed -i "s|server:.*|server: https://$1:6443|g" ~/.kube/config
