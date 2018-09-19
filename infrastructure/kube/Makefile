include ../../includes.mk


# Unfortunately this does not work because Tiller takes quite some time to initialize, so it fails on install-ingress step
# TODO: figure out a way to wait for tiller.
all: initialize-cluster install-secret install-network install-helm install-ingress install-registry install-registry-secret
.PHONY: all

initialize-cluster:
	# This allows POD scheduling on master node, which is a must because we're runnning a single-node cluster.
	kubectl taint nodes --all node-role.kubernetes.io/master- || true
.PHONY: initialize-cluster

install-ingress:
	helm install stable/nginx-ingress --name ingress-nginx-release \
		--set controller.ingressClass=nginx \
		--set controller.service.externalIPs[0]=192.168.99.100
.PHONY: install-ingress

delete-ingress:
	helm del --purge ingress-nginx-release
.PHONY: delete-ingress

install-network:
	./install-network.sh
.PHONY: install-network

install-helm:
	helm init
	./patch_tiller.sh
.PHONY: install-helm

delete-registry:
	kubectl apply --prune -f registry.yaml
	kubectl apply --prune -f registry-ingress.yaml
.PHONY: delete-registry

install-registry:
	kubectl apply -f registry.yaml
	kubectl apply -f registry-ingress.yaml
.PHONY: install-registry

install-secret:
	kubectl create secret tls kube-dev-secret --key ../certs/output/domain.key.pem --cert ../certs/output/domain.crt
.PHONY: install-secret

install-registry-secret:
	kubectl create secret docker-registry registry-secret \
		--docker-server=https://registry.kube.dev \
		--docker-username=user \
		--docker-password=user \
		--docker-email=user@user
.PHONY: install-registry-secret