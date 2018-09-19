include ../includes.mk

########################################################################################################################
# Dev Kubernetes setup related targets:
########################################################################################################################
setup-certs:
	@$(MAKE) -C certs all
.PHONY: setup-certs

setup-vm:
	@$(MAKE) -C vm all
.PHONY: setup-vm

setup-kube:
	@$(MAKE) -C kube all
.PHONY: setup-kube