include ../../includes.mk

all: delete-vm create-vm copy-kube-config
.PHONY: all

create-vm:
	@vagrant up --provision
.PHONY: create-vm

get-host-interface-name:
	@echo $(shell ./get_interface_name.sh)
.PHONY: get-host-interface-name

get-vm-interface-name:
	@vboxmanage showvminfo kubedevnode | grep "Host-only Interface" | grep -Po "'([^']+)'" | grep -Po "[^']+"
.PHONY: get-vm-interface-name

delete-vm:
	@vagrant destroy -f
.PHONY: delete-vm

copy-kube-config:
	@./copy-kube-config.sh $(BASE_DOMAIN)
.PHONY: copy-config