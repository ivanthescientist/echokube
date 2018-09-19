REGISTRY?=registry.$(BASE_DOMAIN)/
IMAGE_NAME?=echokube
BASE_DOMAIN=kube.dev

########################################################################################################################
# Base local build targets:
########################################################################################################################
build: clean
	mvn install
.PHONY: build

clean:
	mvn clean
.PHONY: clean

########################################################################################################################
# Local docker related targets:
########################################################################################################################
build-image: build
	sudo docker build -t $(REGISTRY)$(IMAGE_NAME) .
.PHONY: build-image

push-image: build-image
	sudo docker push $(REGISTRY)$(IMAGE_NAME)
.PHONY: push-image

kube-install:
	kubectl apply -f echokube.yaml
	kubectl apply -f echokube-ingress.yaml
.PHONY: kube-install