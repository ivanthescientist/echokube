include ../../includes.mk

all: create-configs create-root-key create-root-ca create-domain-key create-domain-csr create-domain-cert
	@echo "Finished generating certs, import output/ca.pem into your browser and use output/domain.crt and output/domain.key for your server"
.PHONY: all

create-configs:
	@sed "s/<base-domain-name>/$(BASE_DOMAIN)/g" openssl.cnf.template > ./output/openssl.cnf
.PHONY: create-configs

create-root-key:
	@openssl genrsa -out ./output/ca.key.pem 2048
.PHONY: create-root-key

create-root-ca:
	@openssl req \
		-new \
		-x509 \
		-nodes \
		-subj "/CN=mydevca" \
		-extensions v3_ca \
		-days 3650 \
		-key ./output/ca.key.pem \
		-out ./output/ca.pem \
		-config ./output/openssl.cnf
.PHONY: create-root-ca

create-domain-key:
	@openssl genrsa -out ./output/domain.key.pem 2048
.PHONY: create-domain-key

create-domain-csr:
	@openssl req -subj "/CN=$(BASE_DOMAIN)" \
		-extensions v3_req \
		-new \
		-key ./output/domain.key.pem \
		-out ./output/domain.csr
.PHONY: create-domain-csr

create-domain-cert:
	@openssl x509 -req \
		-extensions v3_req \
		-days 3650 \
		-in ./output/domain.csr \
		-CA ./output/ca.pem \
		-CAkey ./output/ca.key.pem \
		-CAcreateserial \
		-out ./output/domain.crt \
		-extfile ./output/openssl.cnf
.PHONY: create-domain-cert

install-ca-locally:
	sudo mkdir -p /etc/docker/certs.d/registry.kube.dev
	sudo cp ./output/ca.pem /etc/docker/certs.d/registry.kube.dev/ca.crt
.PHONY: install-ca-locally